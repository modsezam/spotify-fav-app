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

import java.util.List;

@Slf4j
@Service
public class LogService {

    @Autowired
    private NitriteRepository nitriteRepository;

    public void insertLogRecord(String logText){
        nitriteRepository.insertNewLogRecord(logText);
    }

    public Cursor<Log> getLogs(){
        return nitriteRepository.findAllLogs();
    }

}
