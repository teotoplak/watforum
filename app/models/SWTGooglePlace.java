package models;

import com.fasterxml.jackson.databind.JsonNode;
import play.Play;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Controller;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Created by TeoLenovo on 4/13/2017.
 * Class representing Google place
 * Mostly used as a swtPlace class wrapper for view
 */
public class SWTGooglePlace extends Controller{

    @Inject
    private static WSClient ws;

    public String googleID;

    public String name;

    public String phoneNumber;

    public String address;

    public String website;

    /*
    link to google maps place
     */
    public String googleMaps;

    public Double lat;

    public Double lng;

    // if google place is region (area)
    public boolean isRegion;

    public Double NElat;
    public Double NElng;
    public Double SWlat;
    public Double SWlng;


    /**
     *
     * @param node google place json
     */
    public SWTGooglePlace(JsonNode node) {

            this.googleID = node.findPath("place_id").textValue();
            this.name = node.findPath("name").textValue();
            this.phoneNumber = node.findPath("international_phone_number").textValue();
            this.address = node.findPath("formatted_address").textValue();
            this.website = node.findPath("website").textValue();
            this.googleMaps = node.findPath("url").textValue();

            this.isRegion = checkIfRegion(node.path("types").toString());

            // location
            JsonNode aField = node.findPath("location");
            JsonNode bField = aField.findPath("lat");
            JsonNode cField = aField.findPath("lng");
            lat = Double.parseDouble(bField.toString());
            lng = Double.parseDouble(cField.toString());

            // viewport
            JsonNode viewport = node.findPath("viewport");

            // two possible formats of json (one from text search other from autocomplete)
            // text search parse
            try {
                JsonNode northeast = viewport.findPath("northeast");
                JsonNode southwest = viewport.findPath("southwest");
                NElat = Double.parseDouble(northeast.findPath("lat").toString());
                NElng = Double.parseDouble(northeast.findPath("lng").toString());
                SWlat = Double.parseDouble(southwest.findPath("lat").toString());
                SWlng = Double.parseDouble(southwest.findPath("lng").toString());
            } catch (NumberFormatException ex) {
                // autocomplete parse
                NElat = Double.parseDouble(viewport.findPath("north").toString());
                NElng = Double.parseDouble(viewport.findPath("east").toString());
                SWlat = Double.parseDouble(viewport.findPath("south").toString());
                SWlng = Double.parseDouble(viewport.findPath("west").toString());
            }


    }

    private boolean checkIfRegion(String typesString) {
        String[] regionKeywords = {
                "locality",
                "sublocality",
                "postal_code",
                "country",
                "administrative_area_level_1",
                "administrative_area_level_2",
                "administrative_area_level_3",
                "administrative_area_level_4",
                "administrative_area_level_5",
                "neighborhood",
                "street_address",
                "street_number",
                "route",
                "postal_code",
                "postal_town",
                "intersection",
                "colloquial_area",
        };
        boolean isRegion = false;
        for(int index=0; index<regionKeywords.length; index++) {
            if(typesString.contains(regionKeywords[index])) {
                isRegion = true;
                break;
            }
        }
        return isRegion;
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
            // could be google API error limit exceeded
            // try one more time
            try {
                Thread.sleep(2500);
                return new SWTGooglePlace(ws.url(url).get().thenApply(WSResponse::asJson).toCompletableFuture().get());
            } catch (InterruptedException|ExecutionException e) {
                e.printStackTrace();
            }
            ex.printStackTrace();
            return null;
        }
    }

    private static String getGoogleAskForPlaceUrl(String id) {
        return "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + id + "&key="
                + Play.application().configuration().getString("googleAPIkey");
    }

    @Override
    public String toString() {
        return "SWTGooglePlace{" +
                "googleID='" + googleID + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", website='" + website + '\'' +
                ", googleMaps='" + googleMaps + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", isRegion=" + isRegion +
                ", NElat=" + NElat +
                ", NElng=" + NElng +
                ", SWlat=" + SWlat +
                ", SWlng=" + SWlng +
                '}';
    }

    public boolean isInUSA() {
        return this.address.contains("United States") || this.address.contains("USA");
    }
}
