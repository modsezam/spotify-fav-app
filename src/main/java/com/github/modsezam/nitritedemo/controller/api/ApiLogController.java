package com.github.modsezam.nitritedemo.controller.api;

import com.github.modsezam.nitritedemo.model.db.Log;
import com.github.modsezam.nitritedemo.service.LogService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/api")
public class ApiLogController {

    @Autowired
    private LogService logService;

    @ApiOperation(value = "Use this endpoint to get all logs from database.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Get request from database with success."),
    })
    @GetMapping("/logs")
    public ResponseEntity<List<Log>> getLog(){
        log.info("Get logs from database");
        logService.insertLogRecord("Get logs from database");

        List<Log> logList = logService.getLogs();

        return ResponseEntity.ok(logList);
    }

}
