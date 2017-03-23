package controllers;

import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.play.PlayWebContext;
import org.pac4j.play.store.PlaySessionStore;
import play.mvc.Controller;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Created by TeoLenovo on 3/23/2017.
 */
public class CurrentUserUtility extends Controller{

    /*This gets static injection in SecurityModule.
    * This is NOT recommended - find a better access to logged in
    * user for navbar.*/
    @Inject
    protected static PlaySessionStore playSessionStore;

    public static CommonProfile getUserProfile() {
        PlayWebContext webContext = new PlayWebContext(ctx(), playSessionStore);
        ProfileManager<CommonProfile> profileManager = new ProfileManager(webContext);
        Optional<CommonProfile> profile = profileManager.get(true);
        if (profile.isPresent()) {
            return profile.get();
        }
        return null;
    }


    public static String getUserPictureUrl() {
        if (getUserProfile().getClientName().equals("FacebookClient")) {
            return "http://graph.facebook.com/"+ getUserProfile().getId() +"/picture";
        }
        return null;
    }
}
