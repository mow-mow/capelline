package info.caprese.capelline.service.spec;

import org.springframework.social.connect.UserProfile;

import info.caprese.capelline.entity.User;

public interface SignupService {

	User createUser(UserProfile userProfile);

}
