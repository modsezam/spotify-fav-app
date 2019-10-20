package com.github.modsezam.nitritedemo.service;

import com.github.modsezam.nitritedemo.model.db.FavoriteTrack;
import com.github.modsezam.nitritedemo.model.db.Log;
import com.github.modsezam.nitritedemo.repository.NitriteRepository;
import lombok.extern.slf4j.Slf4j;
import org.dizitart.no2.exceptions.NitriteIOException;
import org.dizitart.no2.objects.Cursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.security.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Slf4j
@Service
public class LogService {

    @Autowired
    private NitriteRepository nitriteRepository;

    public void insertLogRecord(String logText) {
        nitriteRepository.insertNewLogRecord(logText);
    }

    public List<Log> getLogs() {
        Cursor<Log> logs = nitriteRepository.findAllLogs();
        List<Log> logList = new ArrayList<>();
        for (Log singleLog : logs) {
            long logLongDate = singleLog.getLogDate();
            LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(logLongDate), TimeZone.getDefault().toZoneId());
            singleLog.setLogLocalDateTime(localDateTime);
            logList.add(singleLog);
        }
        return logList;
    }

}
