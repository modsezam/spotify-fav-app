package com.github.modsezam.nitritedemo.model.spotify.authorisation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpotifyTokenHolder {

    private String token;
    private LocalDateTime expiredDateTime;

    public Optional<LocalDateTime> getExpiredDateTime() {
        return Optional.ofNullable(expiredDateTime);
    }
}
