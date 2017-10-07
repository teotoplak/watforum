package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by TeoLenovo on 4/6/2017.
 */
@Entity
public class SWTYear extends Model implements Comparable{

    public SWTYear(Integer year, SWTSponsor sponsor, SWTUser user) {
        this.year = year;
        this.sponsor = sponsor;
        this.user = user;
    }

    @Id
    public Long id;

    public Integer year;

    @ManyToOne
    @JoinColumn(name = "sponsor_id")
    public SWTSponsor sponsor;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    public SWTUser user;

    @OneToMany(mappedBy = "swtYear")
    @JsonIgnore
    public Set<SWTRating> ratings;

    @Override
    public String toString() {
        return year + ". (" + sponsor + ")";
    }

    public static Finder<Long, SWTYear> find = new Finder<>(SWTYear.class);
    public static SWTYear findYearById(Long id) {
        return find.where().eq("id", id).findUnique();
    }
    public static SWTYear findYear(Long sponsorId, Integer year, Long userId) {
        return find.where()
                .eq("sponsor_id", sponsorId).and()
                .eq("year", year).and()
                .eq("user_id", userId)
                .findUnique();
    }

    public static SWTYear findYear(Long id) {
        return find.where().eq("id", id).findUnique();
    }

    @Override
    public int compareTo(@NotNull Object o) {
        SWTYear comparingYear = (SWTYear) o;
        if (comparingYear.year.equals(year)) {
            return 0;
        }
        return comparingYear.year > year? 1: -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SWTYear)) return false;

        SWTYear swtYear = (SWTYear) o;

        if (!id.equals(swtYear.id)) return false;
        if (!year.equals(swtYear.year)) return false;
        if (!sponsor.equals(swtYear.sponsor)) return false;
        return user.equals(swtYear.user);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + year.hashCode();
        result = 31 * result + sponsor.hashCode();
        result = 31 * result + user.hashCode();
        return result;
    }
}
