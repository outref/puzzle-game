package com.nikonets.puzzle.conroller;

import com.nikonets.puzzle.service.PuzzleSolverService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Controller
public class PuzzleSolverController {
    private final PuzzleSolverService puzzleSolverService;

    @GetMapping("/solver")
    public String displaySolverPage() {
        return "solver";
    }

    @PostMapping("/solver")
    public String uploadImage(Model model,
                              @RequestParam String imageName,
                              @RequestParam("files") MultipartFile[] files) throws IOException {
        String solutionUrl = puzzleSolverService.solvePuzzle(files);
        model.addAttribute("solutionUrl", solutionUrl);
        model.addAttribute("msg", "Image solved successfully!");
        return "solver";
    }
}
