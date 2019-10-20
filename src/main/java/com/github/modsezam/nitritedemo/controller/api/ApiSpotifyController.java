package com.github.modsezam.nitritedemo.controller.api;

import com.github.modsezam.nitritedemo.model.spotify.SpotifyModel;
import com.github.modsezam.nitritedemo.service.DatabaseService;
import com.github.modsezam.nitritedemo.service.LogService;
import com.github.modsezam.nitritedemo.service.SpotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
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

    private SpotifyModel spotifyModelResp;

    private String currentQuery;

    private Set<String> favoriteTrackListById;

    @GetMapping("/search/tracks")
    public ResponseEntity<SpotifyModel> callGet(@RequestParam(name = "q") String query,
                                                @RequestParam(name = "limit", defaultValue = "10") int pageLimit,
                                                @RequestParam(name = "ofset", defaultValue = "0") int offset,
                                                @RequestParam(name = "market", defaultValue = "PL") String market) {

        log.info("Api - Get track search request q={}", query);
        logService.insertLogRecord("Api - Get track search request");

        spotifyModelResp = spotifyService.getTrackList(query, pageLimit, offset, market).getBody();
        spotifyModelResp = databaseService.checkFavoritesTrack(spotifyModelResp);

        return ResponseEntity.ok(spotifyModelResp);
    }

}
