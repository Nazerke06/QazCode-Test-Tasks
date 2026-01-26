package org.qazcodenarxoz.mainmicroservice1.controller;

import lombok.RequiredArgsConstructor;
import org.qazcodenarxoz.mainmicroservice1.entity.MC1Entity;
import org.qazcodenarxoz.mainmicroservice1.service.MC1Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MC1Controller {
    private final MC1Service mc1service;

    @GetMapping("/start")
    public String start() {
        return mc1service.start();
    }

    @GetMapping("/stop")
    public String stop() {
        return mc1service.stop();
    }
    @PostMapping("/receive")
    public ResponseEntity<?> receiveFromMC3(@RequestBody MC1Entity entity){
        mc1service.cycle(entity);
        return ResponseEntity.ok("Message received");
    }
}