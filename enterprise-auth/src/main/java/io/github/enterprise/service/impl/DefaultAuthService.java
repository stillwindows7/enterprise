/**
 * 
 */
package io.github.enterprise.service.impl;

import java.util.Optional;

import io.github.enterprise.repository.UserRepository;
import io.github.enterprise.utils.security.SecurityUtils;
import jdk.nashorn.internal.runtime.options.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.github.enterprise.model.LocalAuth;
import io.github.enterprise.model.User;
import io.github.enterprise.repository.LocalAuthRepository;
import io.github.enterprise.service.AuthService;

/**
 * Created by Sheldon on 2017年12月12日
 *
 */
@Service
public class DefaultAuthService implements AuthService {

	@Value("${password.key.str:secret}")
	private String passwordKeyStr;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LocalAuthRepository localAuthRepository;
	
	@Override
	public Optional<User> login(String username, String password) {
		Optional<LocalAuth> localAuthOptional = this.localAuthRepository.findByUsernameAndPassword(username, password);
		Optional<User> userOptional = Optional.empty();
		if (localAuthOptional.isPresent()) {
			LocalAuth localAuth = localAuthOptional.get();
			userOptional = Optional.of(localAuth.getUser());
		}
		return userOptional;
	}

	@Override
	public Optional<User> register(String username, String password) {
	    Optional<User> userOptional = Optional.empty();
		Optional<LocalAuth> localAuthOptional = this.localAuthRepository.findByUsername(username);
		if (!localAuthOptional.isPresent()) {
		    User user = new User(username, new LocalAuth(username, SecurityUtils.sha512(String.format("%s%s", password, this.passwordKeyStr))));
		    user = this.userRepository.save(user);
		    userOptional = Optional.of(user);
		}
		return userOptional;
	}

}
