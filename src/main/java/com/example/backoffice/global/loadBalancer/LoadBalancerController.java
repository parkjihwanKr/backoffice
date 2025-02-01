package com.example.backoffice.global.loadBalancer;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j(topic = "Load Balancer Controller")
@RequestMapping("/api/v1/health-check")
@Tag(name = "LoadBalancer API", description = "로드 밸런서 체크")
public class LoadBalancerController {

    @GetMapping
    public ResponseEntity<Void> healthCheck(){
        log.info("로드 밸런서 컨트롤러 진입 성공!");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
