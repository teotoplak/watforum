package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;
import play.libs.F;

import javax.annotation.Generated;
import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by teo on 11/27/16.
 */
@Entity
public class User extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long id;

    @Constraints.Required
    @Column(unique=true)
    @Constraints.ValidateWith(value=UsernameValidator.class,message = "Username must contain " +
            "at least 3 characters. All letters, numbers, points, dashes and underscores allowed.")
    public String username;

    @Constraints.Required
    public String password;

    public String country;

    @OneToMany(mappedBy = "user")
    public Set<Rating> ratings = new HashSet<>();


    public static Finder<Long, User> find = new Finder<>(User.class);

    public static List<User> findAll() {
        return find.all();
    }

    public static User checkUser(String username, String password) {
        return find.where().eq("username", username).and().eq("password", password).findUnique();
    }

    public static User findUserByUsername(String username) {
        return find.where().eq("username", username).findUnique();
    }

    public static User findUserById(Long id) {
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

