package controllers;

import models.SWTPlace;
import models.SWTUser;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

/**
 * Created by TeoLenovo on 4/17/2017.
 */
public class SWTRatingController extends Controller {

    private static final Logger.ALogger logger = Logger.of(SWTRatingController.class);


    public Result rate() {

        DynamicForm form = Form.form().bindFromRequest();
        return ok(form.get("rating"));
    }

    public Result ratingForm(String id) {

        SWTPlace place = new SWTPlace(id);
        SWTGooglePlace gplace;
        try {
            gplace = place.getGooglePlace();
        } catch (IllegalArgumentException ex) {
            flash("error", "Internal error occurred");
            logger.error("Error while fetching json for google place");
            return redirect(routes.SWTPlaceController.searchBox());
        }

        SWTUser user = SWTUserController.currentUser();
        return ok(views.html.rate.render(place,gplace,user.swtYears));
    }

}
