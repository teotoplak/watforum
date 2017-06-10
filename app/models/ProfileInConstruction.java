package models;

import models.enumerations.OAuthClient;

/**
 * Created by teo on 5/1/17.
 * Used as a cache for storing auth user data
 */
public class ProfileInConstruction {
        public SWTUser user;
        public OAuthClient client;
        public String oauthId;
        public ProfileInConstruction(SWTUser user, OAuthClient client, String oauthId) {
            this.user = user;this.client = client;this.oauthId = oauthId;
        }
}
