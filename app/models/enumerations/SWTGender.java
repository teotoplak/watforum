package models.enumerations;

import java.util.Arrays;
import java.util.List;

/**
 * Created by teo on 5/5/17.
 */
public enum SWTGender {

    MALE,FEMALE;

    public static SWTGender toValue(String input) throws IllegalArgumentException{
        if(legalGenderInputsMale.contains(input)) {
            return MALE;
        }
        if(legalGenderInputsFemale.contains(input)) {
            return FEMALE;
        }
        throw new IllegalArgumentException();
    }

    private static final List<String> legalGenderInputsMale =
            Arrays.asList("Male","M","MALE","m","male");

    private static final List<String> legalGenderInputsFemale =
            Arrays.asList("Female","F","FEMALE","f","female");

}
