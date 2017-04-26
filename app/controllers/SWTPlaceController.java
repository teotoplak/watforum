package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.SWTPlace;
import models.WatPlace;
import play.Logger;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by TeoLenovo on 4/13/2017.
 */
public class SWTPlaceController extends Controller {


    private static final Logger.ALogger logger = Logger.of(SWTPlaceController.class);


    public Result places(String ids) {
        String[] idsArray = ids.split(",");
        List<SWTGooglePlace> places = new LinkedList<>();
        for (String googleId : Arrays.asList(idsArray)) {
            try {
                places.add(new SWTGooglePlace(googleId));
            } catch (IllegalArgumentException ex) {
                flash("error", "Internal error occurred");
                logger.error("Error while fetching json for google place");
                return redirect(routes.SWTPlaceController.searchBox());
            }
        }
        return ok(views.html.swtPlaces.render(places));
    }

    public Result place(String id) {

        SWTPlace place = SWTPlace.findPlaceByGoogleId(id);
        if (place == null) {
             place = new SWTPlace(id);
        }
        SWTGooglePlace gplace;
        try {
            gplace = place.getGooglePlace();
        } catch (IllegalArgumentException ex) {
            flash("error", "Internal error occurred");
            logger.error("Error while fetching json for google place");
            return redirect(routes.SWTPlaceController.searchBox());
        }
        return ok(views.html.swtPlace.render(place,gplace));
    }

    public Result searchBox() {
        return ok(views.html.search.render());
    }



}
