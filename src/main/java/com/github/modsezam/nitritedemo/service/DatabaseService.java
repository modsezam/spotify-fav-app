package com.github.modsezam.nitritedemo.service;

import com.github.modsezam.nitritedemo.model.db.FavoriteTrack;
import com.github.modsezam.nitritedemo.model.spotify.track.Item;
import com.github.modsezam.nitritedemo.model.spotify.track.SpotifyModelTrack;
import com.github.modsezam.nitritedemo.model.spotify.track.Tracks;
import com.github.modsezam.nitritedemo.repository.NitriteRepository;
import lombok.extern.slf4j.Slf4j;
import org.dizitart.no2.exceptions.NitriteIOException;
import org.dizitart.no2.objects.Cursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class DatabaseService {

    @Autowired
    private NitriteRepository nitriteRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeDatabase(){
        log.info("Initialize database");
        try {
            nitriteRepository.nitriteInitialization();
        } catch (NitriteIOException e) {
            log.error("Error initialize db: {}", e.getMessage());
        }
    }

    public void insertNewFavoriteTrackRecord(Item item) {
        nitriteRepository.insertNewFavoriteTrackRecord(item);
    }


    public Optional<Set<String>> getAllIdTracks(){
        Cursor<FavoriteTrack> allTrack = nitriteRepository.findAllTrack();
        Set<String> allTrackId = new HashSet<>();
        if (allTrack != null){
            for (FavoriteTrack favoriteTrack : allTrack) {
                allTrackId.add(favoriteTrack.getId());
            }
            return Optional.of(allTrackId);
        } else {
            log.warn("Find all tracks - empty list");
            nitriteRepository.insertNewLogRecord("Find all tracks - empty list");
        }
        return Optional.empty();
    }

    public ResponseEntity<SpotifyModelTrack> checkTracksAreInFavorites(SpotifyModelTrack spotifyModelTrack) {
        Optional<Set<String>> listTrackById = getAllIdTracks();
        if (!listTrackById.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            List<Item> items = (spotifyModelTrack).getTracks().getItems();
            for (Item item : items) {
                if (listTrackById.get().contains(item.getId())){
                    item.setFavorite(true);
                }
            }
        }
        return ResponseEntity.of(Optional.of(spotifyModelTrack));
    }

    public int deleteTrackByIdFromFavorite(String id) {
        return nitriteRepository.deleteTrackById(id);
    }

    public ResponseEntity<SpotifyModelTrack> getAllFavoritesTracks() {
        Cursor<FavoriteTrack> allFavoritesTracks = nitriteRepository.findAllFavoritesTracks();
        SpotifyModelTrack spotifyModelTrack = new SpotifyModelTrack();
        List<Item> items = new ArrayList<>();
        Tracks tracks = new Tracks();
        for (FavoriteTrack allFavoritesTrack : allFavoritesTracks) {
            allFavoritesTrack.getItem().setFavorite(true);
            items.add(allFavoritesTrack.getItem());
        }
        tracks.setItems(items);
        spotifyModelTrack.setTracks(tracks);
        return ResponseEntity.ok(spotifyModelTrack);
    }
}
