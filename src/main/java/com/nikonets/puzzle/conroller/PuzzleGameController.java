package com.nikonets.puzzle.conroller;

import com.nikonets.puzzle.model.GameBoard;
import com.nikonets.puzzle.service.PuzzleGameService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@RequestMapping("/game")
public class PuzzleGameController {
    private final PuzzleGameService gameService;

    @GetMapping
    public String loadGame(Model model,
                           HttpSession session,
                           @RequestParam String imageName) {
        GameBoard gameBoard = gameService.createInitTable(imageName);
        session.setAttribute("game-board", gameBoard);
        model.addAttribute("tilesTable", gameBoard.getTilesTable());
        return "game";
    }

    @PostMapping("/swap")
    public String swapPuzzles(Model model,
                              HttpSession session,
                              @RequestParam Integer tile1Pos,
                              @RequestParam Integer tile2Pos) {
        GameBoard gameBoard = (GameBoard) session.getAttribute("game-board");
        String message = null;
        try {
            gameBoard = gameService.swapPuzzles(gameBoard, tile1Pos, tile2Pos);
        } catch (Exception e) {
            message = e.getMessage();
        }
        model.addAttribute("message", message);
        model.addAttribute("tilesTable", gameBoard.getTilesTable());
        return "game";
    }

    @PostMapping("/rotate")
    public String rotateRight(Model model,
                              HttpSession session,
                              @RequestParam Integer tilePos) {
        GameBoard gameBoard = (GameBoard) session.getAttribute("game-board");
        String message = null;
        try {
            gameBoard = gameService.rotateRight(gameBoard, tilePos);
        } catch (Exception e) {
            message = e.getMessage();
        }
        model.addAttribute("message", message);
        model.addAttribute("tilesTable", gameBoard.getTilesTable());
        return "game";
    }

    @PostMapping("/check")
    public String checkSolution(Model model,
                                HttpSession session) {
        GameBoard gameBoard = (GameBoard) session.getAttribute("game-board");
        boolean correctSolution = gameService.checkSolution(gameBoard);
        String message;
        if (correctSolution) {
            message = "Correct!";
        } else {
            message = "Incorrect! See correct image below -->";
        }
        model.addAttribute("message", message);
        model.addAttribute("originalImg", gameBoard.getOriginalImageUrl());
        model.addAttribute("tilesTable", gameBoard.getTilesTable());
        return "game";
    }
}
