package io.github.enterprise.service.impl;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import io.github.enterprise.AuthServerApplication;
import io.github.enterprise.model.LocalAuth;
import io.github.enterprise.model.User;
import io.github.enterprise.repository.LocalAuthRepository;
import io.github.enterprise.repository.UserRepository;
import io.github.enterprise.service.AccessTokenService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.Optional;

/**
 * Created by Sheldon on 2017年12月13日
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DatabaseSetup("/META-INF/dbtest/default_access_token_sample_data.xml")
public class DefaultAccessTokenServiceTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocalAuthRepository localAuthRepository;

    @Autowired
    private AccessTokenService accessTokenService;

    private String accessToken;

    @Before
    public void setUp() {
        Optional<LocalAuth> sheldonAuthOptional = this.localAuthRepository
                .findByUsernameAndPassword("sheldonchen", "123456Ab");
        Assert.assertTrue("user must be exist", sheldonAuthOptional.isPresent());

        LocalAuth sheldonAuth = sheldonAuthOptional.get();
        User user = sheldonAuth.getUser();
        this.accessToken = this.accessTokenService.generateAccessToken(user);
    }

    @After
    public void tearDown() {
        this.userRepository.deleteAll();
    }

    /**
     * 测试生成 access token 方法
     */
    @Test
    public void testGenerateAccessToken() {
        Assert.assertNotNull("access token cannot be empty", this.accessToken);
    }

    /**
     * 测试 access token 是否存在，此方法测试存在的 access token
     * <p>
     * 应该返回 true
     */
    @Test
    public void testCheckSuccess() {
        boolean result = this.accessTokenService.check(accessToken);
        Assert.assertTrue("this access token exist", result);
    }

    /**
     * 测试 access token 是否存在，此方法测试不存在的 access token
     * <p>
     * 应该返回 false
     */
    @Test
    public void testCheckFail() {
        boolean result = this.accessTokenService.check("wrong access token");
        Assert.assertFalse("this access token is not exist", result);
    }

    /**
     * 测试 根据 access token 返回 User 对象方法，此方法测试正确的 access token
     * <p>
     * 应该返回正确的 Optional 对象
     */
    public void testFindbyAccessTokenSuccess() {
        Optional<User> userOptional = this.accessTokenService.findByAccessToken(this.accessToken);
        Assert.assertTrue("user must exist", userOptional.isPresent());

        User user = userOptional.get();
        Assert.assertEquals("user id must equals 1", "1", user.getId());
    }

    /**
     * 测试 根据 access token 返回 User 对象方法，此方法测试错误的 access token
     * <p>
     * 应该返回空的 Optional 对象
     */
    public void testFindByAccessTokenFail() {
        Optional<User> userOptional = this.accessTokenService.findByAccessToken("wrong access token");
        Assert.assertFalse("user must not exist", userOptional.isPresent());
    }

}
