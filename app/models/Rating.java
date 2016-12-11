package models;

import com.avaje.ebean.Model;
import play.mvc.Result;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.ocpsoft.prettytime.PrettyTime;

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

    @Column(name = "rated_at")
    public Date ratedAt;

    public Rating(User user, WatPlace watPlace, Integer rating) {
        this.user = user;
        this.watPlace = watPlace;
        this.rating = rating;
    }

    @Override
    public void save() {
        ratedAt = new Date();
        super.save();
    }

    @Override
    public void update() {
        ratedAt = new Date();
        super.update();
    }

    public String getRatedAt() {
        PrettyTime p = new PrettyTime();
        return p.format(ratedAt);
    }

    public static Finder<Long, Rating> find = new Finder<>(Rating.class);

    public static List<Rating> findAll() {
        return find.order("rated_at desc").findList();
    }

    public static Integer findRating(User user, WatPlace watPlace) {
        Rating rating = find.where().eq("user_id",user.id).and().eq("watPlace_id",watPlace.id).findUnique();
        if (rating == null) {
            return -1;
        } else {
            return rating.rating;
        }
    }

    public static Integer findAverageRatingForPlace(Long placeId) {
        List<Rating> ratings = find.where().eq("watPlace_id", placeId).findList();
        if(ratings.isEmpty()) {
            return -1;
        }
        Double sum = 0.0;
        Double num = 0.0;
        for (Rating rating : ratings) {
            sum+=rating.rating;
            num++;
        }
        return (int)Math.round(sum/num);
    }

    public static List<Rating> userRatings(Long user_id) {
        return find.where().eq("user_id", user_id).findList();
    }

}
