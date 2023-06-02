package com.nikonets.puzzle.service.impl;

import com.nikonets.puzzle.service.UploadService;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadServiceImpl implements UploadService {
    @Value("${images.dir}")
    private String imagesDir;

    @Override
    public String uploadImage(String imageName, MultipartFile file) throws IOException {
        String dirPath = imagesDir + imageName;
        File tilesDir = new File(dirPath);
        tilesDir.mkdirs();

        InputStream is = file.getInputStream();
        BufferedImage image = ImageIO.read(is);

        //writing original(reference) image
        File refImg = new File(dirPath + "/reference.jpg");
        ImageIO.write(image, "jpg", refImg);

        // initalizing rows and columns
        int rows = 4;
        int columns = 4;

        // initializing array to hold subimages
        BufferedImage[] imgs = new BufferedImage[16];

        // Equally dividing original image into subimages
        int subimageWidth = image.getWidth() / columns;
        int subimageHeight = image.getHeight() / rows;

        int currentImg = 0;

        // iterating over rows and columns for each sub-image
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                // Creating sub image
                imgs[currentImg] = new BufferedImage(subimageWidth,
                        subimageHeight, image.getType());
                Graphics2D imgCreator = imgs[currentImg].createGraphics();

                // coordinates of source image
                int srcFirstX = subimageWidth * j;
                int srcFirstY = subimageHeight * i;

                // coordinates of sub-image
                int dstCornerX = subimageWidth * j + subimageWidth;
                int dstCornerY = subimageHeight * i + subimageHeight;

                imgCreator.drawImage(image, 0, 0, subimageWidth, subimageHeight,
                        srcFirstX, srcFirstY, dstCornerX, dstCornerY, null);
                currentImg++;
            }
        }

        //writing sub-images into image files
        for (int i = 0; i < 16; i++) {
            File outputFile = new File(dirPath + "/" + i + ".jpg");
            ImageIO.write(imgs[i], "jpg", outputFile);
        }

        return dirPath + "/reference.jpg";
    }
}
