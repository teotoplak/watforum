package models;

import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by TeoLenovo on 4/6/2017.
 */
public class SWTWork extends SWTPlace {

    public Long id;

    public String googleId;

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

    public Set<SWTWorkRating> ratings;

}
