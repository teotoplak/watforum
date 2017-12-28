package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.pac4j.play.java.Secure;
import org.slf4j.Logger;
import play.Configuration;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
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

    private @Inject WSClient ws;

    @Inject
    private Configuration configuration;

    @Inject
    private FormFactory formFactory;

    @Inject
    MailerClient mailerClient;

    public Result landing() {
        return ok(landing.render());
    }

    public Result termsAndConditions() {
        return ok(terms.render());
    }

    public Result privacyPolicy() {
        return ok(privacy.render());
    }

    public Result aboutUs() {
        return ok(about.render());
    }

    public Result contact() {
        return ok(contact.render());
    }

    public Result sendEmail() {
        DynamicForm form = Form.form().bindFromRequest();
        String name = form.get("name");
        String email = form.get("email");
        String message = form.get("message");
        String recaptchaResponse = form.get("g-recaptcha-response");
        if (!recaptchaSaysOk(recaptchaResponse, "")) {
            flash("error", "Recaptcha rejected you!");
            return redirect(routes.Public.contact());
        }
        Email simpleEmail = new Email()
                .setSubject("WATpointer " + email + " " + name)
                .setFrom("<watpointer@yandex.com>")
                .addTo("<teotoplak95@gmail.com>")
                .setBodyText(message);
        mailerClient.send(simpleEmail);
        flash("success", "Message sent!");
        return redirect(routes.Public.contact());
    }

    private boolean recaptchaSaysOk(String recaptchaFormResponse, String userIP) {

        if (ws == null) {
            ws  = play.api.Play.current().injector().instanceOf(WSClient.class);
        }
        String url = "https://www.google.com/recaptcha/api/siteverify";
        String secret =  configuration.getString("recaptcha");
        String parameters = "secret="+secret+"&response="+recaptchaFormResponse;
        try {
            JsonNode response = ws.url(url).setContentType("application/x-www-form-urlencoded")
                    .post(parameters).thenApply(WSResponse::asJson).toCompletableFuture().get();
            return response.path("success").asText().equals("true");
        } catch (Exception ex) {
            logger.error("Recaptcha request failed: " + ex.toString());
            ex.printStackTrace();
            return false;
        }
    }
//    }




}
