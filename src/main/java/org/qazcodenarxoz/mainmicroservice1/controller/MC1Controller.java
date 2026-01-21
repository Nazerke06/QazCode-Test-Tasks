package org.qazcodenarxoz.mainmicroservice1.controller;

import com.example.dmc1.service.MC1Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MC1Controller {
    private final MC1Service mc1service;

    @GetMapping("/start")
    public String start(){
        return mc1service.start();
    }
    @GetMapping("/stop")
    public String stop(){
        return mc1service.stop();
    }


}
