package com.nikonets.puzzle.repository.impl;

import com.nikonets.puzzle.repository.PuzzleRepository;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class PuzzleRepositoryImpl implements PuzzleRepository {
    private static final String ORIGINAL_IMG_NAME = "original.jpg";
    private static final String IMG_FORMAT = "jpg";
    @Value("${images.storage.dir}")
    private String imagesDir;
    @Value("${solutions.storage.dir}")
    private String solutionsDir;

    @Override
    public String savePuzzle(String imageName,
                             BufferedImage originalImg,
                             BufferedImage[] tileImages) {
        //remove special characters from name
        imageName = imageName.replaceAll("[^A-Za-z0-9]","");

        String dirPath = imagesDir + imageName;
        File tilesDir = new File(dirPath);
        tilesDir.mkdirs();

        //writing original(reference) image
        File originalImgFile = new File(dirPath + "/" + ORIGINAL_IMG_NAME);
        try {
            ImageIO.write(originalImg, IMG_FORMAT, originalImgFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write original image file to repository!", e);
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
        return "/" + dirPath + "/" + ORIGINAL_IMG_NAME;
    }

    @Override
    public List<String> getAllPuzzles() {
        File dir = new File(imagesDir);
        return Arrays.asList(dir.list());
    }

    @Override
    public List<String> getTilesUrlsByImageName(String imageName) {
        File dir = new File(imagesDir + imageName);
        List<String> tilesList = new ArrayList<>();
        for (int i = 0; i < dir.list().length - 1; i++) {
            tilesList.add("/" + imagesDir + imageName + "/" + i + "." + IMG_FORMAT);
        }
        return tilesList;
    }

    @Override
    public String getOriginalImageByName(String imageName) {
        return "/" + imagesDir + imageName + "/" + ORIGINAL_IMG_NAME;
    }

    @Override
    public String saveSolution(BufferedImage solution) {
        int id = new File(solutionsDir).list().length + 1;
        String path = solutionsDir + id + "." + IMG_FORMAT;
        File outputFile = new File(path);
        try {
            ImageIO.write(solution, IMG_FORMAT, outputFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save solution image", e);
        }
        return path;
    }

    @Override
    public List<String> getPuzzleFilesByImageName(String imageName) {
        File dir = new File(imagesDir + imageName);
        return Arrays.stream(dir.listFiles())
                .filter(File::isFile)
                .map(File::getPath)
                .collect(Collectors.toList());
    }
}
