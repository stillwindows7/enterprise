/**
 * 
 */
package io.github.enterprise.service;

import java.util.Optional;

import io.github.enterprise.model.User;

/**
 * 用户验证相关接口
 * 
 * Created by Sheldon on 2017年12月11日
 *
 */
public interface AuthService {

	/**
	 * 用户登录
	 * 
	 * @param username 用户名
	 * @param password 密码
	 * @return 返回用户实体
	 */
	Optional<User> login(String username, String password);
	
	/**
	 * 用户注册
	 * 
	 * @param username 用户名
	 * @param password 密码
	 * @return 返回用户实体
	 */
	Optional<User> register(String username, String password);
}
