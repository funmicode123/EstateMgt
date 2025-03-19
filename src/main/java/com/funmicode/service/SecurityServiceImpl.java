package com.funmicode.service;

import com.funmicode.data.model.VisitingLog;
import com.funmicode.data.model.OtpStatus;
import com.funmicode.data.repository.VisitingLogRepository;
import com.funmicode.dto.request.OtpValidRequest;
import com.funmicode.dto.response.OtpValidResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private VisitingLogRepository visitingLogRepository;

    public OtpValidResponse validateOtp(OtpValidRequest otpValidRequest) {
        OtpValidResponse response = new OtpValidResponse();
        VisitingLog log = visitingLogRepository.findByOtp(otpValidRequest.getOtp());

        if (log == null) {
            response.setSuccess(false);
            response.setMessage("Invalid OTP, please try again.");
            return response;
        }

        LocalDateTime now = LocalDateTime.now();

        if (log.getOtpStatus() == OtpStatus.USED) {
            response.setSuccess(false);
            response.setMessage("OTP has already been used");
            return response;
        }

        if (now.isAfter(log.getOtpExpiredTime())) {
            log.setOtpStatus(OtpStatus.EXPIRED);
            visitingLogRepository.save(log);
            response.setSuccess(false);
            response.setMessage("OTP has expired");
            return response;
        }

        log.setOtpStatus(OtpStatus.USED);
        visitingLogRepository.save(log);
        response.setSuccess(true);
        response.setMessage("OTP validated successfully");
        return response;
    }


    public List<VisitingLog> getVisitingLogs() {
        return visitingLogRepository.findAll();
    }
}
