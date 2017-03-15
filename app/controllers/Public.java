package controllers;

import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import javax.inject.Inject;

/**
 * Pages visible to all users
 * Created by teo on 11/28/16.
 */
public class Public extends Controller {

    @Inject
    private FormFactory formFactory;

    public Result loginForm() {
        return ok(login.render(formFactory.form(User.class)));
    }

    public Result landing() {
        return ok(landing.render());
    }

    public Result register() {
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
        flash("success", String.format("Hello %s! Please login to your new account.", user));
        return redirect(routes.Public.loginForm());
    }

    public Result notAuthLoginForm() {
        flash("error", "Login first to access that page!");
        return redirect(routes.Public.loginForm());
    }

    public Result login() {
        Form<User> boundForm = formFactory.form(User.class).bindFromRequest();
        User enteredUser;
        try {
            enteredUser = boundForm.get();
        } catch (IllegalStateException ex) {
            return loginError(boundForm);
        }
        User user = User.checkUser(enteredUser.username, enteredUser.password);
        if (user == null) {
            return loginError(boundForm);
        }

        session().clear();
        session("username", user.username);
        return redirect(routes.Public.landing());
    }

    private Result loginError(Form<User> boundForm) {
        flash("error", "Incorrect login!");
        return badRequest(views.html.login.render(boundForm));
    }


    public Result registerForm() {
        return ok(register.render(formFactory.form(User.class).bindFromRequest()));
    }


}
