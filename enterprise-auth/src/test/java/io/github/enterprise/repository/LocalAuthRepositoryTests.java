package io.github.enterprise.repository;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import io.github.enterprise.model.LocalAuth;
import io.github.enterprise.model.User;
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
 * Created by Sheldon on 2017/12/08
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DatabaseSetup("/META-INF/dbtest/local_auth_sample_data.xml")
public class LocalAuthRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocalAuthRepository localAuthRepository;

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
        this.userRepository.deleteAll();
    }

    @Test
    public void testFindByUsernameAndPassword() {
        Optional<LocalAuth> sheldonAuthOptional = this.localAuthRepository
                .findByUsernameAndPassword("sheldonchen", "123456Ab");
        Assert.assertTrue("it should be exist", sheldonAuthOptional.isPresent());
    }

}
