package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Rating;
import models.User;
import models.WatPlace;
import play.data.Form;
import play.data.FormFactory;
import play.libs.ws.*;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;

import javax.inject.Inject;

/**
 * Created by teo on 11/30/16.
 */

@Security.Authenticated(Secured.class)
public class Watplaces extends Controller {

    @Inject
    WSClient ws;

    @Inject
    private FormFactory formFactory;

    private static final String googleAPIkey = "AIzaSyBOVsLLDx5MQmY4CUaD9-kt5Dqw5tPjJV4";

    public Result searchBox() {
        return ok(search.render());
    }

//    public Result watPlace() {
//        JsonNode json = request().body().asJson();
//        if(json == null) {
//            return badRequest("Expecting Json data");
//        } else {
//            String name = json.findPath("name").textValue();
//            if(name == null) {
//                return badRequest("Missing parameter [name]");
//            } else {
//                WatPlace place = new WatPlace(json);
//                return ok(watplace.render(place));
//            }
//        }
//    }

    public Result watPlace(String id) throws Exception {


        String url = getUrl(id);
        JsonNode json = ws.url(url).get().thenApply(WSResponse::asJson).toCompletableFuture().get();

        WatPlace place = new WatPlace(json);
        if (place.name == null || place.name.isEmpty()) {
            flash("error", "Bad request was made - place doesn't exist! ");
            return redirect(routes.Watplaces.searchBox());
        }
        if (WatPlace.findWatPlaceByGoogleId(place.googleID) == null) {
            place.save();
        }
        //refreshing to get id
        place = WatPlace.findWatPlaceByGoogleId(place.googleID);
        User user = Users.currentUser();
        Integer rating = Rating.findRating(user, place);
        Integer overAllRating = Rating.findAverageRatingForPlace(place.id);
        return ok(watplace.render(place,rating,overAllRating));

    }

    private String getUrl(String id) {
        return "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + id  + "&key=" + googleAPIkey;
    }

    public Result rate() {

        Form<RatingForm> ratingFormForm = formFactory.form(RatingForm.class);
        RatingForm ratingForm = ratingFormForm.bindFromRequest().get();
        User user = User.findUserById(ratingForm.user_id);
        WatPlace watPlace = WatPlace.findWatPlaceById(ratingForm.watPlace_id);

        Rating rating = new Rating(user, watPlace, ratingForm.ratingInput);
        user.ratings.add(rating);
        watPlace.ratings.add(rating);

        user.save();
        watPlace.save();
        if (Rating.findRating(user, watPlace) != -1) {
            rating.update();
        } else {
            rating.save();
        }
        return redirect(routes.Ratings.listAll());

    }




}
