package com.nikonets.puzzle.service;

import com.nikonets.puzzle.model.GameBoard;

public interface PuzzleGameService {
    GameBoard createInitTable(String imageName);

    GameBoard swapPuzzles(GameBoard gameBoard, Integer tile1Pos, Integer tile2Pos);

    GameBoard rotateRight(GameBoard gameBoard, Integer tilePos);

    boolean checkSolution(GameBoard gameBoard);
}
