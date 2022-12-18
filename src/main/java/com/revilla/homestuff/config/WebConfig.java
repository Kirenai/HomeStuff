package com.revilla.homestuff.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebConfig
 *
 * @author Kirenai
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowCredentials(true)
            .allowedOrigins("http://localhost:19000")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization")
            .exposedHeaders("Access-Control-Allow-Headers",
                "Authorization, x-xsrf-token, Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, "
                    + "Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers")
            .maxAge(3600);
    }

}
