package info.caprese.capelline.repository;

import info.caprese.capelline.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Integer> {

	Optional<Tweet> findByTweetIdAndInvalidFlagAndDeleteFlag(String tweetId, boolean invalidFlag, boolean deleteFlag);

}