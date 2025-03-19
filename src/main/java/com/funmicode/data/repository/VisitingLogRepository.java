package com.funmicode.data.repository;

import com.funmicode.data.model.VisitingLog;
import com.funmicode.data.model.OtpStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

@Repository
public interface VisitingLogRepository extends MongoRepository<VisitingLog, String> {

    VisitingLog findByOtp(String otp);

    Optional<VisitingLog> findByVisitorNameAndOtpStatus(String visitorName, OtpStatus otpStatus);

    Optional<VisitingLog> findByVisitorName(String visitorName);

    void deleteByOtpExpiredTimeBefore(LocalDateTime expiryTime);

    List<VisitingLog> findByOtpStatusAndOtpExpiredTimeBefore(OtpStatus otpStatus, LocalDateTime expiryTime);
}
