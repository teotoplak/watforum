package controllers;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;
import models.SWTUser;
import models.SWTYear;
import play.mvc.Result;

import static play.mvc.Controller.request;
import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;

/**
 * Created by TeoLenovo on 4/11/2017.
 */
public class SWTYearController extends Model {

    public Result saveSWTYear() {
        return SUDrequest(true, request().body().asJson());
    }

    public Result deleteSWTYear() {
        return SUDrequest(false, request().body().asJson());
    }

    private Result SUDrequest(boolean save, JsonNode node) {
        String agency;
        Integer year;
        Long userId;
        try {
            agency = node.get("agency").asText();
            year = Integer.parseInt(node.get("year").asText());
            userId = Long.parseLong(node.get("userId").asText());
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }
        SWTYear swtYear = SWTYear.findYear(agency, year, userId);
        if (save) {
            if (swtYear != null) {
                return badRequest();
            }
            swtYear = new SWTYear(year, agency, SWTUser.findUserById(userId));
            swtYear.save();
        } else {
            swtYear.delete();
        }
        return ok();
    }
}
