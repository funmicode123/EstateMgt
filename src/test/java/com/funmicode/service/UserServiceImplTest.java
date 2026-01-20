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

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

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
        assertEquals("Login successful", loggedInUser.getMessage());

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

    @Test
    public void testLazyMigration_LegacyUserLogin() {
        // 1. Manually create a "legacy" user with plain text password
        User legacyUser = new User();
        legacyUser.setEmail("legacy@gmail.com");
        legacyUser.setPassword("plaintext123"); // Note: Not hashed!
        legacyUser.setRole(UserRole.RESIDENT);
        userRepository.save(legacyUser);

        // 2. Login with plain text password
        LoginRequest request = new LoginRequest();
        request.setEmail("legacy@gmail.com");
        request.setPassword("plaintext123");

        LoginResponse response = userService.login(request);

        // 3. Verify Login Success
        assertThat(response.isSuccess()).isTrue();

        // 4. Verify Password is now HASHED in DB
        User migratedUser = userRepository.findByEmail("legacy@gmail.com").get();
        assertThat(migratedUser.getPassword()).isNotEqualTo("plaintext123");
        assertThat(migratedUser.getPassword()).startsWith("$2a$"); // BCrypt prefix
    }

    @org.springframework.boot.test.mock.mockito.MockBean
    private EmailService emailService;

    @Test
    public void testPasswordResetFlow() {
        // 1. Setup User
        CreateSignupRequest signup = new CreateSignupRequest();
        signup.setEmail("forgot@gmail.com");
        signup.setPassword("oldPass");
        signup.setRole("RESIDENT");
        userService.signup(signup);

        // 2. Forgot Password
        userService.forgotPassword("forgot@gmail.com");

        User userWithOtp = userRepository.findByEmail("forgot@gmail.com").get();
        assertThat(userWithOtp.getOtp()).isNotNull();
        String otp = userWithOtp.getOtp();

        // 3. Reset Password
        com.funmicode.dto.request.ResetPasswordRequest resetRequest = new com.funmicode.dto.request.ResetPasswordRequest();
        resetRequest.setEmail("forgot@gmail.com");
        resetRequest.setOtp(otp);
        resetRequest.setNewPassword("newPassSecure");

        String result = userService.resetPassword(resetRequest);
        assertThat(result).isEqualTo("Password reset successfully");

        // 4. Login with OLD password (should fail)
        LoginRequest loginOld = new LoginRequest();
        loginOld.setEmail("forgot@gmail.com");
        loginOld.setPassword("oldPass");
        Assertions.assertThrows(RuntimeException.class, () -> userService.login(loginOld));

        // 5. Login with NEW password (should succeed)
        LoginRequest loginNew = new LoginRequest();
        loginNew.setEmail("forgot@gmail.com");
        loginNew.setPassword("newPassSecure");
        LoginResponse loginResponse = userService.login(loginNew);
        assertThat(loginResponse.isSuccess()).isTrue();
    }
}
