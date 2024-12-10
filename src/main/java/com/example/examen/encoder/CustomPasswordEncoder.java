package com.example.examen.encoder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class CustomPasswordEncoder {

    @Bean
    public PasswordEncoder bcryptPasswordEncoder () {
        return new BCryptPasswordEncoder();
    }
}
