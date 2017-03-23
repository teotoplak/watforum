package security;

import play.Configuration;
import play.Environment;
import play.api.OptionalSourceMapper;
import play.api.UsefulException;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;
import play.libs.F.Promise;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import javax.inject.Provider;

public class CustomErrorHandler extends DefaultHttpErrorHandler {

    @Inject
    public CustomErrorHandler(Configuration configuration, Environment environment,
                              OptionalSourceMapper sourceMapper, Provider<Router> routes) {
        super(configuration, environment, sourceMapper, routes);
    }

    protected Promise<Result> onDevServerError(RequestHeader var1, UsefulException var2) {
        return Promise.pure(Results.status(500, views.html.error500.render()));
    }

    protected Promise<Result> onProdServerError(RequestHeader var1, UsefulException var2) {
        return Promise.pure(Results.status(500, views.html.error500.render()));
    }
}
