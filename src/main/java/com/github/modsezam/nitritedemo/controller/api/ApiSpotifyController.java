package com.github.modsezam.nitritedemo.controller.api;

import com.github.modsezam.nitritedemo.model.spotify.Item;
import com.github.modsezam.nitritedemo.model.spotify.SpotifyModel;
import com.github.modsezam.nitritedemo.service.DatabaseService;
import com.github.modsezam.nitritedemo.service.LogService;
import com.github.modsezam.nitritedemo.service.SpotifyService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping(path = "/api")
public class ApiSpotifyController {

    @Autowired
    private SpotifyService spotifyService;

    @Autowired
    private LogService logService;

    @Autowired
    private DatabaseService databaseService;

    @Value("${page.limit-result}")
    private int pageLimitResult;

    private ResponseEntity<SpotifyModel> spotifyModelResponseEntity;

    private String currentQuery;

    private Set<String> favoriteTrackListById;

    @GetMapping("/search/tracks")
    @ApiOperation(value = "Use this endpoint to search for tracks on the Spotify API and " +
            "add information if the track is in favorites (item.isFavorite).")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Get request from API Spotify with success."),
            @ApiResponse(code = 400, message = "Bad request from API Spotify.")
    })
    public ResponseEntity<SpotifyModel> searchTrack(@RequestParam(name = "q") String query,
                                                @RequestParam(name = "limit", defaultValue = "10") int pageLimit,
                                                @RequestParam(name = "ofset", defaultValue = "0") int offset,
                                                @RequestParam(name = "market", defaultValue = "PL") String market) {

        log.info("Api - Get track search request q={}", query);
        logService.insertLogRecord("Api - Get track search request");

        spotifyModelResponseEntity = spotifyService.getTrackList(query, pageLimit, offset, market);
        if (spotifyModelResponseEntity.getStatusCode() == HttpStatus.OK ){
            spotifyModelResponseEntity = databaseService.checkTracksAreInFavorites(spotifyModelResponseEntity.getBody());
        }
        return spotifyModelResponseEntity;
    }

    @GetMapping("/search/tracks/query")
    @ApiOperation(value = "Use this endpoint to search query from HATEOAS link on the Spotify API and " +
            "add information if the track is in favorites (item.isFavorite).")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Get request from API Spotify with success."),
            @ApiResponse(code = 400, message = "Bad request from API Spotify.")
    })
    public ResponseEntity<SpotifyModel> searchTrackByQuery(@RequestParam(name = "q") String query) {

        log.info("Get track request from HATEOAS link {}", query);
        logService.insertLogRecord("Get track request from HATEOAS link");
        this.currentQuery = query;

        spotifyModelResponseEntity = spotifyService.getTrackListFromQuery(query);
        if (spotifyModelResponseEntity.getStatusCode() == HttpStatus.OK ){
            spotifyModelResponseEntity = databaseService.checkTracksAreInFavorites(spotifyModelResponseEntity.getBody());
        }
        return spotifyModelResponseEntity;
    }

    @ApiOperation(value = "Use this endpoint to add track by id to favorite tack database.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Add track to database - favorites track."),
            @ApiResponse(code = 404, message = "No such id found in search results.")
    })
    @PutMapping("/add/track")
    public ResponseEntity addToFavorite(@RequestParam(name = "id") String id) {
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
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            log.warn("Database adding error track id {} to favorite", id);
            logService.insertLogRecord("Database adding error to favorite");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ApiOperation(value = "Use this endpoint to delete track by id from favorite tack database.")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Delete track from database - favorites track."),
            @ApiResponse(code = 404, message = "No such id found in database (track favorites).")
    })
    @DeleteMapping("/remove/track")
    public ResponseEntity deleteFromFavorite(@RequestParam(name = "id") String id) {
        log.info("Deleting track id {} from favorite", id);
        logService.insertLogRecord("Deleting track from favorite");

        int numberOfDeletedRecords = databaseService.deleteTrackByIdFromFavorite(id);
        if (numberOfDeletedRecords == 1) {
            log.info("Tack id {} has been removed from favorite", id);
            logService.insertLogRecord("Tack has been removed from favorite");
            ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } else {
            log.warn("Tack id {} deletion error from favorites", id);
            logService.insertLogRecord("Tack deletion error from favorites");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ApiOperation(value = "Use this endpoint to get all favorite tracks from database.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Get request from database with success."),
    })
    @GetMapping("/favorites/track")
    public ResponseEntity<SpotifyModel> getAllFavoriteTracks() {
        log.info("Get all favorites tracks from database");
        logService.insertLogRecord("Get all favorites tracks from database");
        spotifyModelResponseEntity = databaseService.getAllFavoritesTracks();
        return spotifyModelResponseEntity;
    }
}
