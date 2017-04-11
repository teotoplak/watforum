package models;

import com.avaje.ebean.Model;
import models.enumerations.SWTNationality;
import play.data.validation.Constraints;
import play.libs.F;

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
    @GeneratedValue(strategy = GenerationType.TABLE)
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
    public Date birth;


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


    //DAO

    public static Finder<Long, SWTUser> find = new Finder<>(SWTUser.class);
    public static List<SWTUser> findAll() {
        return find.all();
    }
    public static SWTUser checkUser(String username, String password) {
        return find.where().eq("username", username).and().eq("password", password).findUnique();
    }
    public static SWTUser findUserByUsername(String username) {
        return find.where().eq("username", username).findUnique();
    }

    public static SWTUser findUserById(Long id) {
        return find.where().eq("id", id).findUnique();
    }

    @Override
    public String toString() {
        return username;
    }

    public static class UsernameValidator extends Constraints.Validator<String> {
        @Override
        public boolean isValid(String username) {
            String pattern="^[a-zA-Z0-9._-]{3,}$";
            return username != null && username.matches(pattern);
        }

        @Override
        public F.Tuple<String, Object[]> getErrorMessageKey() {
            return new F.Tuple<String,Object[]>("error.invalid.username", new Object[]{});
        }
    }


}
