package com.nikonets.puzzle.service.solver;

import com.nikonets.puzzle.model.SolverTile;
import com.nikonets.puzzle.model.TileEdge;
import java.util.List;

public interface TilesCompatabilityService {
    List<SolverTile> setTilesCompatabilityBySides(List<SolverTile> tilesList,
                                                  TileEdge.Side side1,
                                                  TileEdge.Side side2);
}
