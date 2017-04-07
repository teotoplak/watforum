package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by TeoLenovo on 4/6/2017.
 */
@Entity
public class SWTYear extends Model{

    @Id
    public Long id;

    public Integer year;
    public String country;
    public String agency;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public SWTUser user;

    @OneToMany(mappedBy = "swtYear")
    public Set<SWTWorkRating> ratings;

}
