package com.github.modsezam.nitritedemo.controller;

import com.github.modsezam.nitritedemo.model.db.Log;
import com.github.modsezam.nitritedemo.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@Controller
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping("/logs")
    public String getLog(Model model){
        log.info("Open page with log");
        logService.insertLogRecord("Open page with logs");

        List<Log> logList = logService.getLogs();

        model.addAttribute("logList", logList);
        return "log-list";
    }





}
