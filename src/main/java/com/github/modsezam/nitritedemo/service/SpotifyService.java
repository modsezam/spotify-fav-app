package com.github.modsezam.nitritedemo.service;

import com.github.modsezam.nitritedemo.component.HttpFrameComposer;
import com.github.modsezam.nitritedemo.model.spotify.SpotifyModel;
import com.github.modsezam.nitritedemo.model.spotify.authorisation.SpotifyTokenHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class SpotifyService {

    @Autowired
    private HttpFrameComposer httpFrameComposer;

    @Autowired
    private SpotifyAuthorizationService spotifyAuthorizationService;

    @Autowired
    private SpotifyTokenHolder spotifyTokenHolder;

    @Autowired
    private LogService logService;


    public ResponseEntity<SpotifyModel> getTrackList(String query, int limit, int offset, String market ){
        checkSpotifyToken();
        String url = "https://api.spotify.com/v1/search?q=" + query +
                "&type=track&limit=" + limit +
                "&offset=" + offset +
                "&market=" + market;
        ResponseEntity<SpotifyModel> responseEntity = getSpotifyModelResponseEntity(url);
        return responseEntity;
    }


    public ResponseEntity<SpotifyModel> getTrackListFromQuery(String query){
        checkSpotifyToken();
        ResponseEntity<SpotifyModel> responseEntity = getSpotifyModelResponseEntity(query);
        return responseEntity;
    }


    private ResponseEntity<SpotifyModel> getSpotifyModelResponseEntity(String query) {
        RestTemplate rest = new RestTemplate();
        ResponseEntity<SpotifyModel> responseEntity = rest.exchange(query,
                HttpMethod.GET,
                httpFrameComposer.getAuthorizationTokenEntity(),
                SpotifyModel.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            log.info("The correct track request data has been retrieved from Spotify");
            logService.insertRecord("The correct track request data has been retrieved from Spotify");
        } else {
            log.error("There is a problem with track request data. Http status code: {}",
                    responseEntity.getStatusCodeValue());
            logService.insertRecord("There is a problem with track request data.");
        }
        return responseEntity;
    }


    private void checkSpotifyToken() {
        Optional<LocalDateTime> expiredDateTime = spotifyTokenHolder.getExpiredDateTime();
        if (expiredDateTime.isPresent()){
            LocalDateTime expiredTokenDataTime = expiredDateTime.get();
            if (expiredTokenDataTime.isBefore(LocalDateTime.now())){
                log.warn("Spotify token has expired! {} ", expiredTokenDataTime.toString());
                log.info("Generate new token.");
                logService.insertRecord("Spotify token has expired! Generate new token.");
                spotifyAuthorizationService.generateToken();
            } else {
                log.info("Spotify token is valid");
            }
        } else {
            log.info("No token generated. Get new token.");
            logService.insertRecord("No token generated. Get new token.");
            spotifyAuthorizationService.generateToken();
        }
    }


}
