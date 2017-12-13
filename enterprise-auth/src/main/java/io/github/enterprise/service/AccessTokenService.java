package io.github.enterprise.service;

import io.github.enterprise.model.User;

/**
 * Access Token 相关 
 * 
 * Created by Sheldon on 2017年12月13日
 *
 */
public interface AccessTokenService {

	String generateAccessToken(User user);
}
