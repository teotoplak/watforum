package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;
import org.omg.CORBA.INTERNAL;
import play.data.FormFactory;

import javax.inject.Inject;
import javax.persistence.*;
import java.awt.image.RescaleOp;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
        this.icon = json.findPath("icon").textValue();
        this.weekdayText = json.findPath("weekday_text").textValue();
        this.website = json.findPath("website").textValue();
        this.googleMaps = json.findPath("url").textValue();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long id;

    @Column(unique=true)
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

    @OneToMany(mappedBy = "watPlace")
    public Set<Rating> ratings = new HashSet<>();

    public static Finder<Long, WatPlace> find = new Finder<>(WatPlace.class);

    public static WatPlace findWatPlaceByGoogleId(String id) {
        return find.where().eq("googleID", id).findUnique();
    }

    public static WatPlace findWatPlaceById(Long id) {
        return find.where().eq("id", id).findUnique();
    }


}
