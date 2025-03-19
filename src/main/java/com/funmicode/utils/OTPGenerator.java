package com.funmicode.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class OTPGenerator {
    private static final Random random = new Random();

    public String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}
