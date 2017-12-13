package io.github.enterprise.service.impl;

import java.util.Optional;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import io.github.enterprise.model.LocalAuth;
import io.github.enterprise.model.User;
import io.github.enterprise.repository.LocalAuthRepository;
import io.github.enterprise.repository.UserRepository;
import io.github.enterprise.service.AccessTokenService;

/**
 * Created by Sheldon on 2017年12月13日
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
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
	
	@Before
	public void setUp() {
		
	}
	
	@After
	public void tearDown() {
		this.userRepository.deleteAll();
	}
	
	@Test
	public void testGenerateAccessToken() {
		Optional<LocalAuth> sheldonAuthOptional = this.localAuthRepository
                .findByUsernameAndPassword("sheldonchen", "123456Ab");
		Assert.assertTrue("user must be exist", sheldonAuthOptional.isPresent());
		
		LocalAuth sheldonAuth = sheldonAuthOptional.get();
		User user = sheldonAuth.getUser();
		String accessToken = this.accessTokenService.generateAccessToken(user);
		Assert.assertNotNull("access token cannot be empty", accessToken);
	}
}
