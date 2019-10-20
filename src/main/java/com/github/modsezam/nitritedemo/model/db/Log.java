package com.github.modsezam.nitritedemo.model.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dizitart.no2.IndexType;
import org.dizitart.no2.objects.Index;
import org.dizitart.no2.objects.Indices;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Indices({
        @Index(value = "logText", type = IndexType.Fulltext)
})
public class Log implements Serializable {

    private String logText;
    private long logDate;
    private LocalDateTime logLocalDateTime;

    public Log(String logText, long logDate) {
        this.logText = logText;
        this.logDate = logDate;
    }
}
