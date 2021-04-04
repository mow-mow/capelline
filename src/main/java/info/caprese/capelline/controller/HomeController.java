package info.caprese.capelline.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import info.caprese.capelline.entity.Tweet;
import info.caprese.capelline.repository.TweetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.security.SocialAuthenticationToken;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Slf4j
public class HomeController {
	@Autowired
	HttpSession session;

	@Autowired
	private TweetRepository tweetRepository;
	@Autowired
	private Twitter twitter;

	@GetMapping(path = "/")
	String showMainPage(Model model) {
		writeTweetIdIfLoggedInWithTwitter();


		return "main/main";
	}

	private void writeTweetIdIfLoggedInWithTwitter() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth instanceof SocialAuthenticationToken) {
			long tweetId = twitter.timelineOperations().getUserTimeline().get(0).getId();
			TwitterProfile profile = twitter.userOperations().getUserProfile();

			session.setAttribute("icon", profile.getProfileImageUrl());
			log.info(""+profile.getProfileImageUrl());
			log.info(""+tweetId);

	//		 twitter.timelineOperations().updateStatus("Hello!!");
		}
	}


	@GetMapping(path = "/tweet/{tweetId}")
	String update(Model model, @PathVariable("tweetId") String tweetId) throws IllegalAccessException {

		Optional<Tweet> tweet = tweetRepository.findByTweetIdAndInvalidFlagAndDeleteFlag(tweetId, false, false);
		if (!tweet.isPresent()) {
			throw new IllegalAccessException();
		}


		model.addAttribute("tweet", tweet.get());

		return "tweet/preview";
	}

}