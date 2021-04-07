package info.caprese.capelline.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.security.SocialAuthenticationToken;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;

import javax.servlet.http.HttpSession;

public class AbstractControlelr {
    @Autowired
    HttpSession session;
    @Autowired
    private Twitter twitter;

    public void writeTweetIdIfLoggedInWithTwitter() {
        if (session.getAttribute("icon") == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth instanceof SocialAuthenticationToken) {
                long tweetId = twitter.timelineOperations().getUserTimeline().get(0).getId();
                TwitterProfile profile = twitter.userOperations().getUserProfile();

                session.setAttribute("icon", profile.getProfileImageUrl());
            }
        }
    }
}
