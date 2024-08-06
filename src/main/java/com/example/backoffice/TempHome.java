package com.example.backoffice;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1")
public class TempHome {

    @GetMapping("/goHome")
    public String goHome(){
        return "hello! test test test test";
    }
}
