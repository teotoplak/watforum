package controllers;

import com.avaje.ebean.Model;

/**
 * Created by teo on 12/9/16.
 */
public class RatingForm extends Model{
    protected Long user_id;
    protected Long watPlace_id;
    protected Integer rating;

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getWatPlace_id() {
        return watPlace_id;
    }

    public void setWatPlace_id(Long watPlace_id) {
        this.watPlace_id = watPlace_id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
