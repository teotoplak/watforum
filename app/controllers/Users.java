package controllers;

import models.Rating;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;

import javax.inject.Inject;
import javax.jws.soap.SOAPBinding;
import java.util.List;


/**
 * Created by teo on 11/27/16.
 */
@Security.Authenticated(Secured.class)
public class Users extends Controller {

    @Inject
    private FormFactory formFactory;

    public Result listAllUsers() {
        List<User> list = User.findAll();
        return ok(listing.render(list));
    }

    public Result logout() {
        session().clear();
        return redirect(routes.Public.loginForm());
    }

    public Result profile() {
        User user = currentUser();
        List<Rating> userRatings = Rating.userRatings(user.id);
        return ok(profile.render(user,userRatings));
    }

//    user for helping views to get username
    public static User currentUser() {
        return User.findUserByUsername(ctx().session().get("username"));
    }


}
