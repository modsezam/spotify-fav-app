package com.github.modsezam.nitritedemo.repository;


import com.github.modsezam.nitritedemo.model.db.FavoriteArtist;
import com.github.modsezam.nitritedemo.model.db.FavoriteTrack;
import com.github.modsezam.nitritedemo.model.db.Log;
import com.github.modsezam.nitritedemo.model.spotify.Artist;
import com.github.modsezam.nitritedemo.model.spotify.Item;
import lombok.extern.slf4j.Slf4j;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.WriteResult;
import org.dizitart.no2.exceptions.NitriteIOException;
import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.ObjectFilter;
import org.dizitart.no2.objects.ObjectRepository;
import org.dizitart.no2.objects.filters.ObjectFilters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;

import static org.dizitart.no2.objects.filters.ObjectFilters.eq;

@Slf4j
@Repository
public class NitriteRepository {

    @Value("${db.log-repository-name}")
    private String logRepositoryName;

    @Value("${db.login}")
    private String login;

    @Value("${db.password}")
    private String password;

    private ObjectRepository<Log> logRepository;

    private ObjectRepository<FavoriteTrack> favoriteTrackRepository;

    private ObjectRepository<FavoriteArtist> favoriteArtistRepository;

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

        logRepository = db.getRepository(Log.class);
        log.info("Create an object repository based on class {}", Log.class.getCanonicalName());
        favoriteTrackRepository = db.getRepository(FavoriteTrack.class);
        log.info("Create an object repository based on class {}", FavoriteTrack.class.getCanonicalName());
        favoriteArtistRepository = db.getRepository(FavoriteArtist.class);
        log.info("Create an object repository based on class {}", FavoriteArtist.class.getCanonicalName());
    }

    public void insertNewLogRecord(String logText) {
        Log newLog = new Log(logText, System.currentTimeMillis());
        logRepository.insert(newLog);
        log.debug("Insert new log to data base: {}", logText);
    }

    public void insertNewFavoriteTrackRecord(Item item) {
        FavoriteTrack favoriteTrack = new FavoriteTrack(item.getId(), item);
        favoriteTrackRepository.insert(favoriteTrack);
        log.debug("Insert new favorite track to data base. Track id: {}", item.getId());
    }

    public void insertNewFavoriteArtistRecord(Artist artist) {
        FavoriteArtist favoriteArtist = new FavoriteArtist(artist.getId(), artist);
        favoriteArtistRepository.insert(favoriteArtist);
        log.debug("Insert new favorite artist to data base. Track id: {}", artist.getId());
    }

    public Cursor<FavoriteTrack> findAllTrack() {
        return favoriteTrackRepository.find();
    }

    public void cleanOldLogRecord(long logCleaningTime){
        ObjectFilter objectFilter = ObjectFilters.lte("logDate", (System.currentTimeMillis() - (logCleaningTime * 1000L)));
        WriteResult logDate = logRepository.remove(objectFilter);
        int affectedCount = logDate.getAffectedCount();
        if (affectedCount > 0){
            log.info("Deleting logs older than {} s, number of deleted logs: {}", logCleaningTime, affectedCount);
        } else {
            log.info("Log cleaning. No logs older than {} s", logCleaningTime);
        }
    }

    public int deleteTrackById(String id) {
        WriteResult removeResult = favoriteTrackRepository.remove(eq("id", id));
        return removeResult.getAffectedCount();
    }

    public Cursor<FavoriteTrack> findAllFavoritesTracks() {
        return favoriteTrackRepository.find();
    }

    public Cursor<Log> findAllLogs() {
        return logRepository.find();
    }
}
