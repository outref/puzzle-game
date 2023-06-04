package com.nikonets.puzzle.service.solver;

import com.nikonets.puzzle.model.SolverTile;
import java.awt.image.BufferedImage;

public interface SolverTileCreatorService {
    SolverTile createSolverTile(BufferedImage image);
}
