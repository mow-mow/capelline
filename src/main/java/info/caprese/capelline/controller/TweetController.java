package info.caprese.capelline.controller;

import info.caprese.capelline.entity.Tweet;
import info.caprese.capelline.entity.User;
import info.caprese.capelline.service.TweetService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Optional;

@Controller
@Slf4j
public class TweetController extends AbstractControlelr {

	@Autowired
	private TweetService tweetService;


	@GetMapping(path = "/tweet/{tweetId}")
	String view(Model model, @PathVariable("tweetId") String tweetId) throws IllegalAccessException {

		Optional<Pair<Tweet, User>> tweet = tweetService.find(tweetId);
		if (!tweet.isPresent()) {
			throw new IllegalAccessException();
		}

		model.addAttribute("tweet", tweet.get().getLeft());
		model.addAttribute("user", tweet.get().getRight());

		return "tweet/preview";
	}

	@GetMapping(path = "/new")
	String registView(Model model) {
		writeTweetIdIfLoggedInWithTwitter();

		model.addAttribute("tweet", new Tweet());
		return "tweet/new";
	}

	@PostMapping(path = "/new")
	@Transactional
	String regist(Principal principal, Tweet tweet, RedirectAttributes redirectAttributes) throws Exception {

		tweetService.regist(principal, tweet);

		redirectAttributes.addFlashAttribute("registSuccusessFlag", true);
		return "redirect:/tweet/"+tweet.getTweetId();
	}





}