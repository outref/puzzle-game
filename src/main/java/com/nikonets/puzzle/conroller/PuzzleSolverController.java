package com.nikonets.puzzle.conroller;

import com.nikonets.puzzle.service.solver.PuzzleSolverService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Controller
@RequestMapping("/solver")
public class PuzzleSolverController {
    private final PuzzleSolverService puzzleSolverService;

    @GetMapping
    public String displaySolverPage() {
        return "solver";
    }

    @PostMapping
    public String uploadImage(Model model,
                              @RequestParam("files") MultipartFile[] files) throws IOException {
        String solutionUrl = puzzleSolverService.solvePuzzle(files);
        model.addAttribute("solutionUrl", solutionUrl);
        model.addAttribute("msg", "Image solved successfully!");
        return "solver";
    }
}
