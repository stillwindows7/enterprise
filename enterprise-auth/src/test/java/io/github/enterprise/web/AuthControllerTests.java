package io.github.enterprise.web;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import io.github.enterprise.utils.common.JsonResult;
import io.github.enterprise.web.req.LocalAuthVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * Created by sheldon on 2017/12/13.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DatabaseSetup("/META-INF/dbtest/local_auth_sample_data.xml")
public class AuthControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * 测试成功登录方法
     */
    @Test
    public void testLoginSuccess() {
        LocalAuthVO localAuthVO = new LocalAuthVO();
        localAuthVO.setUsername("sheldonchen");
        localAuthVO.setPassword("123456Ab");
        JsonResult<String> successResult = this.restTemplate.postForObject("/auth/login", localAuthVO, JsonResult.class);

        Assert.assertEquals("login success, it will return 200", "200", successResult.getResult());
        Assert.assertNotNull("access token cannot be null", successResult.getData());
    }

    /**
     * 测试失败登录方法
     */
    @Test
    public void testLoginFail() {
        LocalAuthVO localAuthVO = new LocalAuthVO();
        localAuthVO.setUsername("wrong username");
        localAuthVO.setPassword("wrong password");

        JsonResult<String> successResult = this.restTemplate.postForObject("/auth/login", localAuthVO, JsonResult.class);

        Assert.assertNotEquals("login success, it will be not equals 200", "200", successResult.getResult());
        Assert.assertNull("access token must be null", successResult.getData());
    }

}
