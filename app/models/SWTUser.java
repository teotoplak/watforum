package models;

import com.avaje.ebean.Model;
import models.enumerations.SWTNationality;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by TeoLenovo on 4/6/2017.
 */
@Entity
public class SWTUser extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long id;

    @Constraints.Required
    @Column(unique=true)
    @Constraints.ValidateWith(value= WatUser.UsernameValidator.class,message = "Username must contain " +
            "at least 3 characters. All letters, numbers, points, dashes and underscores allowed.")
    public String username;

    @Constraints.Required
    public String password;


    public String firstName;
    public String lastName;
    public Image profilePicture;
    public String contact;

    @Column(unique = true)
    @Constraints.Email
    public String email;

    @OneToMany(mappedBy = "user")
    public Set<SWTYear> swtYears;

    //for auth
    public List<String> loginProfiles;
//    public SWTNationality nationality;
    //should be other then string
    public String livingLocation;

    //should I have this? get them over swtYears
    public Set<SWTWorkRating> ratings;

}
