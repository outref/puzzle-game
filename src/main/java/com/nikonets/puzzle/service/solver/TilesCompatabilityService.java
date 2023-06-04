package com.nikonets.puzzle.service.solver;

import com.nikonets.puzzle.model.SolverTile;
import java.util.List;

public interface TilesCompatabilityService {
    List<SolverTile> setAllTilesCompatability(List<SolverTile> tilesList);
}
