package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import jdk.nashorn.internal.scripts.JO;
import models.SWTGooglePlace;
import models.SWTPlace;
import models.SWTRating;
import models.SearchResult;
import play.Configuration;
import play.Logger;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import security.Secured;

import javax.inject.Inject;
import java.util.*;

/**
 * Created by TeoLenovo on 4/13/2017.
 */
public class SWTPlaceController extends Controller {


    private static final Logger.ALogger logger = Logger.of(SWTPlaceController.class);
    private @Inject WSClient ws;

    @Inject
    private FormFactory formFactory;

    @Inject
    private Configuration configuration;

    @Security.Authenticated(Secured.class)
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
            return redirect(routes.Public.landing());
        }
        place.calculateRating();
        return ok(views.html.swtPlace.render(place,gplace));
    }

    @Security.Authenticated(Secured.class)
    public Result findSearch() {
        String googleAPIkey = configuration.getString("googleAPIkey");
        String call = "https://maps.googleapis.com/maps/api/js?key="+googleAPIkey+"&libraries=places&callback=initMap";
        return ok(views.html.findSearch.render(call));
    }

    /**
     * Used for ajax calls from search box
     * Asking for all places in given bounds
     */
    public Result getSWTPlaces() {
        DynamicForm form = formFactory.form().bindFromRequest();
        Double lngFrom = Double.parseDouble(form.get("lngFrom"));
        Double lngTo = Double.parseDouble(form.get("lngTo"));
        Double latFrom = Double.parseDouble(form.get("latFrom"));
        Double latTo = Double.parseDouble(form.get("latTo"));
        List<SWTPlace> placesList =  new LinkedList<>();
        placesList.addAll(SWTPlace.findPlaceByViewPort(lngFrom, lngTo, latFrom, latTo));
        List<SearchResult> result = new LinkedList<>();
        for (SWTPlace place : placesList) {
            place.calculateRating();
            result.add(new SearchResult(place));
        }
        return ok(Json.toJson(result));
    }

    // used by ajax from search
    public Result returnAllPlaces() {
        List<SWTPlace> allPlaces = SWTPlace.findAll();
        List<SearchResult> result = new LinkedList<>();
        for(SWTPlace place : allPlaces) {
            place.calculateRating();
            result.add(new SearchResult(place));
        }
        return ok(Json.toJson(result));
    }


    /* ajax requests from autocomplete */
    public Result searchForAutocomplete() {
        List<SearchResult> results = new LinkedList<>();
        JsonNode json = request().body().asJson();
        // if autocomplete dropdown wasn't actually selected
        if(!json.has("place_id")) {
              return noContent();
        }
        SWTGooglePlace gplace = new SWTGooglePlace(json);
        if(gplace.isRegion) {
            results.add(new SearchResult(gplace));
            return ok(Json.toJson(results));
        }
        // if place is rated
        SWTPlace swtPlace = SWTPlace.findPlaceByGoogleId(gplace.googleID);
        if(swtPlace != null) {
            swtPlace.calculateRating();
            results.add(new SearchResult(swtPlace));
        } else {
            results.add(new SearchResult(gplace));
        }
        return ok(Json.toJson(results));
    }

    /* ajax requests */
    public Result searchFor(String text) {
        Optional<JsonNode> jsonResponse = makeGooglePlacesRequest(text);
        List<SearchResult> results = new LinkedList<>();
        if (jsonResponse.isPresent()) {
            JsonNode rootJson = jsonResponse.get();
            JsonNode resultsNode = rootJson.path("results");
            for (JsonNode googlePlaceNode : resultsNode) {
                SWTGooglePlace gplace = new SWTGooglePlace(googlePlaceNode);
                // must be in USA
                if(gplace.isInUSA()) {
                    // if place is region
                    if(gplace.isRegion) {
                        results.add(new SearchResult(gplace));
                        break;
                    }
                    // if place is rated
                    SWTPlace swtPlace = SWTPlace.findPlaceByGoogleId(gplace.googleID);
                    if(swtPlace != null) {
                        swtPlace.calculateRating();
                        results.add(new SearchResult(swtPlace));
                    } else {
                        results.add(new SearchResult(gplace));
                    }
                }
            }
            return ok(Json.toJson(results));
        } else {
            logger.error("Error occurred while doing google API request");
            return internalServerError("Internal error occurred");
        }
    }

    private Optional<JsonNode> makeGooglePlacesRequest(String searchText) {
        String formattedText = searchText.replaceAll("\\s+", "+");
        String key = configuration.getString("googleAPIkey");
        String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query="
                + formattedText
                + "&key="
                + key;
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
