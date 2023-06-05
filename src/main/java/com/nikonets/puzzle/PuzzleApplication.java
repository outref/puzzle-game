package com.nikonets.puzzle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class PuzzleApplication implements WebMvcConfigurer {
    public static void main(String[] args) {
        SpringApplication.run(PuzzleApplication.class, args);
    }
}
