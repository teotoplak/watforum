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
import sun.reflect.annotation.ExceptionProxy;
import views.html.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

        //parameters
        DynamicForm form = Form.form().bindFromRequest();
        String previousUrl = form.get("previousUrl");
        Integer rating = Integer.parseInt(form.get("rating"));
        String position = form.get("position");
        String comment = form.get("comment");
        Integer workload = Integer.parseInt(form.get("workload"));
        Integer payment = Integer.parseInt(form.get("payment"));
        boolean providingHousing = form.get("providingHousing")==null ? false : true;
        boolean providingMeal = form.get("providingMeal")==null ? false : true;
        SWTYear swtYear = user.findYearByYearNumber(Integer.parseInt(form.get("swtYear")));
        String existingRatingIdString = form.get("existingRatingId");

        //determine if delete
        String[] postAction = request().body().asFormUrlEncoded().get("action");
        String action = postAction[0];
        if (action.equals("delete")) {
            return deleteRating(existingRatingIdString, previousUrl);
        }

        SWTRating swtRating;
        if(existingRatingIdString==null) {
            swtRating = new SWTRating();
        } else {
            Long existingRatingId = Long.parseLong(existingRatingIdString);
            swtRating = SWTRating.findRatingById(existingRatingId);
        }
        swtRating.swtPlace = place;
        swtRating.rating = rating;
        swtRating.comment = comment;
        swtRating.providingHousing = providingHousing;
        swtRating.providingMeal = providingMeal;
        swtRating.workLoad = workload;
        swtRating.payment = payment;
        swtRating.workPosition = position;
        swtRating.swtYear = swtYear;
        swtRating.save();
        flash("success", "Rated!");
        return redirect(previousUrl);
    }


    public Result ratingForm(String placeId, String ratingIdString) {
        SWTUser user = SWTUserController.currentUser();

        if (user == null) {
            flash("info", "Login first to rate the place!");
            return redirect(routes.SWTUserController.loginForm());
        }

        //check if user created some swt year
        if (user.swtYears.isEmpty()) {
            flash("error","You have to add some SWT experience to rate places!");
            return redirect(routes.SWTUserController.placesPanel());
        }
        SWTPlace place = new SWTPlace(placeId);
        SWTGooglePlace gplace;
        try {
            gplace = place.getGooglePlace();
        } catch (IllegalArgumentException ex) {
            flash("error", "Internal error occurred");
            logger.error("Error while fetching json for google place");
            return redirect(routes.SWTPlaceController.searchBox());
        }
        SWTRating rating;
        try {
            Long ratingId = Long.parseLong(ratingIdString);
            rating = SWTRating.findRatingById(ratingId);
        } catch (Exception ignorable) {
            rating = null;
        }

        //handling swt years list
        List<SWTYear> swtYears = new ArrayList<>();
        swtYears = user.swtYears;
        Set<SWTRating> ratings = user.getAllRatings();
        for (SWTRating currentRating : ratings) {
            if (swtYears.contains(currentRating.swtYear)
                    && currentRating.swtPlace.googleId.equals(placeId)) {
                swtYears.remove(currentRating.swtYear);
            }
        }
        if (rating != null) {
            swtYears.add(rating.swtYear);
        }
        if (swtYears.isEmpty()) {
            flash("warning", "You already rated this place with all available SWT years!");
            return redirect(routes.SWTPlaceController.place(placeId));
        }
        String previousUrl = request().getHeader("referer");

        return ok(views.html.rate.render(place,gplace,swtYears,rating,previousUrl));
    }

    private Result deleteRating(String ratingId, String previousUrl) {
        try {
            SWTRating rating = SWTRating.findRatingById(Long.parseLong(ratingId));
            SWTPlace place = rating.swtPlace;
            rating.delete();
            // also delete place if there is no more ratings
            if (place.ratings.size() == 0) {
                place.delete();
            }
            flash("success", "Rating deleted!");
        } catch (Exception ex) {
            flash("error", "Error deleting rating");
            logger.error("Error deleting rating");
        }
        return redirect(previousUrl);
    }

}
