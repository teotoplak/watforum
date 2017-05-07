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
        DynamicForm form = formFactory.form().bindFromRequest();
        String username = form.get("username");
        String password = form.get("password");
        String email = form.get("email");
        String firstName = form.get("firstName");
        String lastName = form.get("lastName");
        String contact = form.get("contact");
        URI contactURI = null;
        try {
        contactURI = new URI(contact);
        } catch (URISyntaxException ex) {}

        Locale country = new Locale(form.get("country_selector_code"));
        SWTGender gender = SWTGender.toValue(form.get("gender"));
        String profilePictureUrl = form.get("avatar");
        SWTUser user = new SWTUser(username, password, firstName, lastName, profilePictureUrl, contactURI, null,
                gender, email, null, country);
        user.save();

        //if it was oauth login
        String oauthId = form.get("oauthId");
        OAuthClient client = OAuthClient.convertStringToClass(form.get("client"));
        if (oauthId != null && client != null) {
            SWTOAuthUser swtoAuthUser = new SWTOAuthUser(oauthId, client, user);
            swtoAuthUser.save();
        }

        flash("success", "Profile created!");
        logInUser(user);
        return redirect(routes.SWTUserController.profile());
    }

    public Result register(String profileInConstructionHash) {
        ProfileInConstruction profileInConstruction = cache.get(profileInConstructionHash);
        if (profileInConstruction != null) {
            return ok(views.html.register.render(profileInConstruction));
        }
        return ok(views.html.register.render(null));
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
            return register(hashIdentifier);
        }
    }

    private void logInUser(SWTUser user) {
        session().clear();
        session("username", user.username);
    }

    private SWTUser commonProfileIntoSWTProfile(CommonProfile commonProfile) {
        String firstName = commonProfile.getFirstName();
        String lastName = commonProfile.getFamilyName();
        URI linkToProfile = commonProfile.getProfileUrl();
        if (linkToProfile == null) {
            try{
            linkToProfile = (URI)commonProfile.getAttribute("link");
            } catch (Exception ignorable){}
        }
        String email = commonProfile.getEmail();
        String livingLocation = commonProfile.getLocation();
        String profilePictureUrl = (String)commonProfile.getAttribute(PROFILE_PIC_KEY);
        SWTGender gender = SWTGender.toValue(commonProfile.getGender().toString());
        SWTUser user = new SWTUser(null, null, firstName, lastName, profilePictureUrl, linkToProfile,
                null, gender, email, livingLocation, null);
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

    /**
     * Used for ajax calls from user register form
     */
    public Result checkUsername() {
        DynamicForm form = formFactory.form().bindFromRequest();
        String username = form.get("username");
        ObjectNode result = Json.newObject();
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
        ObjectNode result = Json.newObject();
        if (SWTUser.findUserByEmail(email) != null) {
            result.put("valid", false);
            result.put("message", "Account with this mail already exists!");
        } else {
            result.put("valid", true);
        }
        return ok(result);
    }

}
