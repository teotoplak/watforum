package controllers;

import akka.io.Inet;
import com.fasterxml.jackson.databind.JsonNode;
import models.SWTPlace;
import play.Logger;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.CompletionStage;

/**
 * Created by TeoLenovo on 4/13/2017.
 */
public class SWTPlaceController extends Controller {


    private static final Logger.ALogger logger = Logger.of(SWTPlaceController.class);
    @Inject WSClient ws;

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

    public Result searchFor(String text) {
        Optional<JsonNode> jsonResponse = makeGooglePlacesRequest(text);
        if (jsonResponse.isPresent()) {
            JsonNode rootJson = jsonResponse.get();
            JsonNode resultsNode = rootJson.path("results");
            if (resultsNode.size() > 1) {
                List<SWTGooglePlace> places = new LinkedList<>();
                for (JsonNode googlePlaceNode : resultsNode) {
                    places.add(new SWTGooglePlace(googlePlaceNode));
                }
                return ok(views.html.swtPlaces.render(places));
            } else if(resultsNode.size() == 1) {
                return redirect(routes.SWTPlaceController.place(resultsNode.findPath("place_id").textValue()));
            } else {
                flash("info", "No establishment found for that search!");
                return redirect(request().getHeader("referer"));
            }
        } else {
            return ok("error");
        }
    }

    private Optional<JsonNode> makeGooglePlacesRequest(String searchText) {
        String formattedText = searchText.replaceAll("\\s+", "+");
        String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query="
                + formattedText
                + "&key=AIzaSyBOVsLLDx5MQmY4CUaD9-kt5Dqw5tPjJV4&type=establishment";
        // make sure it is injected (problems in past)
        if (ws == null) {
            ws  = play.api.Play.current().injector().instanceOf(WSClient.class);
        }
        try {
            return Optional.of(ws.url(url).get().thenApply(WSResponse::asJson).toCompletableFuture().get());
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

}
