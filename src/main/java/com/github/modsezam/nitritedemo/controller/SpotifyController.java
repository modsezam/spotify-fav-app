package com.github.modsezam.nitritedemo.controller;

import com.github.modsezam.nitritedemo.model.spotify.SpotifyModel;
import com.github.modsezam.nitritedemo.service.LogService;
import com.github.modsezam.nitritedemo.service.SpotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Slf4j
@Controller
@RequestMapping(path = "/spotify/")
public class SpotifyController {

    @Autowired
    private SpotifyService spotifyService;

    @Autowired
    private LogService logService;

    @Value("${page.limit-result}")
    private int pageLimitResult;

    private ResponseEntity<SpotifyModel> spotifyModelResponse;

    @GetMapping("/search")
    public String getIndexSearchPage() {
        log.info("User get index search page");
        logService.insertRecord("User get index search page");
        return "index";
    }

    @GetMapping("/search/tracks")
    public String callGet(Model model,
                          @RequestParam(name = "q") String question) {
        log.info("Get track search request q={}", question);
        logService.insertRecord("Get track search request");

        spotifyModelResponse = spotifyService.getTrackList(question, pageLimitResult, 0, "PL");
        model.addAttribute("trackList", Objects.requireNonNull(spotifyModelResponse.getBody()).getTracks());
        return "track-list";
    }

    @GetMapping("/search/query")
    public String getQuery(Model model,
                          @RequestParam(name = "q") String query) {
        log.info("Get track search request from query {}", query);
        logService.insertRecord("Get track search request from query");

        spotifyModelResponse = spotifyService.getTrackListFromQuery(query);
        model.addAttribute("trackList", Objects.requireNonNull(spotifyModelResponse.getBody()).getTracks());
        return "track-list";
    }

    @GetMapping("/add")
    public String addToFavorite(Model model,
                           @RequestParam(name = "id") String id) {
        log.info("Add track id {} to favorite", id);
        logService.insertRecord("Add track to favorites");

//        spotifyModelResponse = spotifyService.getTrackListFromQuery(query);
        model.addAttribute("trackList", Objects.requireNonNull(spotifyModelResponse.getBody()).getTracks());
        return "track-list";
    }


}
