package com.nikonets.puzzle.service.solver;

import com.nikonets.puzzle.model.SolverTile;
import java.awt.image.BufferedImage;

public interface SolutionDrawingService {
    BufferedImage drawSolutionFromTable(SolverTile[][] table);
}
