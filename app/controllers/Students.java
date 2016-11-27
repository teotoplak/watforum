package controllers;

import java.util.*;

import models.Student;
import models.Tag;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.*;
import static play.mvc.Http.MultipartFormData;
import java.io.File;
import java.io.IOException;
import com.google.common.io.Files;
import javax.inject.Inject;

import views.html.*;


public class Students extends Controller {
    @Inject private FormFactory formFactory;

    public Result list() {
        List<Student> students = Student.findAll();
        return ok(list.render(students));
    }
    
    public Result newStudent() {
        return ok(details.render(formFactory.form(Student.class).bindFromRequest()));
    }

    public Result details(Student student) {
        Form<Student> filledForm = formFactory.form(Student.class).bindFromRequest().fill(student);
        return ok(details.render(filledForm));
    }

    public Result delete(Long studId) {
        final Student student = Student.findByStudId(studId);
        if(student == null) {
            return badRequest(String.format("Student with %d ID does not exists.", studId));
        }
        student.delete();
        return redirect(routes.Students.list());
    }
    
    public Result save() {
        Form<Student> boundForm = formFactory.form(Student.class).bindFromRequest();
        if (boundForm == null) {
            return null;
        }
        if(boundForm.hasErrors()){
            flash("error", "Please correct the form below");
            return badRequest(details.render(boundForm));
        }
        Student student = boundForm.get();
     
        MultipartFormData body = request().body().asMultipartFormData();
        MultipartFormData.FilePart part = body.getFile("picture");
        
        if(part != null) {
            File picture = (File)part.getFile();
            try {
                student.picture = Files.toByteArray(picture);
            } catch(IOException e) {
                return internalServerError("Error reading file upload");
            }
        }

        List<Tag> tags = new ArrayList<>();
        for(Tag tag : student.tags) {
            if(tag.id != null) {
                tags.add(Tag.findById(tag.id));
            }
        }
        student.tags = tags;

        if(student.id == null) {
            student.save();
        } else {
            student.update();
        }

        flash("success", String.format("Successfully added student: %s", student));
        return redirect(routes.Students.list());
    }
    
    public Result picture(Long studId) {
        final Student student = Student.findByStudId(studId);
        if(student == null) return notFound();
        return ok(student.picture);
    }
}