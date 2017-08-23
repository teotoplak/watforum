package models;

import com.avaje.ebean.Model;
import models.enumerations.OAuthClient;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by teo on 4/30/17.
 *
 */
@Entity
public class SWTOAuthUser extends Model {

    @Id
    public Long id;
    @NotNull
    public String oauthId;
    @NotNull
    public OAuthClient client;
    @ManyToOne
    @JoinColumn(name = "user_id")
    public SWTUser user;

    public SWTOAuthUser(String oauthId, OAuthClient client, SWTUser user) {
        this.oauthId = oauthId;
        this.client = client;
        this.user = user;
    }

    public static Finder<Long, SWTOAuthUser> find = new Finder<>(SWTOAuthUser.class);

    public static SWTOAuthUser findSWTUserByOAuthIdAndClient(String oauthId, OAuthClient client) {
        return find.where().eq("oauthId", oauthId).and().eq("client",client).findUnique();
    }

    public static boolean checkIfUserIsOauth(Long userId) {
        return find.where().eq("user_id", userId).findUnique() != null;
    }

    public static SWTUser getSWTUser(String oauthId, OAuthClient client) {
        SWTOAuthUser swtoAuthUser = findSWTUserByOAuthIdAndClient(oauthId,client);
        if (swtoAuthUser == null) {
            return null;
        }
        return swtoAuthUser.user;
    }
}
