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

    @GetMapping("/search")
    public String getIndexSearchPage() {
        log.info("User get index search page");
        logService.insertRecord("User get index search page");
        return "index";
    }

    @GetMapping("/search/tracks")
    public String callGet(Model model,
                          @RequestParam(name = "q") String question,
                          @RequestParam(name = "page", defaultValue = "0") int page) {
        log.info("Get track search request q={}, page={}", question, page);
        logService.insertRecord("Get track search request");
        int offset = page * pageLimitResult;

        ResponseEntity<SpotifyModel> spotifyModelResponse = spotifyService.getTrackList(question, pageLimitResult, offset, "PL");

        model.addAttribute("trackList", Objects.requireNonNull(spotifyModelResponse.getBody().getTracks()));

//        System.out.println(trackList.getStatusCode());
//        System.out.println(Objects.requireNonNull(trackList.getBody()).getTracks().getItems().get(0).getAlbum().getName());
//        System.out.println(Objects.requireNonNull(trackList.getBody()).getTracks().getItems().get(0).getArtists().get(0).getName());
//        System.out.println(Objects.requireNonNull(trackList.getBody()).getTracks().getItems().get(0).getName());
//        System.out.println(Objects.requireNonNull(trackList.getBody()).getTracks().getItems().get(0).getName());

        return "track-list";
    }


}
