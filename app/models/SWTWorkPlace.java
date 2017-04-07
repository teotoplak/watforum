package models;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by TeoLenovo on 4/6/2017.
 */
@Entity
public class SWTWorkPlace extends SWTPlace {


    public String googleId;

    /*
    text about working hours
     */
    public String weekdayText;

    public String website;

    /*
    link to google maps place
     */
    public String googleMaps;

    @OneToMany(mappedBy = "swtWorkPlace")
    public Set<SWTWorkRating> ratings;

}
