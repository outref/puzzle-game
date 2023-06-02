package com.nikonets.puzzle.service.impl;

import com.nikonets.puzzle.repository.ImageRepository;
import com.nikonets.puzzle.service.UploadService;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class UploadServiceImpl implements UploadService {
    private final ImageRepository imageRepository;

    @Override
    public String uploadImage(String imageName, MultipartFile file) throws IOException {
        InputStream is = file.getInputStream();
        BufferedImage inputImg = ImageIO.read(is);

        // initializing rows and columns
        int rows = 4;
        int columns = 4;

        // initializing array to hold sub-images
        BufferedImage[] tileImages = new BufferedImage[rows * columns];

        // Equally dividing original image into sub-images
        int subImageWidth = inputImg.getWidth() / columns;
        int subImageHeight = inputImg.getHeight() / rows;

        // iterating over rows and columns for each sub-image
        int currentImg = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                // Creating sub image
                tileImages[currentImg] = new BufferedImage(subImageWidth,
                        subImageHeight, inputImg.getType());
                Graphics2D imgCreator = tileImages[currentImg].createGraphics();

                // coordinates of source image
                int srcFirstX = subImageWidth * j;
                int srcFirstY = subImageHeight * i;

                // coordinates of sub-image
                int dstCornerX = subImageWidth * j + subImageWidth;
                int dstCornerY = subImageHeight * i + subImageHeight;

                imgCreator.drawImage(inputImg, 0, 0, subImageWidth, subImageHeight,
                        srcFirstX, srcFirstY, dstCornerX, dstCornerY, null);
                currentImg++;
            }
        }
        return imageRepository.saveTiles(imageName, inputImg, tileImages);
    }
}
