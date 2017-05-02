package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;
import play.libs.F;

import javax.persistence.*;
import java.awt.*;
import java.net.URI;
import java.util.*;
import java.util.List;

/**
 * Created by TeoLenovo on 4/6/2017.
 */
@Entity
public class SWTUser extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Constraints.Required
    @Column(unique=true)
    @Constraints.ValidateWith(value= SWTUser.UsernameValidator.class,message = "Username must contain " +
            "at least 3 characters. All letters, numbers, points, dashes and underscores allowed.")
    public String username;

    public String password;


    public String firstName;
    public String lastName;
    public String profilePictureUrl = "http://localhost:9000/assets/images/profiles/user-default.png";
    public List<URI> contacts;
    public Date birth;
    public String gender;

    @Column(unique = true)
    @Constraints.Email
    public String email;

    @OneToMany(mappedBy = "user")
    public List<SWTYear> swtYears;

//    public SWTNationality nationality;
    //should be other then string
    public String livingLocation;

    //should I have this? get them over swtYears
    public Set<SWTRating> ratings;

    public List<SWTYear> getSortedYears() {
        List<SWTYear> list = this.swtYears;
        Collections.sort(list);
        return list;
    }

    //helper methods
    public SWTYear findYearByYearNumber(Integer yearNum) {
        for (SWTYear year : this.swtYears) {
            if (year.year.equals(yearNum)) {
                return year;
            }
        }
        return null;
    }

    //DAO
    public static Finder<Long, SWTUser> find = new Finder<>(SWTUser.class);
    public static List<SWTUser> findAll() {
        return find.all();
    }

    public static SWTUser verifyCredentials(String usernameOrEmail, String password) {
        return usernameOrEmail.contains("@")?
                find.where().eq("email", usernameOrEmail).and().eq("password", password).findUnique():
                find.where().eq("username", usernameOrEmail).and().eq("password", password).findUnique();
    }

    public static SWTUser findUserByUsername(String username) {
        return find.where().eq("username", username).findUnique();
    }

    public static SWTUser findUserByEmail(String email) {
        return find.where().eq("email", email).findUnique();
    }

    public static SWTUser findUserById(Long id) {
        return find.where().eq("id", id).findUnique();
    }


    public static class UsernameValidator extends Constraints.Validator<String> {
        @Override
        public boolean isValid(String username) {
            //characters, numbers underscore and hyphen allowed
            //min 3, max 15 characters
            String pattern="^[a-zA-Z0-9._-]{3,15}$";
            return username != null && username.matches(pattern);
        }

        @Override
        public F.Tuple<String, Object[]> getErrorMessageKey() {
            return new F.Tuple<String,Object[]>("error.invalid.username", new Object[]{});
        }
    }

    public SWTUser(String firstName, String lastName, String profilePictureUrl, URI contact, Date birth, String email, String gender, String livingLocation) {
        this.firstName = firstName;
        this.lastName = lastName;
        if (profilePictureUrl != null) {
        this.profilePictureUrl = profilePictureUrl;
        }
        List<URI> contacts = new LinkedList<>();
        contacts.add(contact);
        this.contacts = contacts;
        this.birth = birth;
        this.email = email;
        this.gender = gender;
        this.livingLocation = livingLocation;
    }

    public SWTUser(String username, String password, String firstName, String lastName, String profilePictureUrl,URI contact, Date birth, String gender, String email, String livingLocation) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        if (profilePictureUrl != null) {
        this.profilePictureUrl = profilePictureUrl;
        }
        List<URI> contacts = new LinkedList<>();
        contacts.add(contact);
        this.contacts = contacts;
        this.birth = birth;
        this.gender = gender;
        this.email = email;
        this.livingLocation = livingLocation;
    }

    @Override
    public String toString() {
        return "SWTUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", profilePicture=" + profilePictureUrl +
                ", contacts=" + contacts +
                ", birth=" + birth +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", swtYears=" + swtYears +
                ", livingLocation='" + livingLocation + '\'' +
                ", ratings=" + ratings +
                '}';
    }
}
