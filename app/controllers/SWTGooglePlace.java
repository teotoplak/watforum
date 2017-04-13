package controllers;

import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by TeoLenovo on 4/13/2017.
 */
public class SWTGooglePlace {

    public SWTGooglePlace(JsonNode json) throws NullPointerException {
        if (json == null) {
            throw new NullPointerException("Receieved null as argument");
        }
        this.googleID = json.findPath("place_id").textValue();
        this.name = json.findPath("name").textValue();
        this.phoneNumber = json.findPath("international_phone_number").textValue();
        this.address = json.findPath("formatted_address").textValue();
        this.icon = json.findPath("icon").textValue();
        this.weekdayText = json.findPath("weekday_text").textValue();
        this.website = json.findPath("website").textValue();
        this.googleMaps = json.findPath("url").textValue();
    }

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

}
