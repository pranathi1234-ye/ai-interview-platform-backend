
package com.pranathi.ai_interview_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {

        CorsConfiguration config = new CorsConfiguration();

        // Allow the deployed frontend and local development.
        config.setAllowedOrigins(Arrays.asList(
                "https://ai-interview-platform-frontend-5rbw.onrender.com",
                "http://localhost:3000",
                "http://127.0.0.1:3000"
        ));

        // Allow required HTTP methods.
        config.setAllowedMethods(Arrays.asList(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
        ));

        // Allow Authorization and other request headers.
        config.setAllowedHeaders(Arrays.asList("*"));

        // JWT authentication uses a Bearer token, not cookies.
        config.setAllowCredentials(false);

        // Cache preflight responses.
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
