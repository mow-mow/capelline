package info.caprese.capelline.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TWEET")
public class Tweet implements Serializable {

	@Id
	private String tweetId;


	private String message;
	private String maskMessage;

	private String twitterUserId;

	private LocalDateTime updateDate;
	private LocalDateTime insertDate;
	@Version
	private int version;
	private boolean invalidFlag;
	private boolean deleteFlag;
}