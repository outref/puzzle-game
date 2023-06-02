package com.nikonets.puzzle.service.impl;

import com.nikonets.puzzle.model.SolverTile;
import com.nikonets.puzzle.service.PuzzleSolverService;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PuzzleSolverServiceImpl implements PuzzleSolverService {
    private static final double PIXEL_MAX_VALUE = 16777216;

    @Override
    public String solvePuzzle(MultipartFile[] files) throws IOException {
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
            InputStream is = file.getInputStream();
            BufferedImage tileImage = ImageIO.read(is);
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
        SolverTile[][] table = new SolverTile[tilesList.size() * 2][tilesList.size() * 2];
        setAllTilesCompatability(tilesList);
        placeInitialTwoTilesOnTable(tilesList, table);
        for (int i = 0; i < tilesList.size() - 2; i++) {
            placeNextTileOnTable(tilesList, table);
        }
        SolverTile[][] croppedTable = cropTableToImageSize(table);

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

        File outputFile = new File("images/solution.jpg");
        ImageIO.write(solutionImage, "jpg", outputFile);
        return "images/solution.jpg";
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

    private void placeNextTileOnTable(List<SolverTile> tilesList, SolverTile[][] table) {
        SolverTile.Edge edge1 = tilesList.stream()
                .filter(SolverTile::isPlaced)
                .flatMap(t -> t.getEdges().stream())
                .filter(SolverTile.Edge::isAvailable)
                .max(Comparator.comparingDouble(edge ->
                        edge.getCompatabilityList().entrySet().stream()
                        .filter(e -> !e.getKey().getTile().isPlaced())
                        .map(Map.Entry::getValue)
                        .max(Double::compareTo)
                        .get()))
                .get();
        SolverTile.Edge edge2 = edge1.getCompatabilityList().entrySet().stream()
                .filter(e -> !e.getKey().getTile().isPlaced())
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .get()
                .getKey();

        placeAdjacentOnTable(table, edge1, edge2);
    }

    private void placeInitialTwoTilesOnTable(List<SolverTile> tilesList, SolverTile[][] table) {
        SolverTile.Edge edge1 = tilesList.stream()
                .flatMap(t -> t.getEdges().stream())
                .max(Comparator.comparingDouble(edge ->
                        edge.getCompatabilityList().values().stream()
                        .max(Double::compareTo)
                        .get()))
                .get();
        SolverTile tile1 = edge1.getTile();
        table[table.length / 2][table.length / 2] = tile1;
        tile1.setTableX(table.length / 2);
        tile1.setTableY(table.length / 2);
        tile1.setPlaced(true);
        SolverTile.Edge edge2 = edge1.getCompatabilityList().entrySet().stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .get()
                .getKey();
        placeAdjacentOnTable(table, edge1, edge2);
    }

    private void placeAdjacentOnTable(SolverTile[][] table,
                                      SolverTile.Edge edgePlaced,
                                      SolverTile.Edge edgeNew) {
        edgePlaced.setAvailable(false);
        edgeNew.setAvailable(false);
        SolverTile tilePlaced = edgePlaced.getTile();
        SolverTile tileNew = edgeNew.getTile();
        tileNew.setPlaced(true);
        switch (edgeNew.getSide()) {
            case TOP -> {
                tileNew.setTableX(tilePlaced.getTableX());
                tileNew.setTableY(tilePlaced.getTableY() + 1);
            }
            case BOTTOM -> {
                tileNew.setTableX(tilePlaced.getTableX());
                tileNew.setTableY(tilePlaced.getTableY() - 1);
            }
            case RIGHT -> {
                tileNew.setTableX(tilePlaced.getTableX() - 1);
                tileNew.setTableY(tilePlaced.getTableY());
            }
            case LEFT -> {
                tileNew.setTableX(tilePlaced.getTableX() + 1);
                tileNew.setTableY(tilePlaced.getTableY());
            }
            default -> { }
        }
        closeAdjacentEdges(table, tileNew.getTableX(), tileNew.getTableY());
        table[tileNew.getTableY()][tileNew.getTableX()] = tileNew;
    }

    private void closeAdjacentEdges(SolverTile[][] table, int x, int y) {
        if (table[y - 1][x] != null) {
            table[y - 1][x].getEdges().stream()
                    .filter(e -> e.getSide() == SolverTile.Edge.Side.BOTTOM)
                    .findFirst()
                    .get()
                    .setAvailable(false);
        }
        if (table[y + 1][x] != null) {
            table[y + 1][x].getEdges().stream()
                    .filter(e -> e.getSide() == SolverTile.Edge.Side.TOP)
                    .findFirst()
                    .get()
                    .setAvailable(false);
        }
        if (table[y][x - 1] != null) {
            table[y][x - 1].getEdges().stream()
                    .filter(e -> e.getSide() == SolverTile.Edge.Side.RIGHT)
                    .findFirst()
                    .get()
                    .setAvailable(false);
        }
        if (table[y][x + 1] != null) {
            table[y][x + 1].getEdges().stream()
                    .filter(e -> e.getSide() == SolverTile.Edge.Side.LEFT)
                    .findFirst()
                    .get()
                    .setAvailable(false);
        }
    }

    private void setAllTilesCompatability(List<SolverTile> tilesList) {
        //bottom+top
        for (SolverTile tile1 : tilesList) {
            SolverTile.Edge bottom = tile1.getEdges().stream()
                    .filter(e -> e.getSide() == SolverTile.Edge.Side.BOTTOM)
                    .findFirst()
                    .get();
            for (SolverTile tile2 : tilesList) {
                if (tile2 != tile1) {
                    SolverTile.Edge top = tile2.getEdges().stream()
                            .filter(e -> e.getSide() == SolverTile.Edge.Side.TOP)
                            .findFirst()
                            .get();
                    double compatability = calculateEdgesCompatability(bottom, top);
                    bottom.getCompatabilityList().put(top, compatability);
                    top.getCompatabilityList().put(bottom, compatability);
                }
            }
        }

        //right+left
        for (SolverTile tile1 : tilesList) {
            SolverTile.Edge right = tile1.getEdges().stream()
                    .filter(e -> e.getSide() == SolverTile.Edge.Side.RIGHT)
                    .findFirst()
                    .get();
            for (SolverTile tile2 : tilesList) {
                if (tile2 != tile1) {
                    SolverTile.Edge left = tile2.getEdges().stream()
                            .filter(e -> e.getSide() == SolverTile.Edge.Side.LEFT)
                            .findFirst()
                            .get();
                    double compatability = calculateEdgesCompatability(right, left);
                    right.getCompatabilityList().put(left, compatability);
                    left.getCompatabilityList().put(right, compatability);
                }
            }
        }
    }

    private double calculateEdgesCompatability(SolverTile.Edge edge1, SolverTile.Edge edge2) {
        int[] edge1Pixels = edge1.getPixels();
        int[] edge2Pixels = edge2.getPixels();
        double sum = 0;
        for (int i = 0; i < edge1Pixels.length; i++) {
            sum += (PIXEL_MAX_VALUE - Math.abs(edge1Pixels[i] - edge2Pixels[i])) / PIXEL_MAX_VALUE;
        }
        return sum / edge1Pixels.length;
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









