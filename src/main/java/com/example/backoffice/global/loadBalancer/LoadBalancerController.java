package com.example.backoffice.global.loadBalancer;

import com.example.backoffice.global.dto.CommonResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/health-check")
@Tag(name = "LoadBalancer API", description = "로드 밸런서 체크")
public class LoadBalancerController {

    @GetMapping
    public ResponseEntity<CommonResponseDto<Void>> healthCheck(){
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        null,
                        "정상적으로 로드 밸런서가 작동하고 있습니다.",
                        200));
    }
}
