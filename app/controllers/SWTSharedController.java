package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.SWTUser;
import play.Configuration;
import play.data.DynamicForm;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

/**
 * Created by teo on 8/15/17.
 */
public class SWTSharedController extends Controller {

    @Inject
    private Configuration configuration;

    /**
     * Used for ajax calls
     */
    public Result getBaseUrl() {
        return ok(configuration.getString("baseUrl"));
    }

}
