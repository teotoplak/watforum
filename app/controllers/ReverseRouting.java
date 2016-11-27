package controllers;

import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by teo on 11/23/16.
 */
public class ReverseRouting extends Controller{

    public Result hello(String name) {
        return ok("Hello " + name);
    }

    public Result reverseHello() {
        return redirect(controllers.routes.ReverseRouting.hello("Bob"));
    }

}
