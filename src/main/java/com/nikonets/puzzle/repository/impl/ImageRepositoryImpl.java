package com.nikonets.puzzle.repository.impl;

import com.nikonets.puzzle.repository.ImageRepository;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import org.springframework.stereotype.Repository;

@Repository
public class ImageRepositoryImpl implements ImageRepository {
    private static final String REFERENCE_IMG_NAME = "reference.jpg";
    private static final String IMG_FORMAT = "jpg";
    private static final String IMAGES_DIR = "images/";
    private static final String SOLUTIONS_DIR = "images/solutions/";

    @Override
    public String saveTiles(String imageName, BufferedImage refImg, BufferedImage[] tileImages) {
        String dirPath = IMAGES_DIR + imageName;
        File tilesDir = new File(dirPath);
        tilesDir.mkdirs();

        //writing original(reference) image
        File refImgFile = new File(dirPath + "/" + REFERENCE_IMG_NAME);
        try {
            ImageIO.write(refImg, IMG_FORMAT, refImgFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write reference image file to repository!", e);
        }

        //writing sub-images into image files
        for (int i = 0; i < tileImages.length; i++) {
            File outputFile = new File(dirPath + "/" + i + "." + IMG_FORMAT);
            try {
                ImageIO.write(tileImages[i], IMG_FORMAT, outputFile);
            } catch (IOException e) {
                throw new RuntimeException("Failed to write tile image file to repository!", e);
            }
        }
        return dirPath + "/" + REFERENCE_IMG_NAME;
    }

    @Override
    public List<String> getAllImages() {
        File dir = new File(IMAGES_DIR);
        return Arrays.asList(dir.list());
    }

    @Override
    public List<String> getAllTilesByImageName(String imageName) {
        File dir = new File(IMAGES_DIR + imageName);
        List<String> tilesList = new ArrayList<>();
        for (int i = 0; i < dir.list().length - 1; i++) {
            tilesList.add(IMAGES_DIR + imageName + "/" + i + "." + IMG_FORMAT);
        }
        return tilesList;
    }

    @Override
    public String getReferenceByImageName(String imageName) {
        return IMAGES_DIR + imageName + "/" + REFERENCE_IMG_NAME;
    }

    @Override
    public String saveSolution(BufferedImage solution) {
        int id = new File(SOLUTIONS_DIR).list().length + 1;
        String path = SOLUTIONS_DIR + id + "." + IMG_FORMAT;
        File outputFile = new File(path);
        try {
            ImageIO.write(solution, IMG_FORMAT, outputFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save solution image", e);
        }
        return path;
    }
}
