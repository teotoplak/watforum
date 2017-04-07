package models;

import models.enumerations.SWTWorkPosition;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Created by TeoLenovo on 4/6/2017.
 */
@Entity
public class SWTWorkRating extends SWTRating {



    @ManyToOne
    @JoinColumn(name = "swtWorkPlace_id")
    public SWTWorkPlace swtWorkPlace;

    public Integer rating;
    public Date ratedAt;
    public String comment;
    public boolean providingHousing;
    public Integer hoursPerWeekWork;
    public Integer payment;
//    public SWTWorkPosition workPosition;

}
