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

    /**
     * 当用户名正确时，应返回正确的对象
     */
    @Test
    public void testFindByUsernameCorrect() {
        Optional<LocalAuth> localAuthOptional = this.localAuthRepository.findByUsername("sheldonchen");
        Assert.assertTrue("it should be exist", localAuthOptional.isPresent());

        LocalAuth localAuth = localAuthOptional.get();
        Assert.assertEquals("local auth id must be equals 1", "1", localAuth.getId());
    }

    /**
     * 当用户名不正确时，应返回空结果
     */
    @Test
    public void testFindByUsernameWrong() {
        Optional<LocalAuth> localAuthOptional = this.localAuthRepository.findByUsername("wrong user name");
        Assert.assertFalse("it should not be exist", localAuthOptional.isPresent());
    }

    /**
     * 当用户名和密码都正确时，应返回正确的对象
     */
    @Test
    public void testFindByUsernameAndPasswordCorrect() {
        Optional<LocalAuth> sheldonAuthOptional = this.localAuthRepository
                .findByUsernameAndPassword("sheldonchen", "123456Ab");
        Assert.assertTrue("it should be exist", sheldonAuthOptional.isPresent());

        LocalAuth sheldonAuth = sheldonAuthOptional.get();
        Assert.assertEquals("sheldon auth id must be equal 1", "1", sheldonAuth.getId());
    }

    /**
     * 当用户名或密码不正确时，应返回空结果
     */
    @Test
    public void testFindByUsernameAndPasswordFail() {
        Optional<LocalAuth> sheldonAuthOptional = this.localAuthRepository
                .findByUsernameAndPassword("wrong user name", "wrong password");
        Assert.assertFalse("it should not be exist", sheldonAuthOptional.isPresent());
    }

}
