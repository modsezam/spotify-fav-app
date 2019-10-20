package com.github.modsezam.nitritedemo.controller;

import com.github.modsezam.nitritedemo.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class IndexController {

    @Autowired
    private LogService logService;

    @GetMapping("/")
    public String getLog(){
        log.info("Open index page");
        logService.insertLogRecord("Open index page");
        return "index";
    }





}
