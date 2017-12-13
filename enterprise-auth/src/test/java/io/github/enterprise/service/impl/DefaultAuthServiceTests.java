package io.github.enterprise.service.impl;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import io.github.enterprise.model.User;
import io.github.enterprise.repository.UserRepository;
import io.github.enterprise.service.AuthService;
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

import java.util.Optional;

/**
 * Created by sheldon on 2017/12/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
@DatabaseSetup("/META-INF/dbtest/default_auth_service_sample_data.xml")
public class DefaultAuthServiceTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
        this.userRepository.deleteAll();
    }

    @Test
    public void testLogin() {
        Optional<User> userOptional = this.authService.login("sheldonchen", "123456Ab");
        Assert.assertTrue("user must be exist", userOptional.isPresent());
    }

}
