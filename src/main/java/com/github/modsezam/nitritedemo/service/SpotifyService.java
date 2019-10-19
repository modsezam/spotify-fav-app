package com.github.modsezam.nitritedemo.service;

import com.github.modsezam.nitritedemo.component.HttpFrameComposer;
import com.github.modsezam.nitritedemo.model.spotify.SpotifyModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SpotifyService {

    @Autowired
    private HttpFrameComposer httpFrameComposer;

    public ResponseEntity<SpotifyModel> getTrackList(String query, int limit, int offset, String market ){

        String url = "https://api.spotify.com/v1/search?q=" + query +
                "&type=track&limit=" + limit +
                "&offset=" + offset +
                "&market=" + market;

        RestTemplate rest = new RestTemplate();
        ResponseEntity<SpotifyModel> spotifyModelResponseEntity = rest.exchange(url,
                HttpMethod.GET,
                httpFrameComposer.getAuthorizationTokenEntity(),
                SpotifyModel.class);

        return spotifyModelResponseEntity;
    }

}
