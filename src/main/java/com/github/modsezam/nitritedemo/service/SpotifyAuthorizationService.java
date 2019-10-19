package com.github.modsezam.nitritedemo.service;

import com.github.modsezam.nitritedemo.component.HttpFrameComposer;
import com.github.modsezam.nitritedemo.model.spotify.authorisation.SpotifyAuthorizationToken;
import com.github.modsezam.nitritedemo.model.spotify.authorisation.SpotifyTokenHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class SpotifyAuthorizationService {
    @Autowired
    private HttpFrameComposer httpFrameComposer;

    @Autowired
    private SpotifyTokenHolder spotifyTokenHolder;

    public ResponseEntity<SpotifyAuthorizationToken> generateToken() {
        String spotifyApiUrl = "https://accounts.spotify.com/api/token";
        log.info("Get url: " + spotifyApiUrl);
        RestTemplate rest = new RestTemplate();
        ResponseEntity<SpotifyAuthorizationToken> resp = rest.exchange(spotifyApiUrl,
                HttpMethod.POST,
                httpFrameComposer.getAuthorizationEntity(),
                SpotifyAuthorizationToken.class);
        if (resp.getStatusCode() == HttpStatus.OK) {
            SpotifyAuthorizationToken token = resp.getBody();
            spotifyTokenHolder.setToken(token);
        }
        return resp;
    }


}
