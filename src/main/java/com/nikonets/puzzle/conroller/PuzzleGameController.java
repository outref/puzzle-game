package com.nikonets.puzzle.conroller;

import com.nikonets.puzzle.model.GameBoard;
import com.nikonets.puzzle.service.PuzzleGameService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class PuzzleGameController {
    private final PuzzleGameService gameService;

    @GetMapping("/game")
    public String loadGame(Model model,
                           HttpSession session,
                           @RequestParam String imageName) {
        GameBoard gameBoard = gameService.createInitTable(imageName);
        session.setAttribute("game-board", gameBoard);
        model.addAttribute("tilesTable", gameBoard.getTilesTable());
        return "game";
    }

    @PostMapping("/game/swap")
    public String swapPuzzles(Model model,
                              HttpSession session,
                              @RequestParam Integer tile1Pos,
                              @RequestParam Integer tile2Pos) {
        GameBoard gameBoard = (GameBoard) session.getAttribute("game-board");
        gameBoard = gameService.swapPuzzles(gameBoard, tile1Pos, tile2Pos);
        model.addAttribute("tilesTable", gameBoard.getTilesTable());
        return "game";
    }

    @PostMapping("/game/rotate")
    public String rotateRight(Model model,
                          HttpSession session,
                          @RequestParam Integer tilePos) {
        GameBoard gameBoard = (GameBoard) session.getAttribute("game-board");
        gameBoard = gameService.rotateRight(gameBoard, tilePos);
        model.addAttribute("tilesTable", gameBoard.getTilesTable());
        return "game";
    }

    @PostMapping("/game/check")
    public String checkSolution(Model model,
                                HttpSession session) {
        GameBoard gameBoard = (GameBoard) session.getAttribute("game-board");
        boolean correctSolution = gameService.checkSolution(gameBoard);
        String message;
        if (correctSolution) {
            message = "Correct!";
        } else {
            message = "Incorrect!";
        }
        model.addAttribute("msgTop", message);
        model.addAttribute("msgBottom", "Correct image reference:");
        model.addAttribute("referenceImg", gameBoard.getRefImageUrl());
        model.addAttribute("tilesTable", gameBoard.getTilesTable());
        return "game";
    }
}
