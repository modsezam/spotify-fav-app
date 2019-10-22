package com.github.modsezam.nitritedemo.model.db;

import com.github.modsezam.nitritedemo.model.spotify.track.Artist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dizitart.no2.objects.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteArtist {

    @Id
    private String id;

    private Artist artist;
}
