package models;

import models.*;
import java.util.*;
import javax.persistence.*;
import com.avaje.ebean.Model;
import com.avaje.ebean.PagedList;

@Entity
public class Course extends Model {
    @Id
    public Long id;

    @OneToMany(mappedBy="course")
    public List<Enrollment> enrollments = new ArrayList<>();
    
    public String name;
    
    @Override
    public String toString() {
        return name;
    }
    
    public static List<Course> findAll() {
        return find.all();
    }
    
    public static PagedList<Course> findByPage(int page) {
        final int PAGE_SIZE = 2;
        return find.order().asc("id").findPagedList(page, PAGE_SIZE);
    }
    
    public static Finder<Long, Course> find = new Finder<>(Course.class);
}