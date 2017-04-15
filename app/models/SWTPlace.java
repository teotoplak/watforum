package models;

import com.avaje.ebean.Model;
import controllers.SWTGooglePlace;

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

    public String googleId;

    public SWTGooglePlace getGooglePlace() throws IllegalArgumentException {
        return new SWTGooglePlace(googleId);
    }


    @OneToMany(mappedBy = "swtPlace")
    public Set<SWTRating> ratings;

}
