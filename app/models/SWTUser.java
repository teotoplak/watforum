package models;

import models.enumerations.SWTNationality;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by TeoLenovo on 4/6/2017.
 */
public class SWTUser {

    public Long id;
    public String nickname;
    public String firstName;
    public String lastName;
    public Image profilePicture;
    public String contact;
    public String email;
    public List<SWTYear> swtYears;
    public String password;
    //for auth
    public List<String> loginProfiles;
    public SWTNationality nationality;
    //should be other then string
    public String livingLocation;

    //should I have this? get them over swtYears
    public Set<SWTRating> ratings;

}
