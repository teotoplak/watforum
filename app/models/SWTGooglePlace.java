package models;

import com.fasterxml.jackson.databind.JsonNode;
import play.Play;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Controller;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.Optional;

/**
 * Created by TeoLenovo on 4/13/2017.
 * Class representing Google place
 * Mostly used as a swtPlace class wrapper for view
 */
public class SWTGooglePlace extends Controller{

    @Inject
    private static WSClient ws;

    public Long id;

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

    public String state;
    public String county;
    public String city;


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

            Iterator<JsonNode> address_components = node.findPath("address_components").elements();
        while (address_components.hasNext()) {
            JsonNode component = address_components.next();
            String typesString = component.findPath("types").toString();
            if (typesString.contains("administrative_area_level_1")) {
                this.state = component.findPath("long_name").asText();
            }
            if (typesString.contains("administrative_area_level_2")) {
                this.county = component.findPath("long_name").asText();
            }
            if (typesString.contains("locality")) {
                this.city = component.findPath("long_name").asText();
            }
        }


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

    @Override
    public String toString() {
        return "SWTGooglePlace{" +
                "id=" + id +
                ", googleID='" + googleID + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", website='" + website + '\'' +
                ", googleMaps='" + googleMaps + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", state='" + state + '\'' +
                ", county='" + county + '\'' +
                ", city='" + city + '\'' +
                '}';
    }

    public boolean isInUSA() {
        return this.address.contains("United States") || this.address.contains("USA");
    }
}
