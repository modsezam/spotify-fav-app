package com.github.modsezam.nitritedemo.model.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dizitart.no2.IndexType;
import org.dizitart.no2.objects.Index;
import org.dizitart.no2.objects.Indices;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Indices({
        @Index(value = "logText", type = IndexType.Fulltext)
})
public class Log implements Serializable {
    private String logText;
    private long logDate;
}
