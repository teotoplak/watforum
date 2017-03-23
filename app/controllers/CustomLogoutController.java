package controllers;

import org.pac4j.play.LogoutController;

public class CustomLogoutController extends LogoutController {

    public CustomLogoutController() {
        setDefaultUrl("/");
        setLocalLogout(true);
        setCentralLogout(true);
        setDestroySession(true);
//        setLogoutUrlPattern("http://localhost:9000/.*");
    }
}