package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

/**
 * Created by teo on 11/30/16.
 */
public class Search extends Controller {


    public Result searchBox() {
        return ok(search.render());
    }

    public Result watPlace() {
        JsonNode json = request().body().asJson();
        if(json == null) {
            return badRequest("Expecting Json data");
        } else {
            String name = json.findPath("name").textValue();
            if(name == null) {
                return badRequest("Missing parameter [name]");
            } else {
                return redirect(routes.Public.loginForm());
            }
        }
    }

}
