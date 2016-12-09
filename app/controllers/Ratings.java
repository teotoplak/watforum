package controllers;

import models.Rating;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import java.util.List;

/**
 * Created by teo on 12/9/16.
 */
public class Ratings extends Controller {

    public Result listAll() {
        List<Rating> list = Rating.findAll();
        return ok(ratings.render(list));
    }
}
