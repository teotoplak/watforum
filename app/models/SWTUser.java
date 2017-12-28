package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;
import play.libs.F;

import javax.persistence.*;
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
    //not required since there is also oauth
    public String password;
    @Column(unique = true)
    @Constraints.Email
    @Constraints.Required
    public String email;
    @Constraints.Required
    public String firstName;
    @Constraints.Required
    public String lastName;
    public String profilePictureUrl;
    //iso2 format
    public Locale country;
    public Date createdAt;


    //be anonymous to public with your data
    @Constraints.Required
    public boolean anonymous;
    @OneToMany(mappedBy = "user")
    public List<SWTYear> swtYears;

    /*
    those below still not used
     */
    public Date birth;
//    public SWTNationality nationality;
    //should be other then string
    public String livingLocation;

    @PrePersist
    public void createdAt() {
        this.createdAt = new Date();
    }

    public List<SWTYear> getSortedYears() {
        List<SWTYear> list = this.swtYears;
        Collections.sort(list);
        return list;
    }

    public Set<SWTRating> getAllRatings() {
        Set<SWTRating> ratings = new HashSet<>();
        for (SWTYear year : this.swtYears) {
            ratings.addAll(year.ratings);
        }
        return  ratings;
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

    public SWTUser() {
       swtYears = new LinkedList<>();
    }

    @Override
    public String toString() {
        return "SWTUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    /**
     * Get users name and surname or username if he's
     * anonymous
     * @return name
     */
    public String getIdentity() {
        if (anonymous)
            return username;
        return firstName + " " + lastName;

    }

    public boolean isOAuthAccount() {
        return this.password==null;
    }
}
