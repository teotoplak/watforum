package models;

import com.avaje.ebean.Model;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by TeoLenovo on 4/6/2017.
 */
@Entity
public class SWTYear extends Model implements Comparable{

    public SWTYear(Integer year, String agency, SWTUser user) {
        this.year = year;
        this.agency = agency;
        this.user = user;
    }

    @Id
    public Long id;

    public Integer year;
    public String agency;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public SWTUser user;

    @OneToMany(mappedBy = "swtYear")
    public Set<SWTRating> ratings;

    @Override
    public String toString() {
        return year + ". (" + agency + ")";
    }

    public static Finder<Long, SWTYear> find = new Finder<>(SWTYear.class);

    public static SWTYear findYear(String agency, Integer year, Long userId) {
        return find.where()
                .eq("agency", agency).and()
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
        if (comparingYear.year == year) {
            return 0;
        }
        return comparingYear.year > year? 1: -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SWTYear swtYear = (SWTYear) o;

        if (!year.equals(swtYear.year)) return false;
        return agency.equals(swtYear.agency);
    }

    @Override
    public int hashCode() {
        int result = year.hashCode();
        result = 31 * result + agency.hashCode();
        return result;
    }
}
