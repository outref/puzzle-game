package com.nikonets.puzzle.service.solver.impl;

import com.nikonets.puzzle.model.SolverTile;
import com.nikonets.puzzle.service.solver.SolverTileCreatorService;
import java.awt.image.BufferedImage;
import org.springframework.stereotype.Service;

@Service
public class SolverTileCreatorServiceImpl implements SolverTileCreatorService {
    @Override
    public SolverTile createSolverTile(BufferedImage image) {
        SolverTile solverTile = new SolverTile(image);
        setEdgesPixels(solverTile);
        return solverTile;
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
                default -> {
                }
            }
        }
    }
}
