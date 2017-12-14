package io.github.enterprise.service.impl;

import io.github.enterprise.repository.UserRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import io.github.enterprise.model.User;
import io.github.enterprise.service.AccessTokenService;
import io.github.enterprise.utils.security.SecurityUtils;
import org.springframework.util.StringValueResolver;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sheldon on 2017年12月13日
 */
@Service
public class DefaultAccessTokenService implements AccessTokenService {

    @Value("${secret.str:secret}")
    private String secretStr;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserRepository userRepository;

    /**
     * access token 生成方法：
     * <p>
     * 1: user id
     * 2: access token 生成时间
     * 3: 系统固定的一个随机字符串
     * <p>
     * 使用以上 3 个部分拼接，最终得到的一个 hash 值
     */
    @Override
    public String generateAccessToken(User user) {
        final String accessToken = String.format("%s:%d:%s", SecurityUtils.sha512(user.getId()), System.currentTimeMillis(), SecurityUtils.sha512(this.secretStr));
        this.stringRedisTemplate.opsForValue().set(accessToken, user.getId(),30, TimeUnit.MINUTES);
        return accessToken;
    }

    /**
     * 查询该 access token 是否还是生效
     *
     * @param accessToken 需要查询的 access token
     * @return 如果 access token 有效，则返回 true；如果 access token 失效，则返回 false
     */
    @Override
    public boolean check(String accessToken) {
        String userId = this.stringRedisTemplate.opsForValue().get(accessToken);

        boolean result = false;
        if (StringUtils.isNotBlank(userId)) {
            result = true;
        }

        return result;
    }

    /**
     * 根据该 access token 返回 User 对象
     *
     * @param accessToken 需要查询的 access token
     * @return 如果 access token 有效，则返回对应的对象；否则返回空对象
     */
    @Override
    public Optional<User> findByAccessToken(String accessToken) {
        Optional<User> userOptional = Optional.empty();

        if (this.check(accessToken)) {
        	String userId = this.stringRedisTemplate.opsForValue().get(accessToken);
        	User user = this.userRepository.findOne(userId);
            userOptional = Optional.of(user);
        }

        return userOptional;
    }
}
