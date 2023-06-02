package com.nikonets.puzzle.service;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface PuzzleSolverService {
    String solvePuzzle(MultipartFile[] tiles) throws IOException;
}
