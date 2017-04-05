package controllers;

import models.Rating;
import models.WatUser;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;

import javax.inject.Inject;
import java.util.List;


/**
 * Created by teo on 11/27/16.
 */
@Security.Authenticated(Secured.class)
public class Users extends Controller {

    @Inject
    private FormFactory formFactory;

    public Result listAllUsers() {
        List<WatUser> list = WatUser.findAll();
        return ok(listing.render(list));
    }

    public Result logout() {
        session().clear();
        return redirect(routes.Public.loginForm());
    }

    public Result profile() {
        WatUser user = currentUser();
        List<Rating> userRatings = Rating.userRatings(user.id);
        return ok(profile.render(user,userRatings));
    }

//    user for helping views to get username
    public static WatUser currentUser() {
        return WatUser.findUserByUsername(ctx().session().get("username"));
    }


}
