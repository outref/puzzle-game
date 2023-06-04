package com.nikonets.puzzle.service.solver.impl;

import com.nikonets.puzzle.model.SolverTile;
import com.nikonets.puzzle.service.solver.InputTilesProcessingService;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class InputTilesProcessingServiceImpl implements InputTilesProcessingService {

    @Override
    public List<SolverTile> createSolverTilesFromInput(MultipartFile[] files) {
        int sideTilesAmount = (int) Math.sqrt(files.length);
        if ((sideTilesAmount * sideTilesAmount) != files.length) {
            throw new IllegalArgumentException("Tiles must create a square image!"
                    + " (provide 4, 9, 16, 25,... tiles");
        }
        Integer tileSideLength = null;
        List<SolverTile> tilesList = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty() || !file.getContentType().equals("image/jpeg")) {
                throw new IllegalArgumentException("All tiles must be jpg images!");
            }
            BufferedImage tileImage;
            try {
                InputStream is = file.getInputStream();
                tileImage = ImageIO.read(is);
            } catch (IOException e) {
                throw new RuntimeException("Failed to read input file "
                        + file.getOriginalFilename(), e);
            }
            if (tileSideLength == null) {
                tileSideLength = tileImage.getHeight();
            }
            if (tileImage.getHeight() != tileSideLength || tileImage.getWidth() != tileSideLength) {
                throw new IllegalArgumentException("All tiles must be squares of equal size");
            }
            SolverTile solverTile = new SolverTile(tileImage);
            solverTile.setFileName(file.getOriginalFilename());
            setEdgesPixels(solverTile);
            tilesList.add(solverTile);
        }
        return tilesList;
    }

    private void setEdgesPixels(SolverTile tile) {
        for (SolverTile.Edge edge : tile.getEdges()) {
            int width = tile.getTileImage().getWidth();
            int height = tile.getTileImage().getHeight();
            int[] pixels;
            switch (edge.getSide()) {
                case TOP -> {
                    pixels = new int[width];
                    for (int i = 0; i < width; i++) {
                        pixels[i] = tile.getTileImage().getRGB(i, 0);
                    }
                    edge.setPixels(pixels);
                }
                case BOTTOM -> {
                    pixels = new int[width];
                    for (int i = 0; i < width; i++) {
                        pixels[i] = tile.getTileImage().getRGB(i, height - 1);
                    }
                    edge.setPixels(pixels);
                }
                case RIGHT -> {
                    pixels = new int[height];
                    for (int i = 0; i < height; i++) {
                        pixels[i] = tile.getTileImage().getRGB(width - 1, i);
                    }
                    edge.setPixels(pixels);
                }
                case LEFT -> {
                    pixels = new int[height];
                    for (int i = 0; i < height; i++) {
                        pixels[i] = tile.getTileImage().getRGB(0, i);
                    }
                    edge.setPixels(pixels);
                }
                default -> { }
            }
        }
    }
}
