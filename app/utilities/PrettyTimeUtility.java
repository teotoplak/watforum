package utilities;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

/**
 * Created by teo on 5/23/17.
 */
public class PrettyTimeUtility {

    public static String prettyTime(Date date) {
        PrettyTime prettyTime = new PrettyTime();
        return prettyTime.format(date);
    }

}
