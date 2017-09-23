package controllers;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.SWTRating;
import models.SWTUser;
import models.SWTYear;
import play.Logger;
import play.cache.CacheApi;
import play.mvc.Result;

import javax.inject.Inject;

import java.util.List;

import static play.mvc.Controller.flash;
import static play.mvc.Controller.request;
import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;
import static play.mvc.Results.redirect;

/**
 * Created by TeoLenovo on 4/11/2017.
 */
public class SWTYearController extends Model {

    @Inject
    private CacheApi cache;

    private static final Logger.ALogger logger = Logger.of(SWTYearController.class);

    public Result swtYear(Long id) {
        SWTYear year = SWTYear.findYear(id);
        return ok(views.html.swtYear.render(year));
    }

    /* Right now used only for ajax calls*/
    public Result saveSWTYear() {
        return SUDrequest(true, request().body().asJson());
    }

    /* Right now used only for ajax calls*/
    public Result deleteSWTYear() {
        return SUDrequest(false, request().body().asJson());
    }

    private Result SUDrequest(boolean save, JsonNode node) {
        String agency;
        Integer year;
        Long userId;
        String nodeString;
        try {
         nodeString = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return badRequest();
        }
        logger.debug("Got request to do year (save:" + save + "): " + nodeString);

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
                return badRequest("Want to save swtYear but there is already one");
            }
            swtYear = new SWTYear(year, agency, SWTUser.findUserById(userId));
            swtYear.save();
        } else {
            if (swtYear == null) {
                return badRequest("Want to delete swtYear but there is none like it");
            }
            // first delete all ratings of year
            List<SWTRating> ratings = SWTRating.findRatingsBySWTyear(swtYear.id);
            for(SWTRating rating : ratings) {
                rating.delete();
            }
            swtYear.delete();
        }
        return ok();
    }


}
