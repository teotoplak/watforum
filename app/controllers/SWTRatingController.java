package controllers;

import models.SWTPlace;
import models.SWTRating;
import models.SWTUser;
import models.SWTYear;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import scala.Int;
import views.html.*;

/**
 * Created by TeoLenovo on 4/17/2017.
 */
public class SWTRatingController extends Controller {

    private static final Logger.ALogger logger = Logger.of(SWTRatingController.class);


    public Result rate(String id) {
        SWTUser user = SWTUserController.currentUser();
        SWTPlace place = SWTPlace.findPlaceByGoogleId(id);
        if (place == null) {
            place = new SWTPlace(id);
            place.save();
        }
        DynamicForm form = Form.form().bindFromRequest();
        Integer rating = Integer.parseInt(form.get("rating"));
        String position = form.get("position");
        String comment = form.get("comment");
        Integer workload = Integer.parseInt(form.get("workload"));
        Integer payment = Integer.parseInt(form.get("payment"));
        boolean providingHousing = form.get("providingHousing")==null ? false : true;
        SWTYear swtYear = user.findYearByYearNumber(Integer.parseInt(form.get("swtYear")));
        SWTRating swtRating = new SWTRating(place, rating, comment, providingHousing, workload, payment, position, swtYear);
        swtRating.save();
        flash("success", "Rated!");
        return redirect(routes.SWTPlaceController.place(id));
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
