package controllers;

import play.mvc.*;
import java.util.List;
import models.*;
import com.avaje.ebean.PagedList;

public class Courses extends Controller {
  
    public Result pagedList(Integer page) {
        PagedList<Course> courses = Course.findByPage(page);
        return ok(views.html.courselistpaged.render(courses));
    }
    
    public Result list() {
        List<Course> courses = Course.findAll();
       return ok(views.html.courselist.render(courses));
    }
}