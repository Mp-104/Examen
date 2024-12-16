package com.example.examen.config;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;

@org.springframework.context.annotation.Configuration
public class Configuration {

    // Sets maximum upload size of files to 10 MB, default is 1 MB
    @Bean
    public MultipartConfigElement configElement () {

        MultipartConfigFactory configFactory = new MultipartConfigFactory();
        configFactory.setMaxFileSize(DataSize.parse("1MB"));
        configFactory.setMaxRequestSize(DataSize.parse("10MB"));

        return configFactory.createMultipartConfig();
    }
}
