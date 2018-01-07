package security;

import play.http.DefaultHttpFilters;

/**
 * Created by teo on 1/8/18.
 */
public class AppFilters extends DefaultHttpFilters {

    @com.google.inject.Inject
    public AppFilters(TLSFilter tlsFilter) {
        super(tlsFilter);
    }
}