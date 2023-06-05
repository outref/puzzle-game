package com.nikonets.puzzle;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@Configuration
public class PuzzleApplication implements WebMvcConfigurer {
    @Value("${storage.dir}")
    private String storageDir;

    public static void main(String[] args) {
        SpringApplication.run(PuzzleApplication.class, args);
    }

    //To serve static files from external folder
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations(
                        "classpath:/",
                        "classpath:static/",
                        "file:" + storageDir);
    }
}
