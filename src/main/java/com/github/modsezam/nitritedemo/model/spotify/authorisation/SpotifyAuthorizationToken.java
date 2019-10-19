package com.github.modsezam.nitritedemo.model.spotify.authorisation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpotifyAuthorizationToken {
    private String access_token;
    private String token_type;
    private Integer expires_in;
    private String scope;

}
