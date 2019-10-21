package com.github.modsezam.nitritedemo.controller;

import com.github.modsezam.nitritedemo.model.spotify.Item;
import com.github.modsezam.nitritedemo.model.spotify.SpotifyModel;
import com.github.modsezam.nitritedemo.service.DatabaseService;
import com.github.modsezam.nitritedemo.service.LogService;
import com.github.modsezam.nitritedemo.service.SpotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Slf4j
@Controller
@RequestMapping(path = "/spotify/")
public class SpotifyController {

    @Autowired
    private SpotifyService spotifyService;

    @Autowired
    private LogService logService;

    @Autowired
    private DatabaseService databaseService;

    @Value("${page.limit-result}")
    private int pageLimitResult;

    //    private SpotifyModel spotifyModelResp;
    ResponseEntity<SpotifyModel> spotifyModelResponseEntity;

    private String currentQuery;

    private Set<String> favoriteTrackListById;

    public SpotifyController() {
        favoriteTrackListById = new HashSet<>();
    }

    @GetMapping("/search")
    public String getIndexSearchPage() {
        log.info("User get index search page");
        logService.insertLogRecord("User get index search page");
        return "index";
    }

    @GetMapping("/search/tracks")
    public String callGet(Model model,
                          @RequestParam(name = "q") String query) {

        log.info("Get track search request q={}", query);
        logService.insertLogRecord("Get track search request");

        spotifyModelResponseEntity = spotifyService.getTrackList(query, pageLimitResult, 0, "PL");
        if (spotifyModelResponseEntity.getStatusCode() == HttpStatus.OK ){
            spotifyModelResponseEntity = databaseService.checkTracksAreInFavorites(spotifyModelResponseEntity.getBody());
        }
        model.addAttribute("trackList", Objects.requireNonNull(spotifyModelResponseEntity.getBody()).getTracks());
        return "track-list";
    }

    @GetMapping("/search/query")
    public String getQuery(Model model,
                           @RequestParam(name = "q") String query) {
        log.info("Get track search request from query {}", query);
        logService.insertLogRecord("Get track search request from query");
        this.currentQuery = query;

        spotifyModelResponseEntity = spotifyService.getTrackListFromQuery(query);
        if (spotifyModelResponseEntity.getStatusCode() == HttpStatus.OK ){
            spotifyModelResponseEntity = databaseService.checkTracksAreInFavorites(spotifyModelResponseEntity.getBody());
        }
        model.addAttribute("trackList", Objects.requireNonNull(spotifyModelResponseEntity.getBody()).getTracks());
        return "track-list";
    }

    @GetMapping("/add/track")
    public String addToFavorite(Model model,
                                @RequestParam(name = "id") String id) {
        log.info("Adding track id {} to favorite", id);
        logService.insertLogRecord("Adding track to favorites");


        Optional<Item> itemOptional = Objects.requireNonNull(spotifyModelResponseEntity.getBody()).getTracks().getItems()
                .stream()
                .filter(item -> item.getId().equals(id))
                .findFirst();
        if (itemOptional.isPresent()) {
            databaseService.insertNewFavoriteTrackRecord(itemOptional.get());
            log.info("Add track id {} to database", id);
            logService.insertLogRecord("Add track to database");
        } else {
            log.warn("Database adding error track id {} to favorite", id);
            logService.insertLogRecord("Database adding error to favorite");
        }

        spotifyModelResponseEntity = databaseService.checkTracksAreInFavorites(spotifyModelResponseEntity.getBody());

        model.addAttribute("trackList", Objects.requireNonNull(spotifyModelResponseEntity.getBody()).getTracks());
        return "track-list";
    }

    @GetMapping("/remove/track")
    public String deleteFromFavorite(Model model,
                                     @RequestParam(name = "id") String id) {
        log.info("Deleting track id {} from favorite", id);
        logService.insertLogRecord("Deleting track from favorite");

        int numberOfDeletedRecords = databaseService.deleteTrackByIdFromFavorite(id);
        if (numberOfDeletedRecords == 1) {
            log.info("Tack id {} has been removed from favorite", id);
            logService.insertLogRecord("Tack has been removed from favorite");
        } else {
            log.warn("Tack id {} deletion error from favorites", id);
            logService.insertLogRecord("Tack deletion error from favorites");
        }

        Objects.requireNonNull(spotifyModelResponseEntity.getBody()).getTracks().getItems()
                .stream()
                .filter(item -> item.getId().equals(id))
                .forEach(item -> item.setFavorite(null));

        model.addAttribute("trackList", Objects.requireNonNull(spotifyModelResponseEntity.getBody()).getTracks());
        return "track-list";
    }

    @GetMapping("/find/tracks")
    public String findAllTracks(Model model) {
        log.info("Find all tracks in database");
        logService.insertLogRecord("Find all tracks in database");
        favoriteTrackListById = databaseService.getAllIdTracks();

        model.addAttribute("trackList", Objects.requireNonNull(spotifyModelResponseEntity.getBody()).getTracks());

        return "track-list";
    }

    @GetMapping("/favorites")
    public String getAllFavoriteTracks(Model model) {
        log.info("Get all favorites tracks in database");
        logService.insertLogRecord("Get all favorites tracks in database");
        spotifyModelResponseEntity = databaseService.getAllFavoritesTracks();

        model.addAttribute("trackList", Objects.requireNonNull(spotifyModelResponseEntity.getBody().getTracks()));

        return "track-list";
    }

}
