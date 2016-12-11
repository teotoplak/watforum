package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by teo on 12/9/16.
 */
@Entity
public class Rating extends Model {

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;

    @ManyToOne
    @JoinColumn(name = "watPlace_id")
    public WatPlace watPlace;

    @Column(name = "rating")
    public Integer rating;

    public Rating(User user, WatPlace watPlace, Integer rating) {
        this.user = user;
        this.watPlace = watPlace;
        this.rating = rating;
    }


    public static Finder<Long, Rating> find = new Finder<>(Rating.class);

    public static List<Rating> findAll() {
        return find.all();
    }

    public static Integer findRating(User user, WatPlace watPlace) {
        Rating rating = find.where().eq("user_id",user.id).and().eq("watPlace_id",watPlace.id).findUnique();
        System.out.println(watPlace.id);
        if (rating == null) {
            return -1;
        } else {
            return rating.rating;
        }
    }
}
