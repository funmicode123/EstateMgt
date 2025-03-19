package com.funmicode.service;

import com.funmicode.data.model.User;
import com.funmicode.data.model.UserRole;
import com.funmicode.data.repository.UserRepository;
import com.funmicode.dto.request.CreateSignupRequest;
import com.funmicode.dto.request.LoginRequest;
import com.funmicode.dto.response.CreateSignupResponse;
import com.funmicode.dto.response.LoginResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    private UserRole role;
//    @BeforeEach
//    void setUp() {
//        userRepository.deleteAll();
//    }

    @Test
    public void testUserCanCreateAccount() {
        CreateSignupRequest request = new CreateSignupRequest();
        request.setEmail("funmi@gmail.com");
        request.setPassword("201226");
        request.setRole(String.valueOf(UserRole.RESIDENT));

        CreateSignupResponse response = userService.signup(request);

        assertThat(response).isNotNull();
        assertThat(response.getMessage()).isEqualTo("User created successfully as " + request.getRole());

        User savedUser = userRepository.findByEmail(request.getEmail()).orElse(null);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("funmi@gmail.com");
    }

    @Test
    public void testUserCannotSignUpTwice() {
        CreateSignupRequest request = new CreateSignupRequest();
        request.setEmail("funmi@gmail.com");
        request.setPassword("201226");
        request.setRole(UserRole.RESIDENT.name());

        CreateSignupResponse firstResponse = userService.signup(request);

        assertNotNull(firstResponse);
        assertThat(firstResponse.getMessage()).isEqualTo("User created successfully as " + request.getRole());

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> userService.signup(request));

        assertThat(exception.getMessage()).isEqualTo("User with this email already exists");
    }

    @Test
    public void testUserCanLogin_AfterSignup() {
        CreateSignupRequest signupRequest = new CreateSignupRequest();
        signupRequest.setEmail("funmi@gmail.com");
        signupRequest.setPassword("201226");
        signupRequest.setRole(String.valueOf(UserRole.RESIDENT));

        userService.signup(signupRequest);

        LoginRequest request = new LoginRequest();
        request.setEmail("funmi@gmail.com");
        request.setPassword("201226");

        LoginResponse loggedInUser = userService.login(request);

        assertNotNull(loggedInUser);
        assertEquals("Login successfully", loggedInUser.getMessage());

    }

    @Test
    public void testUserCanCreateAccountAsSecurity() {
        CreateSignupRequest request = new CreateSignupRequest();
        request.setEmail("jojo@gmail.com");
        request.setPassword("201226");
        request.setRole(String.valueOf(UserRole.SECURITY));

        CreateSignupResponse response = userService.signup(request);

        assertThat(response).isNotNull();
        assertThat(response.getMessage()).isEqualTo("User created successfully as " + request.getRole());

        User savedUser = userRepository.findByEmail(request.getEmail()).orElse(null);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("jojo@gmail.com");
    }
}
