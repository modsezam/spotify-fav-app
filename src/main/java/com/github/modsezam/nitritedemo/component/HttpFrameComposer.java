package com.github.modsezam.nitritedemo.component;

import com.github.modsezam.nitritedemo.model.spotify.authorisation.SpotifyTokenHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


@Slf4j
@Component
public class HttpFrameComposer {

    @Value("${spotify.client-id}")
    private String spoifyClientId;

    @Value("${spotify.client-secret}")
    private String spoifyClientSecret;

    @Autowired
    private SpotifyTokenHolder spotifyTokenHolder;

    public HttpEntity getAuthorizationEntity() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        body.add("client_id", spoifyClientId);
        body.add("client_secret", spoifyClientSecret);
        body.add("grant_type", "client_credentials");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");

        return new HttpEntity(body, headers);
    }

    public HttpEntity getAuthorizationTokenEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + spotifyTokenHolder.getToken().getAccess_token());

        return new HttpEntity(headers);
    }
}
