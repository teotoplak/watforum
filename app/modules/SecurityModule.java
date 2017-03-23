package modules;

import com.google.inject.AbstractModule;
import controllers.CurrentUserUtility;
import controllers.Users;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.http.client.direct.DirectBasicAuthClient;
import org.pac4j.http.client.direct.ParameterClient;
import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.http.client.indirect.IndirectBasicAuthClient;
import org.pac4j.http.credentials.authenticator.test.SimpleTestUsernamePasswordAuthenticator;
import org.pac4j.oauth.client.FacebookClient;
import org.pac4j.oauth.client.TwitterClient;
import org.pac4j.play.CallbackController;
import org.pac4j.play.LogoutController;
import org.pac4j.play.deadbolt2.Pac4jHandlerCache;
import org.pac4j.play.deadbolt2.Pac4jRoleHandler;
import org.pac4j.play.store.PlayCacheSessionStore;
import org.pac4j.play.store.PlaySessionStore;
import play.Configuration;
import play.Environment;
import play.cache.CacheApi;
import security.CustomHttpActionAdapter;
import security.MyCustomAuth;

import java.io.File;

/*For now only supporting facebook client*/
public class SecurityModule extends AbstractModule {

    private final Configuration configuration;

    private static class MyPac4jRoleHandler implements Pac4jRoleHandler { }

    public SecurityModule(final Environment environment, final Configuration configuration) {
        this.configuration = configuration;
    }

    private final String fbId = "132736803558924";
    private final String fbSecret = "e461422527aeedb32ee6c10834d3e19e";
    private final String baseUrl = "http://localhost:9000"; //need it for form


    @Override
    protected void configure() {


        final PlayCacheSessionStore playCacheSessionStore = new PlayCacheSessionStore(getProvider(CacheApi.class));
        bind(PlaySessionStore.class).toInstance(playCacheSessionStore);
        requestStaticInjection(CurrentUserUtility.class);

        // OAuth
        final FacebookClient facebookClient = new FacebookClient(fbId, fbSecret);

        final Clients clients = new Clients(baseUrl + "/callback",
                facebookClient
                );

        final Config config = new Config(clients);
        config.setHttpActionAdapter(new CustomHttpActionAdapter());
        config.addAuthorizer("custom", new MyCustomAuth());
        bind(Config.class).toInstance(config);

        // callback controller
        final CallbackController callbackController = new CallbackController();
        callbackController.setDefaultUrl("/");
        callbackController.setMultiProfile(false);
        bind(CallbackController.class).toInstance(callbackController);

        // logout
        final LogoutController logoutController = new LogoutController();
        logoutController.setDefaultUrl("/");
        logoutController.setLocalLogout(true);
        logoutController.setCentralLogout(true);
        logoutController.setDestroySession(true);
        bind(LogoutController.class).toInstance(logoutController);
    }
}
