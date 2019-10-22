package com.github.modsezam.nitritedemo.service;

import com.github.modsezam.nitritedemo.component.HttpFrameComposer;
import com.github.modsezam.nitritedemo.model.spotify.track.SpotifyModelTrack;
import com.github.modsezam.nitritedemo.model.spotify.authorisation.SpotifyTokenHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
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


    public ResponseEntity<SpotifyModelTrack> getTrackList(String query, int limit, int offset, String market ){
        checkSpotifyToken();
        String url = "https://api.spotify.com/v1/search?q=" + query +
                "&type=track&limit=" + limit +
                "&offset=" + offset +
                "&market=" + market;
        ResponseEntity<SpotifyModelTrack> responseEntity = getSpotifyModelResponseEntity(url);
        return responseEntity;
    }


    public ResponseEntity<SpotifyModelTrack> getTrackListFromQuery(String query){
        checkSpotifyToken();
        ResponseEntity<SpotifyModelTrack> responseEntity = getSpotifyModelResponseEntity(query);
        return responseEntity;
    }


    private ResponseEntity<SpotifyModelTrack> getSpotifyModelResponseEntity(String query) {
        RestTemplate rest = new RestTemplate();
        ResponseEntity<SpotifyModelTrack> responseEntity = null;
        try {
            responseEntity = rest.exchange(query,
                    HttpMethod.GET,
                    httpFrameComposer.getAuthorizationTokenEntity(),
                    SpotifyModelTrack.class);
        } catch (RestClientException rce) {
            log.error("Rest client exeption", rce);
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//            e.printStackTrace();
        } catch (IllegalArgumentException iae){
            log.error("Query variable exception", iae);
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            log.info("The correct track request data has been retrieved from Spotify");
            logService.insertLogRecord("The correct track request data has been retrieved from Spotify");
            return responseEntity;
        } else {
            log.error("There is a problem with track request data. Http status code: {}",
                    responseEntity.getStatusCodeValue());
            logService.insertLogRecord("There is a problem with track request data.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


    private void checkSpotifyToken() {
        Optional<LocalDateTime> expiredDateTime = spotifyTokenHolder.getExpiredDateTime();
        if (expiredDateTime.isPresent()){
            LocalDateTime expiredTokenDataTime = expiredDateTime.get();
            if (expiredTokenDataTime.isBefore(LocalDateTime.now())){
                log.warn("Spotify token has expired! {} ", expiredTokenDataTime.toString());
                log.info("Generate new token.");
                logService.insertLogRecord("Spotify token has expired! Generate new token.");
                spotifyAuthorizationService.generateToken();
            } else {
                log.info("Spotify token is valid");
            }
        } else {
            log.info("No token generated. Get new token.");
            logService.insertLogRecord("No token generated. Get new token.");
            spotifyAuthorizationService.generateToken();
        }
    }

}
