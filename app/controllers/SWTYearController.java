package controllers;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.*;
import org.pac4j.play.java.Secure;
import play.Logger;
import play.cache.CacheApi;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;
import security.Secured;

import javax.inject.Inject;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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

    @Inject
    private FormFactory formFactory;

    private static final Logger.ALogger logger = Logger.of(SWTYearController.class);

    @Security.Authenticated(Secured.class)
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
        Long sponsorId = null;
        String newSponsorName = null;
        SWTSponsor sponsor = null;
        Long swtYearId = null;
        Integer year;
        Long userId;
        String nodeString;
        SWTYear swtYear;
        try {
         nodeString = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return badRequest();
        }
        logger.debug("Got request to do year (save:" + save + "): " + nodeString);

        if (save) {
            try {
                if (node.get("newSponsorName") != null) {
                    newSponsorName = node.get("newSponsorName").asText();
                }
                if (node.get("sponsorId") != null && !node.get("sponsorId").asText().isEmpty()) {
                    sponsorId = Long.parseLong(node.get("sponsorId").asText());
                }
                year = Integer.parseInt(node.get("year").asText());
                userId = Long.parseLong(node.get("userId").asText());
            } catch (Exception e) {
                return badRequest(e.getMessage());
            }
            if (newSponsorName != null) {
                sponsor = SWTSponsor.getSponsorByName(newSponsorName);
                if (sponsor == null) {
                    sponsor = new SWTSponsor(newSponsorName);
                    sponsor.save();
                }
            } else {
                sponsor = SWTSponsor.findSponsorById(sponsorId);
            }
            swtYear = SWTYear.findYear(sponsor.id, year, userId);
            if (swtYear != null) {
                return badRequest("Want to save swtYear but there is already one");
            }
            swtYear = new SWTYear(year, sponsor, SWTUser.findUserById(userId));
            swtYear.save();
            return ok(Json.toJson(swtYear));
        } else {
            swtYearId = Long.parseLong(node.get("yearId").asText());
            swtYear = SWTYear.findYearById(swtYearId);
            if (swtYear == null) {
                return badRequest("Want to delete swtYear but there is none like it");
            }
            // first delete all ratings of year
            List<SWTRating> ratings = SWTRating.findRatingsBySWTyear(swtYear.id);
            for(SWTRating rating : ratings) {
                rating.delete();
            }
            swtYear.delete();
            return ok();
        }
    }

    /**
     * Used for ajax calls from autocomplete agency
     */
    public Result getAllSponsors() {
        List<SWTSponsor> sponsors = SWTSponsor.getAllSponsors();
        return ok(Json.toJson(sponsors));
    }


}
