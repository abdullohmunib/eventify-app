package com.final_project.serverapp.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve uploaded posters from classpath:/static/uploads/posters/
        registry.addResourceHandler("/uploads/posters/**")
                .addResourceLocations("classpath:/static/uploads/posters/");
    }
}
