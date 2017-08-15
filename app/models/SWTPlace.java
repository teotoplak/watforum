package models;

import com.avaje.ebean.Model;
import controllers.SWTGooglePlace;
import play.data.FormFactory;

import javax.inject.Inject;
import javax.persistence.*;
import java.util.Set;

/**
 * Created by TeoLenovo on 4/6/2017.
 */
@Entity
public class SWTPlace extends Model {

    public SWTPlace(String googleId) {
        this.googleId = googleId;
    }

    @Id
    public Long id;

    @Column(unique = true)
    public String googleId;

    public SWTGooglePlace getGooglePlace() throws IllegalArgumentException {
        SWTGooglePlace place = SWTGooglePlace.getSWTGooglePlaceById(googleId);
        if (place == null) {
            throw new IllegalArgumentException("Something went wrong while getting google place" +
                    "object for id: " + googleId);
        }
        return place;
    }

    @OneToMany(mappedBy = "swtPlace")
    public Set<SWTRating> ratings;

    //DAO
    public static Finder<Long, SWTPlace> find = new Finder<>(SWTPlace.class);
    public static SWTPlace findPlaceByGoogleId(String gid) {
        return find.where().eq("googleId", gid).findUnique();
    }

}
