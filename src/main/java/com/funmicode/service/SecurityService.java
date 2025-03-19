package com.funmicode.service;

import com.funmicode.data.model.VisitingLog;
import com.funmicode.dto.request.OtpValidRequest;
import com.funmicode.dto.response.OtpValidResponse;

import java.util.List;

public interface SecurityService {
    OtpValidResponse validateOtp(OtpValidRequest otpValidRequest);
    List<VisitingLog> getVisitingLogs();
}
