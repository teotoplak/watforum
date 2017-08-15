package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.Play;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Controller;

import javax.inject.Inject;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.sound.sampled.Control;
import java.util.Optional;

/**
 * Created by TeoLenovo on 4/13/2017.
 * Class representing Google place
 */
public class SWTGooglePlace extends Controller{

    @Inject
    private static WSClient ws;

    public Long id;

    public String googleID;

    public String name;

    public String phoneNumber;

    public String address;

    public String icon;

    /*
    text about working hours
     */
    public String weekdayText;

    public String website;

    /*
    link to google maps place
     */
    public String googleMaps;

    public Double lat;

    public Double lng;


    /**
     *
     * @param node google place json
     */
    public SWTGooglePlace(JsonNode node) {

            this.googleID = node.findPath("place_id").textValue();
            this.name = node.findPath("name").textValue();
            this.phoneNumber = node.findPath("international_phone_number").textValue();
            this.address = node.findPath("formatted_address").textValue();
            this.icon = node.findPath("icon").textValue();
            this.weekdayText = node.findPath("weekday_text").textValue();
            this.website = node.findPath("website").textValue();
            this.googleMaps = node.findPath("url").textValue();

            //location
            JsonNode aField = node.findPath("location");
            JsonNode bField = aField.findPath("lat");
            JsonNode cField = aField.findPath("lng");
            lat = Double.parseDouble(bField.toString());
            lng = Double.parseDouble(cField.toString());
    }

    private Optional<JsonNode> getJsonWithId(String id) {
        String url = getGoogleAskForPlaceUrl(id);
        try {
            return Optional.of(ws.url(url).get().thenApply(WSResponse::asJson).toCompletableFuture().get());
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    /**
     * Makes http call to get google place object.
     * Returns null if something went wrong.
     */
    public static SWTGooglePlace getSWTGooglePlaceById(String id) {
        if (ws == null) {
            ws  = play.api.Play.current().injector().instanceOf(WSClient.class);
        }
        String url = getGoogleAskForPlaceUrl(id);
        try {
            return new SWTGooglePlace(ws.url(url).get().thenApply(WSResponse::asJson).toCompletableFuture().get());
        } catch (Exception ex) {
            return null;
        }
    }

    private static String getGoogleAskForPlaceUrl(String id) {
        return "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + id + "&key="
                + Play.application().configuration().getString("googleAPIkey");
    }

}
