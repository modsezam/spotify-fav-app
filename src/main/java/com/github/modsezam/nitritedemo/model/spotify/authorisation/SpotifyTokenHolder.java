package com.github.modsezam.nitritedemo.model.spotify.authorisation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpotifyTokenHolder {
    private SpotifyAuthorizationToken token;
}
