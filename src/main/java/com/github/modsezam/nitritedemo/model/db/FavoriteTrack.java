package com.github.modsezam.nitritedemo.model.db;

import com.github.modsezam.nitritedemo.model.spotify.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dizitart.no2.objects.Id;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteTrack implements Serializable {

    @Id
    private String id;

    private Item item;

}
