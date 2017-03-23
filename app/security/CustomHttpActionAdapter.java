package security;

import org.pac4j.core.context.HttpConstants;
import org.pac4j.play.PlayWebContext;
import org.pac4j.play.http.DefaultHttpActionAdapter;
import org.slf4j.Logger;
import play.mvc.Result;

import static play.mvc.Results.*;

public class CustomHttpActionAdapter extends DefaultHttpActionAdapter {

        private Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());


    @Override
    public Result adapt(int code, PlayWebContext context) {

        if (code == HttpConstants.UNAUTHORIZED) {
            logger.error("login is unathorized..");
            return unauthorized(views.html.error401.render().toString()).as((HttpConstants.HTML_CONTENT_TYPE));
        } else if (code == HttpConstants.FORBIDDEN) {
            logger.error("login is forbidden..");
            return forbidden(views.html.error403.render().toString()).as((HttpConstants.HTML_CONTENT_TYPE));
        } else {
            return super.adapt(code, context);
        }
    }
}
