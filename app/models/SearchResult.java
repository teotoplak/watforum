package models;

/**
 * Created by teo on 11/25/17.
 * Helper class, not model
 * Used as a json response for search results
 */
public class SearchResult {

    public Long id;
    public String type; // enum [rated,notRated,region]
    public String name;
    public Double lat;
    public Double lng;
    public Double avgRating;
    public Integer numRatings;
    public String googleId;
    public Double NElat;
    public Double NElng;
    public Double SWlat;
    public Double SWlng;

    public SearchResult(SWTPlace place) {
        this.type = "rated";
        this.id = place.id;
        this.name = place.name;
        this.lng = place.lng;
        this.lat = place.lat;
        this.googleId = place.googleId;
        this.avgRating = place.avgRating;
        this.numRatings = place.numRatings;
    }

    public SearchResult(SWTGooglePlace place) {
        this.type = place.isRegion? "region" : "notRated";
        if(type.equals("region")) {
            NElat = place.NElat;
            NElng = place.NElng;
            SWlat = place.SWlat;
            SWlng = place.SWlng;
        }
        this.googleId = place.googleID;
        this.name = place.name;
        this.lng = place.lng;
        this.lat = place.lat;
    }


    @Override
    public String toString() {
        return "SearchResult{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", avgRating=" + avgRating +
                ", numRatings=" + numRatings +
                ", googleId='" + googleId + '\'' +
                ", NElat=" + NElat +
                ", NElng=" + NElng +
                ", SWlat=" + SWlat +
                ", SWlng=" + SWlng +
                '}';
    }
}
