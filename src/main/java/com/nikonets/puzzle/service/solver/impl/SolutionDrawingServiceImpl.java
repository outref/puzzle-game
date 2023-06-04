package com.nikonets.puzzle.service.solver.impl;

import com.nikonets.puzzle.model.SolverTile;
import com.nikonets.puzzle.service.solver.SolutionDrawingService;
import java.awt.image.BufferedImage;
import org.springframework.stereotype.Service;

@Service
public class SolutionDrawingServiceImpl implements SolutionDrawingService {
    @Override
    public BufferedImage drawSolutionFromTable(SolverTile[][] table) {
        SolverTile[][] croppedTable = cropTableToImageSize(table);

        int tileSideLength = croppedTable[0][0].getTileImage().getHeight();
        BufferedImage solutionImage = new BufferedImage(croppedTable[0].length * tileSideLength,
                croppedTable.length * tileSideLength, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < croppedTable.length; i++) {
            for (int j = 0; j < croppedTable[i].length; j++) {
                SolverTile tile = croppedTable[i][j];
                if (tile != null) {
                    for (int k = 0; k < tile.getTileImage().getWidth(); k++) {
                        for (int l = 0; l < tile.getTileImage().getHeight(); l++) {
                            solutionImage.setRGB(j * tileSideLength + l,
                                    i * tileSideLength + k, tile.getTileImage().getRGB(l, k));
                        }
                    }
                }
            }
        }
        return solutionImage;
    }

    private SolverTile[][] cropTableToImageSize(SolverTile[][] table) {
        Integer topTileY = null;
        Integer bottomTileY = null;
        Integer leftMostTileX = null;
        Integer rightMostTileX = null;

        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                SolverTile tile = table[i][j];
                if (tile != null) {
                    if (topTileY == null) {
                        topTileY = tile.getTableY();
                    }
                    if (bottomTileY == null || i > bottomTileY) {
                        bottomTileY = tile.getTableY();
                    }
                    if (leftMostTileX == null || j < leftMostTileX) {
                        leftMostTileX = tile.getTableX();
                    }
                    if (rightMostTileX == null || j > rightMostTileX) {
                        rightMostTileX = tile.getTableX();
                    }
                }
            }
        }

        SolverTile[][] croppedTable =
                new SolverTile[bottomTileY - topTileY + 1][rightMostTileX - leftMostTileX + 1];
        for (int i = 0; i < croppedTable.length; i++) {
            for (int j = 0; j < croppedTable[i].length; j++) {
                croppedTable[i][j] = table[i + topTileY][j + leftMostTileX];
            }
        }

        return croppedTable;
    }
}
