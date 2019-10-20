package com.github.modsezam.nitritedemo.service;

import com.github.modsezam.nitritedemo.repository.NitriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledCleaningLogService {

    @Value("${log.cleaning-time}")
    private long logCleaningTime;

    @Autowired
    NitriteRepository nitriteRepository;

    @Scheduled(fixedDelay = 100_000L, initialDelay = 100_000L)
    public void scheduledCleaningLog(){
        nitriteRepository.cleanOldLogRecord(logCleaningTime);
    }

}
