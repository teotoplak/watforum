package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by teo on 12/5/16.
 */
@Entity
public class WatPlace extends Model{

    public WatPlace(JsonNode json) {
        this.googleID = json.findPath("place_id").textValue();
        this.name = json.findPath("name").textValue();
        this.phoneNumber = json.findPath("international_phone_number").textValue();
        this.address = json.findPath("formatted_address").textValue();
    }

    @Id
    public String googleID;

    public String name;

    public String phoneNumber;

    public String address;

}
