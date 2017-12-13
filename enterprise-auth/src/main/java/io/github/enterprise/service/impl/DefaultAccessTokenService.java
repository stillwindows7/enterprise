package io.github.enterprise.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.github.enterprise.model.User;
import io.github.enterprise.service.AccessTokenService;
import io.github.enterprise.utils.security.SecurityUtils;

/**
 * Created by Sheldon on 2017年12月13日
 *
 */
@Service
public class DefaultAccessTokenService implements AccessTokenService {

	@Value("${secret.str:secret}")
	private String secretStr;
	
	/**
	 * access token 生成方法：
	 * 
	 * 1: user id
	 * 2: access token 生成时间
	 * 3: 系统固定的一个随机字符串
	 * 
	 * 使用以上 3 个部分拼接，最终得到的一个 hash 值
	 */
	@Override
	public String generateAccessToken(User user) {
		final String accessToken = SecurityUtils.sha512(String.format("%s:%d:%s", user.getId(), System.currentTimeMillis(), this.secretStr));
		return accessToken;
	}

}
