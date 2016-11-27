package models;

import java.util.List;
import java.util.LinkedList;
import play.data.validation.Constraints;
import play.mvc.PathBindable;
import play.libs.F;
import java.util.Date;

import play.data.format.*;
import play.data.validation.*;

import javax.persistence.*;
import com.avaje.ebean.Model;

@Entity
public class Student extends Model implements PathBindable<Student> {
    @Id
    public Long id;

    @Constraints.Required
    @Constraints.ValidateWith(value=NameValidator.class,message="Start with big letter")
    public String name;

    @Formats.DateTime(pattern="dd-MM-yyyy")
    public Date birthDate;

    public Long studId;

    @ManyToMany
    public List<Tag> tags = new LinkedList<>();

    @OneToMany(mappedBy="student")
    public List<Enrollment> enrollments = new LinkedList<>();

    public byte[] picture;
    
    public Student(String n, Long si) {
        name = n;
        studId = si;
    }
    
    public Student() {
        name = "unknown";
    }

    public String toString() {
        return String.format("%s (%d)", name, studId);
    }

    // supporting methods

    // binding with forms
    @Override
    public Student bind(String key, String value) {
        return findByStudId(Long.parseLong(value));
    }
    @Override
    public String unbind(String key) {
        return "" + this.studId;
    }
    @Override
    public String javascriptUnbind() {
        return "" + this.studId;
    }

    // data access
    public static Finder<Long, Student> find = new Finder<>(Student.class);
    public static List<Student> findAll() {
        return find.all();
    }
    public static Student findByStudId(Long studId) {
        return find.where().eq("studId", studId).findUnique();
    }
    public static void remove(Student stud) {
        stud.delete();
    }

    // internal validator
    public static class NameValidator
      extends Constraints.Validator<String> {
          @Override
          public boolean isValid(String name) {
              String pattern="^[A-Z][a-z]*$";
              return name != null && name.matches(pattern);
          }
          
          @Override
          public F.Tuple<String, Object[]> getErrorMessageKey() {
              return new F.Tuple<String,Object[]>("error.invalid.name", new Object[]{});
          }
    }

    public String getName() {
        return name;
    }
}
    
    
