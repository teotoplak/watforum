package models;

import com.avaje.ebean.Model;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

/**
 * Created by TeoLenovo on 4/6/2017.
 */
@MappedSuperclass
public class SWTPlace extends Model {

    @Id
    public Long id;

    public String name;

    public String address;

    public String phoneNumber;

    public String icon;

    public Date createdAt;

    public Date updatedAt;

    @PrePersist
    public void createdAt() {
        this.createdAt = this.updatedAt = new Date();
    }

    @PreUpdate
    public void updatedAt() {
        this.updatedAt = new Date();
    }


}
