package com.nikonets.puzzle.service.impl;

import com.nikonets.puzzle.exception.GameException;
import com.nikonets.puzzle.model.GameBoard;
import com.nikonets.puzzle.model.GameTile;
import com.nikonets.puzzle.repository.PuzzleRepository;
import com.nikonets.puzzle.service.PuzzleGameService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PuzzleGameServiceImpl implements PuzzleGameService {
    public static final List<Integer> ROTATION_DEGREES = List.of(0, 90, 180, 270);
    private final PuzzleRepository repository;

    @Override
    public GameBoard createInitTable(String imageName) {
        List<String> tileImagesList = repository.getTilesUrlsByImageName(imageName);
        String originalImg = repository.getOriginalImageByName(imageName);

        //positions randomizer
        List<Integer> initPositions = IntStream.range(0, tileImagesList.size())
                .boxed()
                .collect(Collectors.toList());
        Collections.shuffle(initPositions);

        //Creating tiles list
        List<GameTile> tilesList = new ArrayList<>();
        for (int i = 0; i < tileImagesList.size(); i++) {
            int randomIndex = new Random().nextInt(ROTATION_DEGREES.size());
            int rotationDegrees = ROTATION_DEGREES.get(randomIndex);
            tilesList.add(new GameTile(i, initPositions.get(i),
                    rotationDegrees, tileImagesList.get(i)));
        }

        List<List<GameTile>> tilesTable = sortAndCreateSquareTable(tilesList);
        return new GameBoard(originalImg, tilesList, tilesTable);
    }

    @Override
    public GameBoard swapPuzzles(GameBoard gameBoard, Integer tile1Pos, Integer tile2Pos) {
        List<GameTile> tilesList = gameBoard.getTilesList();
        GameTile gameTile1 = tilesList.stream()
                .filter(t -> t.getCurrentPos().equals(tile1Pos))
                .findFirst()
                .orElseThrow(() -> new GameException("No such position " + tile1Pos));
        GameTile gameTile2 = tilesList.stream()
                .filter(t -> t.getCurrentPos().equals(tile2Pos))
                .findFirst()
                .orElseThrow(() -> new GameException("No such position " + tile2Pos));
        gameTile1.setCurrentPos(tile2Pos);
        gameTile2.setCurrentPos(tile1Pos);
        List<List<GameTile>> tilesTable = sortAndCreateSquareTable(tilesList);
        gameBoard.setTilesTable(tilesTable);
        return gameBoard;
    }

    @Override
    public GameBoard rotateRight(GameBoard gameBoard, Integer tilePos) {
        List<GameTile> tilesList = gameBoard.getTilesList();
        GameTile gameTile = tilesList.stream()
                .filter(t -> t.getCurrentPos().equals(tilePos))
                .findFirst()
                .orElseThrow(() -> new GameException("No such position " + tilePos));
        int rotationIndex = ROTATION_DEGREES.indexOf(gameTile.getRotation());
        if (rotationIndex == ROTATION_DEGREES.size() - 1) {
            gameTile.setRotation(ROTATION_DEGREES.get(0));
        } else {
            gameTile.setRotation(ROTATION_DEGREES.get(rotationIndex + 1));
        }
        List<List<GameTile>> tilesTable = sortAndCreateSquareTable(tilesList);
        gameBoard.setTilesTable(tilesTable);
        return gameBoard;
    }

    @Override
    public boolean checkSolution(GameBoard gameBoard) {
        boolean result = true;
        for (GameTile gameTile : gameBoard.getTilesList()) {
            if (!gameTile.getCurrentPos().equals(gameTile.getCorrectPos())
                    || gameTile.getRotation() != 0) {
                result = false;
                break;
            }
        }
        return result;
    }

    private List<List<GameTile>> sortAndCreateSquareTable(List<GameTile> tilesList) {
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
