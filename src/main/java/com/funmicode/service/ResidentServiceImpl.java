package com.funmicode.service;

import com.funmicode.data.model.*;
import com.funmicode.data.repository.ExitPassRepository;
import com.funmicode.data.repository.UserRepository;
import com.funmicode.data.repository.VisitingLogRepository;
import com.funmicode.dto.request.ExitPassRequest;
import com.funmicode.dto.request.ExitRequest;
import com.funmicode.dto.request.OtpRequest;
import com.funmicode.dto.response.ExitPassResponse;
import com.funmicode.dto.response.OtpResponse;
import com.funmicode.utils.Mapper;
import com.funmicode.utils.OTPGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ResidentServiceImpl implements ResidentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OTPGenerator otpGenerator;
    @Autowired
    private Mapper mapper;
    @Autowired
    private VisitingLogRepository visitingLogRepository;
    @Autowired
    private ExitPassRepository exitPassRepository;
    @Autowired
    private EmailService emailService;

    @Override
    public OtpResponse generateOtpForVisitor(OtpRequest otpRequest) {

        Optional<User> resident = userRepository.findByEmail(otpRequest.getResidentEmail());

        if (resident.isEmpty() || resident.get().getRole() != UserRole.RESIDENT) {
            throw new IllegalArgumentException("Only residents can generate OTPs.");
        }

        String otp = otpGenerator.generateOtp();

        VisitingLog log = mapper.mapOtpRequest(otpRequest);
        log.setOtp(otp);
        log.setOtpCreatedTime(LocalDateTime.now());
        log.setOtpExpiredTime(LocalDateTime.now().plusMinutes(5));
        log.setOtpStatus(OtpStatus.PENDING);

        visitingLogRepository.save(log);

//        emailService.sendOtpEmail(resident.get().getEmail(), otp);

        return new OtpResponse(otp,"OTP has been sent to your registered email.", log.getOtpExpiredTime(), true);

    }

    public ExitPassResponse generateExitPass(ExitPassRequest exitPassRequest) {
        Optional<VisitingLog> visitorLog = visitingLogRepository.findByVisitorNameAndOtpStatus(
                exitPassRequest.getVisitorName(), OtpStatus.USED);

        if (visitorLog.isEmpty()) {
            return new ExitPassResponse("No valid entry record found for visitor: " + exitPassRequest.getVisitorName(), LocalDateTime.now(), false);
        }

        if (visitorLog.get().getOtpExpiredTime().isBefore(LocalDateTime.now())) {
            return new ExitPassResponse("OTP has expired. Please request a new one.", LocalDateTime.now(), false);
        }

        VisitingLog log = visitorLog.get();

        ExitPass exitPass = mapper.mapExitPassRequest(exitPassRequest);

        exitPass.setResidentEmail(log.getResidentEmail());
        LocalDateTime exitTime = LocalDateTime.now();
        exitPass.setExitTime(exitTime);

        exitPassRepository.save(exitPass);

        return new ExitPassResponse("Exit pass generated successfully for visitor: " + log.getVisitorName(), LocalDateTime.now(), true);
    }

}
