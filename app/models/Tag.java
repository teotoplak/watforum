package models;

import play.data.validation.Constraints;
import java.util.*;
import javax.persistence.*;
import com.avaje.ebean.Model;

@Entity
public class Tag extends Model {
    @Id
    public Long id;

    @ManyToMany(mappedBy="tags")
    public List<Student> students;

    @Constraints.Required
    public String name;
    
    public Tag() {}
    
    public Tag(Long id, String name, Collection<Student> students) {
        this.id = id;
        this.name = name;
        this.students = new LinkedList<Student>(students);
        for(Student student:students) {
            student.tags.add(this);
        }
    }
    
    public static Tag findById(Long id) {
        return find.byId(id);
    }
    
    public static Finder<Long, Tag> find = new Finder<>(Tag.class);
    
    
}