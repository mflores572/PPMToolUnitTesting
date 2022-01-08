package com.loel.serviceTests;

import com.loel.domain.User;
import com.loel.repositories.UserRepository;
import com.loel.services.CustomUserDetailsService;
import com.loel.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @MockBean
    private UserRepository userRepository;

    /* Tests for UserService class */

    @Test
    public void testFindAllUsers() {
        /* Arrange */
        User testUser = new User((long) 1, "Test@test.com", "Test User", "password");
        User userTested = new User((long) 2, "Tested@test.com", "User Tested", "password");
        User testingUser = new User((long) 3, "Testing@test.com", "Testing User", "no-password");

        /* Act */
        when(userRepository.findAll()).thenReturn(Stream
                .of(testUser, userTested, testingUser).collect(Collectors.toList()));

        /* Confirm by Print AND Assert */
        Iterable<User> list = userService.findAll();
        for (User u : list) {
            System.out.println(u.getUsername());
        }

        Assertions.assertEquals("Test@test.com", testUser.getUsername());
        Assertions.assertEquals("User Tested", userTested.getFullName());
        Assertions.assertEquals("no-password", testingUser.getPassword());

    }

    @Test
    public void testSaveUser() {
        /* Arrange */
        String password = "no-password";
        User testUser = new User((long) 1, "Test@test.com", "Test User", password);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        /* Act */
        when(userRepository.save(testUser)).thenReturn(testUser);

        User myUser = userService.saveUser(testUser);

        /* Assert */
        Assertions.assertEquals("Test@test.com", myUser.getUsername());
        //asserting that password that gets saved to User is the encoded one and it matches the original string
        boolean isPasswordMatch = passwordEncoder.matches(password, myUser.getPassword());
        Assertions.assertEquals(true, isPasswordMatch);
    }

    /* Tests for CustomUserDetailsService class */

    @Test
    public void testLoadUserByUsername() {
        /* Arrange */
        String myUsername = "myuser@test.com";
        User myUser = new User((long) 1, myUsername, "Test User", "password");

        /* Act */
        when(userRepository.findByUsername(myUsername)).thenReturn(myUser);

        /* Assert */
        User userFromService = (User) userDetailsService.loadUserByUsername(myUsername);
        Assertions.assertEquals(userFromService, myUser);

    }

    @Test
    public void testLoadUserById() {
        /* Arrange */
        long myId = 1;
        User myUser = new User(myId, "myuser@test.com", "Test User", "password");

        /* Act */
        when(userRepository.getById(myId)).thenReturn(myUser);

        /* Assert */
        User userFromService = userDetailsService.loadUserById(myId);
        Assertions.assertEquals(userFromService, myUser);

    }
} //end of UserServiceTest.java