package security;

import com.typesafe.sslconfig.util.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.pac4j.core.authorization.authorizer.ProfileAuthorizer;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.profile.CommonProfile;
import org.slf4j.Logger;

import java.util.List;

public class MyCustomAuth extends ProfileAuthorizer<CommonProfile> {

    private Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

    @Override
    public boolean isAuthorized(final WebContext context, final List<CommonProfile> profiles) throws HttpAction {
        logger.error("checking if one of the profiles is authorized..");
        return isAnyAuthorized(context, profiles);
    }

    @Override
    public boolean isProfileAuthorized(final WebContext context, final CommonProfile profile) {
//        this iterates through everyProfile
        context.setSessionAttribute("profile", profile.getFirstName());
        logger.error("checking if the profile> "+ profile.getFirstName() +" is authorized..");
        if (profile == null) {
            return false;
        }

        return true;
    }
}
