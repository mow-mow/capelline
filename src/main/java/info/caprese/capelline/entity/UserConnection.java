package info.caprese.capelline.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "UserConnection")
public class UserConnection implements Serializable {
	@Id
	@Column(name = "userId")
	private String userId;
	@Column(name = "providerId")
	private String providerId;
	@Column(name = "providerUserId")
	private String providerUserId;
	@Column(name = "rank")
	private Integer rank;
	@Column(name = "displayName")
	private String displayName;
	@Column(name = "profileUrl")
	private String profileUrl;
	@Column(name = "imageUrl")
	private String imageUrl;
	@Column(name = "accessToken")
	private String accessToken;
	@Column(name = "secret")
	private String secret;
	@Column(name = "refreshToken")
	private String refreshToken;
	@Column(name = "expireTime")
	private Long expireTime;
}