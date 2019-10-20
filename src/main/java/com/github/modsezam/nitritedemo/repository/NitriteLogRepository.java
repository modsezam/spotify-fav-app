package com.github.modsezam.nitritedemo.repository;


import com.github.modsezam.nitritedemo.model.db.Log;
import lombok.extern.slf4j.Slf4j;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.WriteResult;
import org.dizitart.no2.exceptions.NitriteIOException;
import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.ObjectFilter;
import org.dizitart.no2.objects.ObjectRepository;
import org.dizitart.no2.objects.filters.ObjectFilters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;

@Slf4j
@Repository
public class NitriteLogRepository {

    @Value("${db.log-repository-name}")
    private String logRepositoryName;

    @Value("${db.login}")
    private String login;

    @Value("${db.password}")
    private String password;

    private ObjectRepository<Log> repository;

    public void nitriteInitialization() throws NitriteIOException {
        String tempDir = System.getProperty("java.io.tmpdir");
        String path = tempDir + logRepositoryName + ".db";

        File file = new File(path);
        boolean existsRepository = file.exists();
        if (existsRepository) {
            log.info("The database already exists. Open database: {}", logRepositoryName);
        } else {
            log.info("Create data base: {}", logRepositoryName);
        }

        Nitrite db = Nitrite.builder()
                .compressed()
                .filePath(path)
                .openOrCreate(login, password);
        log.info("Save log file to path: {}", path);

        repository = db.getRepository(Log.class);
        log.info("Create an object repository based on class {}", Log.class.getCanonicalName());
    }

    public void insertNewLogRecord(String logText) {
        Log newLog = new Log(logText, System.currentTimeMillis());
        repository.insert(newLog);
        log.debug("Insert new log to data base: {}", logText);
    }

    public void test2() {
        Cursor<Log> logs = repository.find();
        System.out.println("log size " + logs.size());
        for (Log log1 : logs) {
            System.out.println(log1.getLogText());
        }
    }

    public void cleanOldLogRecord(long logCleaningTime){
        ObjectFilter objectFilter = ObjectFilters.lte("logDate", (System.currentTimeMillis() - (logCleaningTime * 1000L)));
        WriteResult logDate = repository.remove(objectFilter);
        int affectedCount = logDate.getAffectedCount();
        if (affectedCount > 0){
            log.info("Deleting logs older than {} s, number of deleted logs: {}", logCleaningTime, affectedCount);
        } else {
            log.info("Log cleaning. No logs older than {} s", logCleaningTime);
        }
    }

}
