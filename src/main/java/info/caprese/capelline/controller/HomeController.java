package info.caprese.capelline.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import info.caprese.capelline.entity.Tweet;
import info.caprese.capelline.entity.User;
import info.caprese.capelline.repository.TweetRepository;
import info.caprese.capelline.repository.UserRepository;
import info.caprese.capelline.service.TweetLogic;
import info.caprese.capelline.util.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class HomeController {
	@Autowired
	HttpSession session;

	@Autowired
	private TweetRepository tweetRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private Twitter twitter;
	@Autowired
	private TweetLogic tweetLogic;

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
	String view(Model model, @PathVariable("tweetId") String tweetId) throws IllegalAccessException {

		Optional<Tweet> tweet = tweetRepository.findByTweetIdAndInvalidFlagAndDeleteFlag(tweetId, false, false);
		if (!tweet.isPresent()) {
			throw new IllegalAccessException();
		}


		Optional<User> user = userRepository.findOne(tweet.get().getUserId());
		model.addAttribute("tweet", tweet.get());
		model.addAttribute("user", user.get());

		return "tweet/preview";
	}

	@GetMapping(path = "/new")
	String registView(Model model) {
		model.addAttribute("tweet", new Tweet());
		return "tweet/new";
	}

	@PostMapping(path = "/new")
	@Transactional
	String regist(Principal principal, Tweet tweet, RedirectAttributes redirectAttributes) {


		tweet.setTweetId(UUID.randomUUID().toString());
		tweet.setMaskMessage(replacePasta(tweet.getMessage(), maskChar()));
		tweet.setMessage(convertHTMLMsg(tweet.getMessage()));

		tweet.setInvalidFlag(false);
		tweet.setDeleteFlag(false);
		tweet.setInsertDate(LocalDateTime.now());
		tweet.setUpdateDate(LocalDateTime.now());
		tweet.setUserId(AuthUtil.fetchUsedrId(principal));
		tweetRepository.save(tweet);

		tweetLogic.tweet(tweet);

		redirectAttributes.addFlashAttribute("registSuccusessFlag", true);
		return "redirect:/tweet/"+tweet.getTweetId();
	}

	private String convertHTMLMsg(String message) {
		message = message.replaceAll("\\r\\n|\\r|\\n", "<br>");
		return replaceSpan(message);
	}

	private String replaceSpan(String message) {
		int startChar = message.indexOf('[', 0);
		int endChar = message.indexOf(']', 0);

		if (startChar==-1 || endChar==-1) {
			return message;
		}
		message = message.replace("[", "<span>").replace("]", "</span>");
		return replaceSpan(message);
	}

	private String replacePasta(String message, String orig) {
		int startChar = message.indexOf('[', 0);
		int endChar = message.indexOf(']', 0);

		if (startChar==-1 || endChar==-1) {
			return message;
		}
		message = message.substring(0, startChar) + StringUtils.repeat("🍝",endChar - (startChar + 2-1)) + message.substring(endChar+1, message.length()-1);
		return replacePasta(message, orig);
	}

	private String maskChar() {
		Random random = new Random();
		int num = random.nextInt(20);
		if (num == 0) {
			return "🍙";
		} else if (1 <= num && num <= 14) {
			return "🍝";
		} else {        // num == 19
			return "🍕";
		}
	}

}