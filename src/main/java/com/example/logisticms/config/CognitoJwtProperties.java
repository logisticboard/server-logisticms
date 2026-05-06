package com.example.logisticms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aws.cognito")
@Data
public class CognitoJwtProperties {
    private String region;
    private String userPoolId;
    private String clientId;
    private String issuer;
    private String jwkSetUri;
}
