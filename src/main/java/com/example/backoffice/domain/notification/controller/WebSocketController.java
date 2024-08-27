package com.example.backoffice.domain.notification.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class WebSocketController {

    @GetMapping("/websocket")
    public String showWebSocketPage(){
        log.info("showWebSocketPage Success!");
        return "notificationTestPage";
    }
}
