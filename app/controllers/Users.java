package controllers;

import models.Rating;
import models.User;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.play.PlayWebContext;
import org.pac4j.play.java.Secure;
import org.pac4j.play.store.PlaySessionStore;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;

import javax.inject.Inject;
import javax.jws.soap.SOAPBinding;
import java.util.List;
import java.util.Optional;


/**
 * Created by teo on 11/27/16.
 */
public class Users extends Controller {

    @Inject
    private FormFactory formFactory;

@Secure(clients = "FacebookClient", authorizers = "custom")
    public Result listAllUsers() {
        List<User> list = User.findAll();
        return ok(listing.render(list));
    }

    public Result logout() {
        session().clear();
        return redirect(routes.Public.landing());
    }

    @Secure(clients = "FacebookClient", authorizers = "custom")
    public Result profile() {
        CommonProfile profile = CurrentUserUtility.getUserProfile();
        if(profile == null) {
            ok("no user logged in!");
        }
        return ok(views.html.profile.render(profile));
    }

//    user for helping views to get username
    public static User currentUser() {
        return User.findUserByUsername(ctx().session().get("username"));
    }




}
