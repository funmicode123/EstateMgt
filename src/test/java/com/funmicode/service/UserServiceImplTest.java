package com.funmicode.service;

import com.funmicode.data.model.AccountStatus;
import com.funmicode.data.model.Apartment;
import com.funmicode.data.model.User;
import com.funmicode.data.model.UserRole;
import com.funmicode.data.repository.ApartmentRepository;
import com.funmicode.data.repository.UserRepository;
import com.funmicode.dto.request.ApartmentRequest;
import com.funmicode.dto.request.CreateSignupRequest;
import com.funmicode.dto.response.CreateSignupResponse;
import com.funmicode.utils.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ApartmentRepository apartmentRepository;
    @Mock
    private Mapper mapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserServiceImpl userService;

    private CreateSignupRequest residentRequest;

    @BeforeEach
    void setUp() {
        residentRequest = new CreateSignupRequest();
        residentRequest.setEmail("resident@example.com");
        residentRequest.setPassword("password123");
        residentRequest.setFirstName("John");
        residentRequest.setLastName("Doe");
        residentRequest.setRole("RESIDENT");
        
        ApartmentRequest aptReq = new ApartmentRequest();
        aptReq.setHouseNo("101");
        aptReq.setBlock("A");
        aptReq.setStreetName("Main St");
        residentRequest.setApartment(aptReq);
    }

    @Test
    void testSignupResident_Success() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        User user = new User();
        user.setPassword("password123");
        when(mapper.mapUserRequest(any())).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_password");
        
        when(apartmentRepository.findByHouseNoAndBlockAndStreetName(anyString(), anyString(), anyString()))
                .thenReturn(Optional.empty());
        when(apartmentRepository.save(any())).thenReturn(new Apartment());

        // Act
        CreateSignupResponse response = userService.signup(residentRequest);

        // Assert
        assertThat(response.isSuccess()).isTrue();
        assertThat(user.getStatus()).isEqualTo(AccountStatus.PENDING);
        assertThat(user.getPassword()).isEqualTo("hashed_password");
        verify(userRepository).save(user);
    }

    @Test
    void testCreateSecurity_Success() {
        // Arrange
        CreateSignupRequest securityReq = new CreateSignupRequest();
        securityReq.setEmail("security@example.com");
        securityReq.setRole("SECURITY");
        
        User user = new User();
        when(mapper.mapUserRequest(any())).thenReturn(user);
        when(passwordEncoder.encode(any())).thenReturn("hashed");

        // Act
        CreateSignupResponse response = userService.createSecurity(securityReq);

        // Assert
        assertThat(response.isSuccess()).isTrue();
        assertThat(user.getRole()).isEqualTo(UserRole.SECURITY);
        assertThat(user.getStatus()).isEqualTo(AccountStatus.ACTIVE);
    }

    @Test
    void testApproveResident_Success() {
        // Arrange
        User user = new User();
        user.setRole(UserRole.RESIDENT);
        user.setStatus(AccountStatus.PENDING);
        when(userRepository.findByEmail("resident@example.com")).thenReturn(Optional.of(user));

        // Act
        String result = userService.approveResident("resident@example.com");

        // Assert
        assertThat(result).contains("successfully");
        assertThat(user.getStatus()).isEqualTo(AccountStatus.ACTIVE);
        verify(userRepository).save(user);
    }
}
