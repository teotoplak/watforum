package controllers;

import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import javax.inject.Inject;
import java.util.List;


/**
 * Created by teo on 11/27/16.
 */
public class Users extends Controller {

    @Inject
    private FormFactory formFactory;

    public Result listAllUsers() {
        List<User> list = User.findAll();
        return ok(listing.render(list));
    }

    public Result register() {
        return ok(register.render(formFactory.form(User.class).bindFromRequest()));
    }

    public Result save() {
        Form<User> boundForm = formFactory.form(User.class).bindFromRequest();
        if (boundForm == null) {
            return null;
        }
        if(boundForm.hasErrors()){
            flash("error", "Incorrect register!");
            return badRequest(register.render(boundForm));
        }
        User user = boundForm.get();

        if(user.id == null) {
            user.save();
        } else {
            user.update();
        }

        flash("success", String.format("Successfully added user: %s", user));
        return redirect(routes.Users.listAllUsers());
    }

}
