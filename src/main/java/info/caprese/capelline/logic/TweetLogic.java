package info.caprese.capelline.logic;

import info.caprese.capelline.entity.Tweet;
import info.caprese.capelline.entity.UserConnection;
import info.caprese.capelline.repository.UserConnectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import java.util.Optional;

@Slf4j
@Service
public class TweetLogic {

    @Value("${spring.social.twitter.consumer-id}")
    private String consumerId;
    @Value("${spring.social.twitter.consumer-secret}")
    private String consumerSecret;

    @Autowired
    private UserConnectionRepository userConnectionRepository;

    public Status tweet(Tweet tweet) throws Exception {

        Optional<UserConnection> userConnection = userConnectionRepository.findById(tweet.getUserId());
        if(!userConnection.isPresent()) {
            log.error("ユーザ接続情報存在チェック - [NG] : " + tweet.getUserId());
            throw new IllegalStateException();
        }
        log.info("ユーザ接続情報存在チェック - [OK]" + tweet.getUserId());

        Twitter twitter = generateTwitter(userConnection.get().getAccessToken(), userConnection.get().getSecret());
        Status status;
        try {
            status = twitter.updateStatus(tweet.getMaskMessage());
        } catch (TwitterException e) {
            log.error("ツイート - [NG]", e);
            throw e;
        }

        log.info("ツイート - [OK]");
        log.info("message : " + status.getText());
        return status;
    }

    private Twitter generateTwitter(String accessToken, String secret) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerId)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(secret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        return tf.getInstance();
    }
}





