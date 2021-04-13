package info.caprese.capelline.service;

import info.caprese.capelline.entity.Tweet;
import info.caprese.capelline.entity.User;
import info.caprese.capelline.logic.TweetLogic;
import info.caprese.capelline.repository.TweetRepository;
import info.caprese.capelline.repository.UserRepository;
import info.caprese.capelline.util.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import twitter4j.Status;

import javax.transaction.Transactional;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
public class TweetService {

    @Autowired
    private TweetLogic tweetLogic;
    @Autowired
    private TweetRepository tweetRepository;
    @Autowired
    private UserRepository userRepository;

    public Optional<Pair<Tweet, User>> find(String tweetId) {
        Optional<Tweet> tweet = tweetRepository.findByTweetIdAndInvalidFlagAndDeleteFlag(tweetId, false, false);
        if (!tweet.isPresent()) {
            return Optional.empty();
        }
        Optional<User> user = userRepository.findOne(tweet.get().getUserId());
        if (!user.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(Pair.of(tweet.get(), user.get()));
    }

    public void regist(Principal principal, Tweet tweet) throws Exception {


        tweet.setTweetId(UUID.randomUUID().toString());
        tweet.setOriginalMessage(tweet.getMessage());
        tweet.setMaskMessage(replacePasta(tweet.getMessage(), maskChar()) + "\r\n" + "https://chobitter.caprese.info/tweet/" + tweet.getTweetId());
        tweet.setMessage(convertHTMLMsg(tweet.getMessage()));

        tweet.setInvalidFlag(false);
        tweet.setDeleteFlag(false);
        tweet.setInsertDate(LocalDateTime.now());
        tweet.setUpdateDate(LocalDateTime.now());
        tweet.setUserId(AuthUtil.fetchUsedrId(principal));
        tweetRepository.save(tweet);

		Status status = tweetLogic.tweet(tweet);

        tweet.setTwitterStatusId(status.getId());
        tweet.setUpdateDate(LocalDateTime.now());
        tweetRepository.save(tweet);

        log.info("ちょびったー登録 - [OK]");
    }


    private String maskChar() {
        Random random = new Random();
        int num = random.nextInt(20);
        if (num == 0) {
            return "🍙";
        } else if (1 <= num && num <= 14) {
            return "🍝";
        } else {
            return "🍕";
        }
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
        message = message.substring(0, startChar)
                + StringUtils.repeat("🍝",endChar - (startChar + 2-1))
                + StringUtils.right(message, message.length() - endChar - 1) ;
        return replacePasta(message, orig);
    }
}
