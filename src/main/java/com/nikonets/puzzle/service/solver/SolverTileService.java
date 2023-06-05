package com.nikonets.puzzle.service.solver;

import com.nikonets.puzzle.model.SolverTile;
import java.awt.image.BufferedImage;

public interface SolverTileService {
    SolverTile createSolverTile(BufferedImage image);
}
