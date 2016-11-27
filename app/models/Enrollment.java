package models;

import models.*;
import javax.persistence.*;
import com.avaje.ebean.Model;

@Entity
public class Enrollment extends Model {
    @Id
    public long id;
    
    @ManyToOne
    public Course course;

    @ManyToOne
    public Student student;
    
    public int grade;
    
    public String toString() {
        return String.format("%s - %s", course, student);
    }
}