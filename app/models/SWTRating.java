package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by TeoLenovo on 4/6/2017.
 */
//this annotation means that there will be no table for this class
@Entity
public class SWTRating extends Model {

    public SWTRating(SWTPlace swtPlace, Integer rating, String comment, boolean providingHousing, Integer workLoad,
                     Integer payment, String workPosition, SWTYear swtYear) {
        this.swtPlace = swtPlace;
        this.rating = rating;
        this.comment = comment;
        this.providingHousing = providingHousing;
        this.workLoad = workLoad;
        this.payment = payment;
        this.workPosition = workPosition;
        this.swtYear = swtYear;
    }

    @Id
    public Long id;

    @ManyToOne
    @JoinColumn(name = "swtPlace")
    public SWTPlace swtPlace;

    public Integer rating;
    public String comment;
    public boolean providingHousing;
    public Integer workLoad;
    public Integer payment;
    public String workPosition;

    @ManyToOne
    @JoinColumn(name = "swtYear_id")
    public SWTYear swtYear;

    public Date createdAt;

    public Date updatedAt;

    @PrePersist
    public void createdAt() {
        this.createdAt = this.updatedAt = new Date();
    }

    @PreUpdate
    public void updatedAt() {
        this.updatedAt = new Date();
    }


}
