package com.example.backoffice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class TempHome {

    @GetMapping("/goHome")
    public String goHome(){
        return "hello! test test test test";
    }
}
