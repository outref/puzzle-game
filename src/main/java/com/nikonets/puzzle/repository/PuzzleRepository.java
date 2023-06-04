package com.nikonets.puzzle.repository;

import java.awt.image.BufferedImage;
import java.util.List;

public interface PuzzleRepository {
    String savePuzzle(String imageName, BufferedImage originalImg, BufferedImage[] tileImgs);

    List<String> getAllPuzzles();

    List<String> getTilesUrlsByImageName(String imageName);

    String getOriginalImageByName(String imageName);

    String saveSolution(BufferedImage solution);

    List<String> getPuzzleFilesByImageName(String imageName);
}
