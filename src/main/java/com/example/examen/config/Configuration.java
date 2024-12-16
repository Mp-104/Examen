package com.example.examen.config;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;

@org.springframework.context.annotation.Configuration
public class Configuration {

    // Configures maximum upload size of files, default is 1 MB
    @Bean
    public MultipartConfigElement configElement () {

        MultipartConfigFactory configFactory = new MultipartConfigFactory();
        configFactory.setMaxFileSize(DataSize.parse("5MB"));
        configFactory.setMaxRequestSize(DataSize.parse("10MB"));

        return configFactory.createMultipartConfig();
    }
}
