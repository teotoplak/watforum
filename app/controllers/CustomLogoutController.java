package controllers;

import org.pac4j.play.LogoutController;
import org.slf4j.LoggerFactory;
import play.Logger;

public class CustomLogoutController extends LogoutController {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    public CustomLogoutController() {
        logger.info("Inside Oauth logout controller...");
        setDefaultUrl("/");
        setLocalLogout(true);
        setCentralLogout(true);
        setDestroySession(true);
//        setLogoutUrlPattern("http://localhost:9000/.*");
    }
}