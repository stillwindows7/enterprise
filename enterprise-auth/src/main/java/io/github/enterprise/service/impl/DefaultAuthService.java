/**
 * 
 */
package io.github.enterprise.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
		// TODO Auto-generated method stub
		return null;
	}

}
