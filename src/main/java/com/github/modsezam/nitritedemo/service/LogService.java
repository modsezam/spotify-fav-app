package com.github.modsezam.nitritedemo.service;

import com.github.modsezam.nitritedemo.repository.NitriteLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.dizitart.no2.exceptions.NitriteIOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogService {

    @Autowired
    private NitriteLogRepository nitriteLogRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeDatabase(){
        log.info("Initialize database");
        try {
            nitriteLogRepository.nitriteInitialization();
        } catch (NitriteIOException e) {
            log.error("Error initialize db: {}", e.getMessage());
        }
    }

    public void insertRecord(String logText){
        nitriteLogRepository.insertNewLogRecord(logText);
    }

}
