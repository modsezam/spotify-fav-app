package com.github.modsezam.nitritedemo.controller;

import com.github.modsezam.nitritedemo.model.spotify.SpotifyModel;
import com.github.modsezam.nitritedemo.service.SpotifyAuthorizationService;
import com.github.modsezam.nitritedemo.service.SpotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Controller
public class SpotifyController {

    @Autowired
    private SpotifyService spotifyService;
    @Autowired
    private SpotifyAuthorizationService spotifyAuthorizationService;

    @GetMapping("/spotify")
    public String callGet(Model model) {

        ResponseEntity<SpotifyModel> trackList = spotifyService.getTrackList("elo", 1, 0, "PL");

        System.out.println(trackList.getStatusCode());
        System.out.println(Objects.requireNonNull(trackList.getBody()).getTracks().getItems().get(0).getAlbum().getName());
        System.out.println(Objects.requireNonNull(trackList.getBody()).getTracks().getItems().get(0).getArtists().get(0).getName());
        System.out.println(Objects.requireNonNull(trackList.getBody()).getTracks().getItems().get(0).getName());
        System.out.println(Objects.requireNonNull(trackList.getBody()).getTracks().getItems().get(0).getName());

        return "index";
    }

    @GetMapping("/token")
    public String getToken() {

        String access_token = Objects.requireNonNull(spotifyAuthorizationService.generateToken().getBody()).getAccess_token();

        System.out.println(access_token);

        return "index";
    }
}
