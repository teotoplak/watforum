package controllers;

import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by TeoLenovo on 4/17/2017.
 */
public class SWTRatingController extends Controller {

    public Result rate() {

        DynamicForm form = Form.form().bindFromRequest();
        return ok(form.get("rating"));
    }

}
