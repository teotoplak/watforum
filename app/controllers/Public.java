package controllers;

import org.pac4j.play.java.Secure;
import org.slf4j.Logger;
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

    private Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

    @Inject
    private FormFactory formFactory;

    public Result landing() {
        return ok(landing.render());
    }

    public Result termsAndConditions() {
        return ok(terms.render());
    }


//    @Secure(clients = "FacebookClient", authorizers = "custom")
//    public Result login() {
//        flash("logged in!!");
//        return redirect(request().getHeader("referer"));
//    }
//
//    private Result loginError(Form<User> boundForm) {
//        flash("error", "Incorrect login!");
//        return badRequest(views.html.login.render(boundForm));
//    }




}
