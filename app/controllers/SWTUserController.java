package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.ProfileInConstruction;
import models.SWTOAuthUser;
import models.enumerations.OAuthClient;
import models.enumerations.SWTGender;
import org.pac4j.play.java.Secure;
import models.SWTUser;
import models.SWTYear;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.api.cache.Cache;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import scala.util.parsing.json.JSONObject;
import views.html.register;
import views.html.*;
import play.cache.*;

import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.play.PlayWebContext;
import org.pac4j.play.store.PlaySessionStore;

import javax.inject.Inject;
import javax.sound.sampled.Control;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

import static play.mvc.Controller.flash;
import static play.mvc.Results.badRequest;
import static play.mvc.Results.redirect;

/**
 * Created by TeoLenovo on 4/10/2017.
 */
public class SWTUserController extends Controller{

    @Inject
    private CacheApi cache;

    @Inject
    private FormFactory formFactory;

    @Inject
    protected static PlaySessionStore playSessionStore;

    private static final String PROFILE_PIC_KEY = "profilePicKey";

    private Logger logger = LoggerFactory.getLogger(getClass());

    public Result listAllUsers() {
        List<SWTUser> list = SWTUser.findAll();
        return ok(listing.render(list));
    }

    public Result loginForm() {
        return ok(login.render(formFactory.form(SWTUser.class)));
    }


    public Result notAuthLoginForm() {
        flash("error", "Login first to access that page!");
        return redirect(routes.SWTUserController.loginForm());
    }

    /**
     * Normal form login
     * @return
     */
    public Result login() {
        DynamicForm form = Form.form().bindFromRequest();
        String usernameOrEmail = form.get("username");
        String password = form.get("password");
        SWTUser user = SWTUser.verifyCredentials(usernameOrEmail, password);
        if (user == null) {
            flash("error", "Invalid login or password. Please try again.");
            return redirect(routes.SWTUserController.login());
        }
        logInUser(user);
        return redirect(routes.Public.landing());
    }

    @Secure(clients = "FacebookClient", authorizers = "custom")
    public Result fblogin() {
        PlayWebContext webContext = new PlayWebContext(ctx(), playSessionStore);
        ProfileManager<CommonProfile> profileManager = new ProfileManager(webContext);
        Optional<CommonProfile> profileOptional = profileManager.get(true);
        if (!profileOptional.isPresent()) {
            flash("error", "Failed login!");
            return redirect(routes.Public.landing());
        }
        //CHANGE THIS - DELEGATE PROFILE PIC TO OAUTH LOGIN, NO NEED TO DO THIS IF USER EXISTS ALSO!!
        CommonProfile profile = profileOptional.get();
        profile.addAttribute(PROFILE_PIC_KEY,
                "http://graph.facebook.com/"+ profile.getId() +"/picture?type=large");
        return oAuthLogin(profile, OAuthClient.FACEBOOK);
    }

    public Result saveUser() {

        SWTUser user;
        boolean editing = currentUser() != null;
        if (editing) {
            user = currentUser();
        } else {
            user = new SWTUser();
        }

        DynamicForm form = formFactory.form().bindFromRequest();
        user.username = form.get("username");
        user.password = form.get("password");
        user.email = form.get("email");
        user.firstName = form.get("firstName");
        user.lastName = form.get("lastName");
        user.anonymous = Boolean.parseBoolean(form.get("anonymous"));
        user.country = new Locale(form.get("country_selector_code"));
        user.gender = SWTGender.toValue(form.get("gender"));
        if(form.get("avatar")!=null) {
            user.profilePictureUrl = form.get("avatar");
        }

        if (editing) {
            user.update();
            logger.debug("updated swt user: " + user.toString());
        } else {
            user.save();
            logger.debug("saved swt user: " + user.toString());
            logInUser(user);
            logger.debug("logged in user: " + user.username);
        }

        //if it was oauth login
        String oauthId = form.get("oauthId");
        OAuthClient client = OAuthClient.convertStringToClass(form.get("client"));
        if (oauthId != null && client != null) {
            SWTOAuthUser swtoAuthUser = new SWTOAuthUser(oauthId, client, user);
            swtoAuthUser.save();
        }

        if (editing) {
            flash("success", "Successfully updated!");
            return redirect(routes.SWTUserController.profile());
        } else {
            flash("success", "Great! Now add your SWT years!");
            return redirect(routes.SWTUserController.placesPanel());
        }

    }

    public Result register(String profileInConstructionHash) {
        ProfileInConstruction profileInConstruction = cache.get(profileInConstructionHash);
        if (profileInConstruction != null) {
            return ok(views.html.register.render(
                    profileInConstruction.user, profileInConstruction.client, profileInConstruction.oauthId));
        }
        // it was not oauth register
        return ok(views.html.register.render(null,null,null));
    }

    private Result oAuthLogin(CommonProfile commonProfile, OAuthClient client) {
        String oauthId = commonProfile.getId();
        SWTUser user;
        user = SWTOAuthUser.getSWTUser(oauthId,client);
        //if user already exists
        if (user != null) {
            logInUser(user);
            return redirect(routes.Public.landing());
        } else {
            user = commonProfileIntoSWTProfile(commonProfile);
            ProfileInConstruction profileInConstruction =
                    new ProfileInConstruction(user, client, oauthId);
            String hashIdentifier = UUID.randomUUID().toString();
            cache.set(hashIdentifier,profileInConstruction,60);
            flash("info", "We filled in some data for you, please" +
                    " ensure that everything is correct and fill the rest!");
            return register(hashIdentifier);
        }
    }

    /**
     * Clears all session data and logs in user
     * @param user
     */
    private void logInUser(SWTUser user) {
        session().clear();
        session("username", user.username);
    }

    /**
     * Converting {@link CommonProfile} into {@link SWTUser}
     * Use PROFILE_PIC_KEY as attribute for profile picture
     * @param commonProfile
     * @return
     */
    private SWTUser commonProfileIntoSWTProfile(CommonProfile commonProfile) {
        SWTUser user = new SWTUser();
        user.firstName = commonProfile.getFirstName();
        user.lastName = commonProfile.getFamilyName();
        user.email = commonProfile.getEmail();
        user.livingLocation = commonProfile.getLocation();
        user.profilePictureUrl = (String)commonProfile.getAttribute(PROFILE_PIC_KEY);
        user.gender = SWTGender.toValue(commonProfile.getGender().toString());
        return user;
    }

    public Result logout() {
        session().clear();
        return redirect(routes.SWTUserController.loginForm());
    }

    public static SWTUser currentUser() {
        return SWTUser.findUserByUsername(ctx().session().get("username"));
    }


        public static CommonProfile currentPACUser() {
            PlayWebContext webContext = new PlayWebContext(ctx(), playSessionStore);
            ProfileManager<CommonProfile> profileManager = new ProfileManager(webContext);
            Optional<CommonProfile> profile = profileManager.get(true);
            if (profile.isPresent()) {
                return profile.get();
            }
            return null;
        }


    public Result profile() {
        SWTUser user = currentUser();
        return ok(profile.render(user, currentPACUser()));
    }

    public Result editProfile() {
        SWTUser user = currentUser();
        return ok(views.html.register.render(user,null,null));
    }

    /**
     * Used for ajax calls from user register form
     */
    public Result checkUsername() {
        DynamicForm form = formFactory.form().bindFromRequest();
        String username = form.get("username");
        ObjectNode result = Json.newObject();
        SWTUser currentUser = currentUser();
        if(currentUser !=null) {
          if(currentUser.username.equals(username)) {
              result.put("valid", true);
              return ok(result);
          }
        }
        if (SWTUser.findUserByUsername(username) != null) {
            result.put("valid", false);
            result.put("message", "This username is already taken!");
        } else {
            result.put("valid", true);
        }
        return ok(result);
    }
    /**
     * Used for ajax calls from user register form
     */
    public Result checkEmail() {
        DynamicForm form = formFactory.form().bindFromRequest();
        String email = form.get("email");
        SWTUser currentUser = currentUser();
        ObjectNode result = Json.newObject();
        if(currentUser !=null) {
            if(currentUser.email.equals(email)) {
                result.put("valid", true);
                return ok(result);
            }
        }
        if (SWTUser.findUserByEmail(email) != null) {
            result.put("valid", false);
            result.put("message", "Account with this mail already exists!");
        } else {
            result.put("valid", true);
        }
        return ok(result);
    }

    public Result placesPanel() {
        SWTUser user = controllers.SWTUserController.currentUser();
        return ok(placesPanel.render(user));
    }

}
