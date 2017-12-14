package io.github.enterprise.web;

import io.github.enterprise.model.LocalAuth;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import io.github.enterprise.model.User;
import io.github.enterprise.repository.UserRepository;
import io.github.enterprise.utils.common.JsonResult;
import io.github.enterprise.web.request.LocalAuthVO;
import io.github.enterprise.web.response.UserVO;

import java.net.URI;
import java.net.URISyntaxException;

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
	private UserRepository userRepository;
	
    @Autowired
    private TestRestTemplate restTemplate;

    private JsonResult<String> successResult;
    
    @SuppressWarnings("unchecked")
	@Before
    public void setUp() {
    	LocalAuthVO localAuthVO = new LocalAuthVO();
        localAuthVO.setUsername("sheldonchen");
        localAuthVO.setPassword("123456Ab");
        this.successResult = this.restTemplate.postForObject("/auth/login", localAuthVO, JsonResult.class);
    }
    
    @After
    public void tearDown() {
    	this.userRepository.deleteAll();
    }
    
    /**
     * 测试成功登录方法
     */
	@Test
    public void testLoginSuccess() {
        Assert.assertEquals("login success, it will return 200", "200", this.successResult.getResult());
        Assert.assertNotNull("access token cannot be null", this.successResult.getData());
    }

    /**
     * 测试失败登录方法
     */
    @SuppressWarnings("unchecked")
	@Test
    public void testLoginFail() {
        LocalAuthVO localAuthVO = new LocalAuthVO();
        localAuthVO.setUsername("wrong username");
        localAuthVO.setPassword("wrong password");
        JsonResult<String> failResult = this.restTemplate.postForObject("/auth/login", localAuthVO, JsonResult.class);
        Assert.assertNotEquals("login fail, it will be not equals 200", "200", failResult.getResult());
        Assert.assertNull("access token must be null", failResult.getData());
    }
    
    /**
     * 测试查询 access token 是否过期方法，还在有效期
     */
    @SuppressWarnings("unchecked")
	@Test
    public void testCheckAccessTokenSuccess() {
    	String url = String.format("/auth/accesstoken/check/%s", this.successResult.getData());
    	JsonResult<Boolean> jsonResult = this.restTemplate.getForObject(url, JsonResult.class);
        Assert.assertEquals("check access token success, it will return 200", "200", jsonResult.getResult());
        Assert.assertTrue("access token can be true", jsonResult.getData());
    }
    
    /**
     * 测试查询 access token 是否过期方法，不在有效期或者不存在的 access token
     */
    @SuppressWarnings("unchecked")
    @Test
	public void testCheckAccessTokenFail() {
    	String url = String.format("/auth/accesstoken/check/%s", "wrong_access_token");
    	JsonResult<Boolean> jsonResult = this.restTemplate.getForObject(url, JsonResult.class);
    	Assert.assertEquals("check access token fail,it will return 200", "200", jsonResult.getResult());
        Assert.assertFalse("access token can be false", jsonResult.getData());
    }

    /**
     * 测试 根据 access token 查询 user 方法，正确的 access token 情况下
     * 
     * 应正确返回 user
     */
	@Test
    public void testQueryUserByAccessTokenSuccess() {
    	String url = String.format("/auth/accesstoken/query/%s", this.successResult.getData());
        JsonResult<UserVO> jsonResult = this.restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<JsonResult<UserVO>>() {}).getBody();
        Assert.assertEquals("query user by access token success, it will return 200", "200", jsonResult.getResult());
        Assert.assertNotNull("user cannot be null", jsonResult.getData());
        
        UserVO userVO = jsonResult.getData();
        Assert.assertEquals("user id will equals 1", "1", userVO.getId());
    }
    
    /**
     * 测试 根据 access token 查询 user 方法，错误的 access token 情况下
     * 
     * 应返回错误信息
     */
    @SuppressWarnings("unchecked")
    @Test
	public void testQueryUserByAccessTokenFail() {
    	String url = String.format("/auth/accesstoken/query/%s", "wrong_access_token");
    	JsonResult<UserVO> jsonResult = this.restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<JsonResult<UserVO>>() {}).getBody();
    	Assert.assertNotEquals("query user by access token fail, it will not return 200 again", "200", jsonResult.getResult());
        Assert.assertNull("access token can be false", jsonResult.getData());
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        LocalAuthVO localAuthVO = new LocalAuthVO();
        localAuthVO.setUsername("newuser");
        localAuthVO.setPassword("newpass");

        RequestEntity<LocalAuthVO> requestEntity = new RequestEntity<>(localAuthVO, HttpMethod.POST, new URI("/auth/register"));
        JsonResult<UserVO> jsonResult = this.restTemplate.exchange("/auth/register", HttpMethod.POST, requestEntity, new ParameterizedTypeReference<JsonResult<UserVO>>() {}).getBody();
        Assert.assertEquals("register success, it will return 200", "200", jsonResult.getResult());
        Assert.assertNotNull("user cannot be null", jsonResult.getData());

        UserVO userVO = jsonResult.getData();
        Assert.assertNotNull("user id will not be null", userVO.getId());
    }

    @Test
    public void testRegisterFail() throws Exception {
        LocalAuthVO localAuthVO = new LocalAuthVO();
        localAuthVO.setUsername("sheldonchen");
        localAuthVO.setPassword("123456Ab");

        RequestEntity<LocalAuthVO> requestEntity = new RequestEntity<>(localAuthVO, HttpMethod.POST, new URI("/auth/register"));
        JsonResult<UserVO> jsonResult = this.restTemplate.exchange("/auth/register", HttpMethod.POST, requestEntity, new ParameterizedTypeReference<JsonResult<UserVO>>() {}).getBody();
        Assert.assertNotEquals("register fail", "200", jsonResult.getResult());
        Assert.assertNull("user will be null", jsonResult.getData());
    }

}
