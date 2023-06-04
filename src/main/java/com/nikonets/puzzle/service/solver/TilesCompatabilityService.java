package com.nikonets.puzzle.service.solver;

import com.nikonets.puzzle.model.SolverTile;
import java.util.List;

public interface TilesCompatabilityService {
    List<SolverTile> setTilesCompatabilityBySides(List<SolverTile> tilesList,
                                                  SolverTile.Edge.Side side1,
                                                  SolverTile.Edge.Side side2);
}
