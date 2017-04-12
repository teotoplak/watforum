package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Rating;
import models.SWTUser;
import models.SWTYear;
import models.WatUser;
import play.api.cache.Cache;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.login;
import views.html.register;
import views.html.*;
import play.cache.*;

import javax.inject.Inject;
import javax.sound.sampled.Control;

import java.util.List;
import java.util.Map;

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

    public Result register() {
        Form<SWTUser> boundForm = formFactory.form(SWTUser.class).bindFromRequest();
        if (boundForm == null) {
            return null;
        }
        if(boundForm.hasErrors()){
            flash("error", "Incorrect register!");
            return badRequest(register.render(boundForm));
        }
        SWTUser user = boundForm.get();

        if(user.id == null) {
            user.save();
        } else {
            user.update();
        }

        cache.set("userId", user.id, 30);
        return redirect(routes.SWTYearController.addSWTYearsForm());
    }

    public Result listAllUsers() {
        List<SWTUser> list = SWTUser.findAll();
        return ok(listing.render(list));
    }

    public Result registerForm() {
        return ok(register.render(formFactory.form(SWTUser.class).bindFromRequest()));
    }






    public Result loginForm() {
        return ok(login.render(formFactory.form(SWTUser.class)));
    }


    public Result notAuthLoginForm() {
        flash("error", "Login first to access that page!");
        return redirect(routes.SWTUserController.loginForm());
    }

    public Result login() {
        Form<SWTUser> boundForm = formFactory.form(SWTUser.class).bindFromRequest();
        SWTUser enteredUser;
        try {
            enteredUser = boundForm.get();
        } catch (IllegalStateException ex) {
            return loginError(boundForm);
        }
        SWTUser user = SWTUser.checkUser(enteredUser.username, enteredUser.password);
        if (user == null) {
            return loginError(boundForm);
        }

        session().clear();
        session("username", user.username);
        return redirect(routes.Public.landing());
    }

    private Result loginError(Form<SWTUser> boundForm) {
        flash("error", "Incorrect login!");
        return badRequest(views.html.login.render(boundForm));
    }

    public Result logout() {
        session().clear();
        return redirect(routes.SWTUserController.loginForm());
    }

    public static SWTUser currentUser() {
        return SWTUser.findUserByUsername(ctx().session().get("username"));
    }

    @Security.Authenticated(Secured.class)
    public Result profile() {
        SWTUser user = currentUser();
        List<Rating> userRatings = Rating.userRatings(user.id);
        return ok(profile.render(user,userRatings));
    }

}
