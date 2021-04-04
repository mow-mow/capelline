package info.caprese.capelline.util;

import info.caprese.capelline.data.SocialUserData;
import org.springframework.security.core.Authentication;

import java.security.Principal;

public class AuthUtil {
    public static String fetchUsedrId(Principal principal) {
        SocialUserData loginUser = (SocialUserData) ((Authentication)principal).getPrincipal();
        return loginUser.getUser().getUserId();
    }
}
