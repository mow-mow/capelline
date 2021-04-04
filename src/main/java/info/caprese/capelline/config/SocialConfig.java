package info.caprese.capelline.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.twitter.api.Twitter;

import info.caprese.capelline.service.impl.ConnectionSignUpImpl;
import info.caprese.capelline.service.impl.SignupServiceImpl;
import info.caprese.capelline.service.spec.UserContext;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {

	private final DataSource dataSource;
	private final SignupServiceImpl signupService;
	private final UserContext userContext;

	public SocialConfig(DataSource dataSource, SignupServiceImpl signupService, UserContext userContext) {
		this.dataSource = dataSource;
		this.signupService = signupService;
		this.userContext = userContext;
	}

	@Override
	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
		JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource,
				connectionFactoryLocator, Encryptors.noOpText()); // TODO change Encryptor
		repository.setConnectionSignUp(new ConnectionSignUpImpl(signupService));
		return repository;
	}

	@Bean
	@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
	public Twitter twitter(UsersConnectionRepository usersConnectionRepository) {
		return usersConnectionRepository.createConnectionRepository(userContext.getUserId().get())
				.findPrimaryConnection(Twitter.class).getApi();
	}

	@Override
	public UserIdSource getUserIdSource() {
		return new AuthenticationNameUserIdSource();
	}
	@Override
	public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer, Environment environment) {

		connectionFactoryConfigurer.addConnectionFactory(new TwitterConnectionFactory(
				environment.getProperty("spring.social.twitter.consumer-id"),
				environment.getProperty("spring.social.twitter.consumer-secret")));

	}

}