package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.WatPlace;
import play.Logger;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by TeoLenovo on 4/13/2017.
 */
public class SWTPlaceController extends Controller {

    @Inject
    WSClient ws;

    private static final Logger.ALogger logger = Logger.of(SWTPlaceController.class);

    private static final String googleAPIkey = "AIzaSyBOVsLLDx5MQmY4CUaD9-kt5Dqw5tPjJV4";

    public Result places(String ids) {
        String[] idsArray = ids.split(",");
        List<SWTGooglePlace> places = new LinkedList<>();
        for (String googleId : Arrays.asList(idsArray)) {
            String url = getGoogleAskForPlaceUrl(googleId);
            try {
                JsonNode json = ws.url(url).get().thenApply(WSResponse::asJson).toCompletableFuture().get();
                places.add(new SWTGooglePlace(json));
            } catch (Exception ex) {
                flash("error", "Internal error occurred");
                logger.error("Error while fetching json for google place");
                return redirect(routes.SWTPlaceController.searchBox());
            }
        }

        return ok(views.html.swtPlaces.render(places));
    }
    public Result searchBox() {
        return ok(views.html.search.render());
    }


    private String getGoogleAskForPlaceUrl(String id) {
        return "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + id + "&key=" + googleAPIkey;
    }
}
