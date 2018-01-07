package security;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import javax.inject.Inject;
import akka.stream.Materializer;
import com.google.common.base.Strings;
import play.api.Play;
import play.mvc.*;

/**
 * Created by teo on 1/7/18.
 */
public class TLSFilter extends Filter {
    @Inject
    public TLSFilter(Materializer mat) {
        super(mat);
    }

    @Override
    public CompletionStage<Result> apply(Function<Http.RequestHeader, CompletionStage<Result>> next, Http.RequestHeader rh) {
        if (Play.current().isProd()) {
            String[] httpsHeader = rh.headers().getOrDefault(Http.HeaderNames.X_FORWARDED_PROTO, new String[]{"http"});
            if (Strings.isNullOrEmpty(httpsHeader[0]) || httpsHeader[0].equalsIgnoreCase("http")) {
                return CompletableFuture.completedFuture(Results.movedPermanently("https://".concat(rh.host().concat(rh.uri()))));
            }
        }
        return next.apply(rh).toCompletableFuture();
    }
}