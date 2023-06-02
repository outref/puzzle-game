package com.nikonets.puzzle.service.impl;

import com.nikonets.puzzle.model.GameBoard;
import com.nikonets.puzzle.model.GameTile;
import com.nikonets.puzzle.service.PuzzleGameService;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PuzzleGameServiceImpl implements PuzzleGameService {
    @Value("${images.dir}")
    private String imagesDir;

    @Override
    public GameBoard createInitTable(String imageName) {
        File tilesDir = new File(imagesDir + imageName);
        int tilesCount = tilesDir.list().length - 1;

        //positions randomizer
        List<Integer> initPositions = IntStream.range(0, tilesCount)
                .boxed()
                .collect(Collectors.toList());
        Collections.shuffle(initPositions);

        //Creating tiles list
        List<GameTile> tilesList = new ArrayList<>();
        for (int i = 0; i < tilesCount; i++) {
            int randomIndex = new Random().nextInt(GameTile.ROTATION_DEGREES.size());
            int rotationDegrees = GameTile.ROTATION_DEGREES.get(randomIndex);
            tilesList.add(new GameTile(i, initPositions.get(i), rotationDegrees,
                    "/" + imagesDir + imageName + "/" + i + ".jpg"));
        }

        List<List<GameTile>> tilesTable = sortAndCreateTable(tilesList);
        String refImageUrl = "/" + imagesDir + imageName + "/reference.jpg";
        return new GameBoard(refImageUrl, tilesList, tilesTable);
    }

    @Override
    public GameBoard swapPuzzles(GameBoard gameBoard, Integer tile1Pos, Integer tile2Pos) {
        List<GameTile> tilesList = gameBoard.getTilesList();
        GameTile gameTile1 = tilesList.stream()
                .filter(t -> t.getCurrentPos().equals(tile1Pos))
                .findFirst()
                .get();
        GameTile gameTile2 = tilesList.stream()
                .filter(t -> t.getCurrentPos().equals(tile2Pos))
                .findFirst()
                .get();
        gameTile1.setCurrentPos(tile2Pos);
        gameTile2.setCurrentPos(tile1Pos);
        List<List<GameTile>> tilesTable = sortAndCreateTable(tilesList);
        gameBoard.setTilesTable(tilesTable);
        return gameBoard;
    }

    @Override
    public GameBoard rotateRight(GameBoard gameBoard, Integer tilePos) {
        List<GameTile> tilesList = gameBoard.getTilesList();
        GameTile gameTile = tilesList.stream()
                .filter(t -> t.getCurrentPos().equals(tilePos))
                .findFirst()
                .get();
        int rotationIndex = GameTile.ROTATION_DEGREES.indexOf(gameTile.getRotation());
        if (rotationIndex == GameTile.ROTATION_DEGREES.size() - 1) {
            gameTile.setRotation(GameTile.ROTATION_DEGREES.get(0));
        } else {
            gameTile.setRotation(GameTile.ROTATION_DEGREES.get(rotationIndex + 1));
        }
        List<List<GameTile>> tilesTable = sortAndCreateTable(tilesList);
        gameBoard.setTilesTable(tilesTable);
        return gameBoard;
    }

    @Override
    public boolean checkSolution(GameBoard gameBoard) {
        List<GameTile> tilesList = gameBoard.getTilesList();
        boolean result = true;
        for (GameTile gameTile : tilesList) {
            if (gameTile.getCurrentPos() != gameTile.getCorrectPos()
                    || gameTile.getRotation() != 0) {
                result = false;
                break;
            }
        }
        return result;
    }

    private List<List<GameTile>> sortAndCreateTable(List<GameTile> tilesList) {
        tilesList.sort(Comparator.comparingInt(GameTile::getCurrentPos));
        int gridSideLength = (int) Math.sqrt(tilesList.size());
        List<List<GameTile>> tilesTable = new ArrayList<>();
        for (int i = 0; i < gridSideLength; i++) {
            tilesTable.add(new ArrayList<>());
            for (int j = 0; j < gridSideLength; j++) {
                int pos = i * gridSideLength + j;
                tilesTable.get(i).add(tilesList.get(pos));
            }
        }
        return tilesTable;
    }
}
