package models;

import models.enumerations.SWTWorkPosition;

import java.util.Date;

/**
 * Created by TeoLenovo on 4/6/2017.
 */
public class SWTWorkRating extends SWTRating {

    public SWTWork work;
    public Integer rating;
    public Date ratedAt;
    public String comment;
    public boolean providingHousing;
    public Integer hoursPerWeekWork;
    public Integer payment;
    public SWTWorkPosition workPosition;
    public SWTYear swtYear;


}
