package controllers;

import models.User;
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
        List<User> list = User.findAll();
        return ok(listing.render(list));
    }

    public Result logout() {
        session().clear();
        return redirect(routes.Public.loginForm());
    }

    public static String currentUser() {
        return ctx().session().get("username");
    }

}
