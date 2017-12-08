package io.github.enterprise.repository;

import io.github.enterprise.model.LocalAuth;
import io.github.enterprise.model.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

/**
 * Created by Sheldon on 2017/12/08
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class LocalAuthRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocalAuthRepository localAuthRepository;

    @Before
    public void setUp() {
        User sheldon = new User();
        sheldon.setNickname("Sheldon Chen");

        LocalAuth sheldonAuth = new LocalAuth();
        sheldonAuth.setUsername("sheldonchen");
        sheldonAuth.setPassword("123456Ab");

        sheldon.setLocalAuth(sheldonAuth);

        this.userRepository.save(sheldon);
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
