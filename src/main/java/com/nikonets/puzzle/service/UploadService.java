package com.nikonets.puzzle.service;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    String uploadImage(String image, Integer sideLength, MultipartFile file) throws IOException;
}
