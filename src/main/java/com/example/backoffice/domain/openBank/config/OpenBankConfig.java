package com.example.backoffice.domain.openBank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OpenBankConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
