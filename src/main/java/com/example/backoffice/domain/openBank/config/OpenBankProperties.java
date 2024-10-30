package com.example.backoffice.domain.openBank.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "openbank")
public class OpenBankProperties {
    private String useCode;
    private String clientId;
    private String clientSecret;
    private String redirectUri;

}