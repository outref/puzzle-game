package com.nikonets.puzzle.repository;

import java.awt.image.BufferedImage;
import java.util.List;

public interface ImageRepository {
    String saveTiles(String imageName, BufferedImage originalImg, BufferedImage[] tileImgs);

    List<String> getAllImages();

    List<String> getTilesUrlsByImageName(String imageName);

    String getOriginalImageByName(String imageName);

    String saveSolution(BufferedImage solution);

    List<String> getAllFilesByImageName(String imageName);
}
