package com.github.modsezam.nitritedemo.configuration;

import com.github.modsezam.nitritedemo.model.spotify.authorisation.SpotifyTokenHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BasicConfig {

    @Bean
    public SpotifyTokenHolder getToken(){
        return new SpotifyTokenHolder();
    }
}