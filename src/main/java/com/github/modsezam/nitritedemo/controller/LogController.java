package com.github.modsezam.nitritedemo.controller;

import com.github.modsezam.nitritedemo.repository.NitriteLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
public class LogController {


    //poprawić i dodać serwis!!!
    @Autowired
    NitriteLogRepository nitriteLogRepository;

    @GetMapping("/")
    public String getLog(){
        System.out.println("get log ");
        log.info("eko ajfdkjaf");
        return "index";
    }

    @GetMapping("/test")
    public String getLog2(){
        nitriteLogRepository.test2();
        return "index";
    }

    @GetMapping("/insert/{logText}")
    public String insertLog(@PathVariable("logText")String newLog){
        nitriteLogRepository.insertNewLog(newLog);
        return "index";
    }

    @GetMapping("/remove")
    public String removeLog(){
        nitriteLogRepository.remove(10_000);
        return "index";
    }



}
