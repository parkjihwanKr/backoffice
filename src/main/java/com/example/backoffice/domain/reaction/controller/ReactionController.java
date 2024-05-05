package com.example.backoffice.domain.reaction.controller;

import com.example.backoffice.domain.reaction.service.ReactionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReactionController {

    private final ReactionsService reactionsService;


}
