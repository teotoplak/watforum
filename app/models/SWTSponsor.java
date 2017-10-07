package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * Created by teo on 10/6/17.
 */
@Entity
public class SWTSponsor extends Model {

    public SWTSponsor(String fullName) {
        this.fullName = fullName;
    }

    public SWTSponsor(String fullName, String shortName, String websiteUrl) {
        this.fullName = fullName;
        this.shortName = shortName;
        this.websiteUrl = websiteUrl;
    }

    @Id
    public Long id;

    @Column(unique=true)
    public String fullName;

    @Column(unique=true)
    public String shortName;

    @JsonIgnore
    public String websiteUrl;

    @OneToMany(mappedBy = "sponsor")
    @JsonIgnore
    public Set<SWTYear> swtYears;

    //DAO
    public static Finder<Long, SWTSponsor> find = new Finder<>(SWTSponsor.class);
    public static SWTSponsor findSponsorById(Long id) {
        return find.where().eq("id", id).findUnique();
    }
    public static List<SWTSponsor> getAllSponsors() {
        return find.all();
    }

    /* checking for both full and short sponsor name */
    public static SWTSponsor getSponsorByName(String sponsorName) {
        return find.where().eq("full_name", sponsorName).findUnique();
    }

    @Override
    public String toString() {
        String string = this.fullName;
        if (this.shortName != null) {
            string += " (" + this.shortName + ")";
        }
        return string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SWTSponsor)) return false;

        SWTSponsor that = (SWTSponsor) o;

        if (!id.equals(that.id)) return false;
        if (!fullName.equals(that.fullName)) return false;
        if (shortName != null ? !shortName.equals(that.shortName) : that.shortName != null) return false;
        return websiteUrl != null ? websiteUrl.equals(that.websiteUrl) : that.websiteUrl == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + fullName.hashCode();
        result = 31 * result + (shortName != null ? shortName.hashCode() : 0);
        return result;
    }
}
