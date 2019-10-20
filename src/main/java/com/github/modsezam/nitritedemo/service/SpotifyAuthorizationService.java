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

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
public class SpotifyAuthorizationService {
    @Autowired
    private HttpFrameComposer httpFrameComposer;

    @Autowired
    private SpotifyTokenHolder spotifyTokenHolder;

    @Autowired
    private LogService logService;

    public ResponseEntity<SpotifyAuthorizationToken> generateToken() {
        String spotifyApiUrl = "https://accounts.spotify.com/api/token";
        RestTemplate rest = new RestTemplate();
        ResponseEntity<SpotifyAuthorizationToken> responseEntity = rest.exchange(spotifyApiUrl,
                HttpMethod.POST,
                httpFrameComposer.getAuthorizationEntity(),
                SpotifyAuthorizationToken.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            SpotifyAuthorizationToken token = responseEntity.getBody();
            spotifyTokenHolder.setToken(Objects.requireNonNull(token).getAccess_token());
            spotifyTokenHolder.setExpiredDateTime(LocalDateTime.now().plusSeconds(token.getExpires_in()));
            log.info("The correct token data has been retrieved from Spotify");
            logService.insertLogRecord("The correct token data has been retrieved from Spotify");
        } else {
            log.warn("There is a problem with token request data. Http status code: {}",
                    responseEntity.getStatusCodeValue());
            logService.insertLogRecord("There is a problem with token request data.");
        }
        return responseEntity;
    }


}
