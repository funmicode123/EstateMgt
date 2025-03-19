package com.funmicode.service;
import org.springframework.scheduling.annotation.Scheduled;
import com.funmicode.data.repository.VisitingLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OtpCleanupService {

    @Autowired
    private VisitingLogRepository visitingLogRepository;

    @Scheduled(fixedRate = 3600000)
    public void removeExpiredOTPs() {
        LocalDateTime expiryTime = LocalDateTime.now().minusHours(2);
        visitingLogRepository.deleteByOtpExpiredTimeBefore(expiryTime);
    }
}

