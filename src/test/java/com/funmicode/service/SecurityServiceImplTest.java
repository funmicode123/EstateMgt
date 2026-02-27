package com.funmicode.service;

import com.funmicode.data.model.OtpStatus;
import com.funmicode.data.model.VisitingLog;
import com.funmicode.data.repository.VisitingLogRepository;
import com.funmicode.dto.request.OtpValidRequest;
import com.funmicode.dto.response.OtpValidResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityServiceImplTest {

    @Mock
    private VisitingLogRepository visitingLogRepository;

    @InjectMocks
    private SecurityServiceImpl securityService;

    private OtpValidRequest validRequest;
    private VisitingLog visitingLog;

    @BeforeEach
    void setUp() {
        validRequest = new OtpValidRequest();
        validRequest.setOtp("123456");
        validRequest.setSecurityEmail("guard@example.com");

        visitingLog = new VisitingLog();
        visitingLog.setOtp("123456");
        visitingLog.setOtpStatus(OtpStatus.PENDING);
        visitingLog.setOtpExpiredTime(LocalDateTime.now().plusMinutes(5));
    }

    @Test
    void testValidateOtp_Success() {
        // Arrange
        when(visitingLogRepository.findByOtp("123456")).thenReturn(visitingLog);

        // Act
        OtpValidResponse response = securityService.validateOtp(validRequest);

        // Assert
        assertThat(response.isSuccess()).isTrue();
        assertThat(visitingLog.getOtpStatus()).isEqualTo(OtpStatus.USED);
        assertThat(visitingLog.getSecurityGuardId()).isEqualTo("guard@example.com");
        assertThat(visitingLog.getCheckInTime()).isNotNull();
        verify(visitingLogRepository).save(visitingLog);
    }

    @Test
    void testValidateOtp_InvalidOtp_ReturnsFailure() {
        // Arrange
        when(visitingLogRepository.findByOtp("123456")).thenReturn(null);

        // Act
        OtpValidResponse response = securityService.validateOtp(validRequest);

        // Assert
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).contains("Invalid OTP");
    }

    @Test
    void testValidateOtp_ExpiredOtp_ReturnsFailure() {
        // Arrange
        visitingLog.setOtpExpiredTime(LocalDateTime.now().minusMinutes(1));
        when(visitingLogRepository.findByOtp("123456")).thenReturn(visitingLog);

        // Act
        OtpValidResponse response = securityService.validateOtp(validRequest);

        // Assert
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).contains("expired");
        assertThat(visitingLog.getOtpStatus()).isEqualTo(OtpStatus.EXPIRED);
        verify(visitingLogRepository).save(visitingLog);
    }
}
