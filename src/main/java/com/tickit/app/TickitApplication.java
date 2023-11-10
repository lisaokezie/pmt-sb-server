package com.tickit.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class TickitApplication {

    public static void main(String[] args) {
        SpringApplication.run(TickitApplication.class, args);
    }

    /**
     * Enable CORS for localhost:4200
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/greeting-javaconfig")
                        .allowedOrigins("http://localhost:8080", "http://localhost:4200");
            }
        };
    }
}
