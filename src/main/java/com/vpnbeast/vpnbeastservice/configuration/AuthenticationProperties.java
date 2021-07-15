package com.vpnbeast.vpnbeastservice.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "vpnbeast-service.security")
public class AuthenticationProperties {

    private Long verificationCodeValidInMinutes;

}