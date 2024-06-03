package com.t1.jwt_auth_app.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "super-user")
@Getter
@Setter
public class SuperUserProperties {
    private String name;
    private String email;
    private String password;
    private String role;
}
