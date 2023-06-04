package com.nikonets.puzzle.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    String uploadAndCutImage(String image, Integer sideLength, MultipartFile file);
}
