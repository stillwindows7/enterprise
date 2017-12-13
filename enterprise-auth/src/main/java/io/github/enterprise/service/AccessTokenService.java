package io.github.enterprise.service;

import io.github.enterprise.model.User;

import java.util.Optional;

/**
 * Access Token 相关 
 * 
 * Created by Sheldon on 2017年12月13日
 *
 */
public interface AccessTokenService {

	/**
	 * 生成 access token 方法
	 *
	 * @param user
	 * @return
	 */
	String generateAccessToken(User user);

	/**
	 * 查询该 access token 是否还是生效
	 *
	 * @param accessToken
	 * @return
	 */
	boolean check(String accessToken);

	/**
	 * 根据该 access token 返回 User 对象
     *
	 * @param accessToken
	 * @return
	 */
	Optional<User> findByAccessToken(String accessToken);
}
