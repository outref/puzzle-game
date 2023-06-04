package com.nikonets.puzzle.service.impl;

import com.nikonets.puzzle.exception.InputReadingException;
import com.nikonets.puzzle.service.FileToImageReaderService;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileToImageServiceImpl implements FileToImageReaderService {
    @Override
    public BufferedImage fileToImage(MultipartFile file) {
        if (!file.getContentType().equals("image/jpeg")) {
            throw new InputReadingException(file.getOriginalFilename()
                    + " is not a jpeg image!");
        }
        BufferedImage inputImg;
        try (InputStream is = file.getInputStream()) {
            inputImg = ImageIO.read(is);
        } catch (Exception e) {
            throw new InputReadingException("Failed to read input file "
                    + file.getOriginalFilename(), e);
        }
        if (inputImg == null) {
            throw new InputReadingException("Failed to read image from input file "
                    + file.getOriginalFilename());
        }
        return inputImg;
    }
}
