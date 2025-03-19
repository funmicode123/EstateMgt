package com.funmicode.service;

import com.funmicode.dto.request.ExitPassRequest;
import com.funmicode.dto.request.OtpRequest;
import com.funmicode.dto.response.ExitPassResponse;
import com.funmicode.dto.response.OtpResponse;

public interface ResidentService {
    OtpResponse generateOtpForVisitor(OtpRequest otpRequest);
    ExitPassResponse generateExitPass(ExitPassRequest exitPassRequest);
}
