package com.funmicode.service;

import com.funmicode.data.model.*;
import com.funmicode.data.repository.ExitPassRepository;
import com.funmicode.data.repository.UserRepository;
import com.funmicode.data.repository.VisitingLogRepository;
import com.funmicode.dto.request.ExitPassRequest;
import com.funmicode.dto.request.OtpRequest;
import com.funmicode.dto.response.ExitPassResponse;
import com.funmicode.dto.response.OtpResponse;
import com.funmicode.utils.Mapper;
import com.funmicode.utils.OTPGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResidentServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private OTPGenerator otpGenerator;
    @Mock
    private Mapper mapper;
    @Mock
    private VisitingLogRepository visitingLogRepository;
    @Mock
    private ExitPassRepository exitPassRepository;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private ResidentServiceImpl residentService;

    private User resident;
    private OtpRequest otpRequest;

    @BeforeEach
    void setUp() {
        resident = new User();
        resident.setEmail("res@example.com");
        resident.setRole(UserRole.RESIDENT);
        
        Apartment apt = new Apartment();
        apt.setHouseNo("12");
        apt.setBlock("B");
        apt.setStreetName("Oak Ave");
        resident.setApartment(apt);

        otpRequest = new OtpRequest();
        otpRequest.setResidentEmail("res@example.com");
        otpRequest.setVisitorName("Visitor One");
    }

    @Test
    void testGenerateOtpForVisitor_Success() {
        when(userRepository.findByEmail("res@example.com")).thenReturn(Optional.of(resident));
        when(otpGenerator.generateOtp()).thenReturn("123456");
        
        VisitingLog log = new VisitingLog();
        when(mapper.mapOtpRequest(any())).thenReturn(log);

        OtpResponse response = residentService.generateOtpForVisitor(otpRequest);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getOtp()).isEqualTo("123456");
        assertThat(log.getApartmentDetails()).contains("House 12, Block B, Oak Ave");
        verify(visitingLogRepository).save(log);
    }

    @Test
    void testGenerateOtpForVisitor_NotResident_ThrowsException() {
        resident.setRole(UserRole.ADMIN);
        when(userRepository.findByEmail("res@example.com")).thenReturn(Optional.of(resident));

        try {
            residentService.generateOtpForVisitor(otpRequest);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).contains("Only residents");
        }
    }

    @Test
    void testGenerateExitPass_Success() {
        ExitPassRequest req = new ExitPassRequest();
        req.setVisitorName("Visitor One");
        
        VisitingLog log = new VisitingLog();
        log.setVisitorName("Visitor One");
        log.setOtpStatus(OtpStatus.USED);
        log.setOtpExpiredTime(LocalDateTime.now().plusHours(1));
        
        when(visitingLogRepository.findByVisitorNameAndOtpStatus("Visitor One", OtpStatus.USED))
                .thenReturn(Optional.of(log));
        when(mapper.mapExitPassRequest(any())).thenReturn(new ExitPass());

        ExitPassResponse response = residentService.generateExitPass(req);

        assertThat(response.isSuccess()).isTrue();
        verify(exitPassRepository).save(any());
    }
}