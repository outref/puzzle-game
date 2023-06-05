package com.nikonets.puzzle.service.solver.impl;

import com.nikonets.puzzle.model.SolverTile;
import com.nikonets.puzzle.service.solver.TilesCompatabilityService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TilesCompatabilityServiceImpl implements TilesCompatabilityService {
    private static final double PIXEL_MAX_VALUE = 16777216;

    @Override
    public List<SolverTile> setTilesCompatabilityBySides(List<SolverTile> tilesList,
                                                         SolverTile.Edge.Side side1,
                                                         SolverTile.Edge.Side side2) {
        for (SolverTile tile1 : tilesList) {
            SolverTile.Edge edge1 = tile1.getEdges().stream()
                    .filter(e -> e.getSide() == side1)
                    .findFirst()
                    .get();
            for (SolverTile tile2 : tilesList) {
                if (tile2 != tile1) {
                    SolverTile.Edge edge2 = tile2.getEdges().stream()
                            .filter(e -> e.getSide() == side2)
                            .findFirst()
                            .get();
                    double compatability = calculateEdgesCompatability(edge1, edge2);
                    edge1.getCompatabilityList().put(edge2, compatability);
                    edge2.getCompatabilityList().put(edge1, compatability);
                }
            }
        }
        return tilesList;
    }

    private double calculateEdgesCompatability(SolverTile.Edge edge1, SolverTile.Edge edge2) {
        if (edge1.getPixels().length != edge2.getPixels().length) {
            return 0; //sides of different length = incompatible
        }
        int[] edge1Pixels = edge1.getPixels();
        int[] edge2Pixels = edge2.getPixels();
        double sum = 0;
        for (int i = 0; i < edge1Pixels.length; i++) {
            sum += (PIXEL_MAX_VALUE - Math.abs(edge1Pixels[i] - edge2Pixels[i])) / PIXEL_MAX_VALUE;
        }
        return sum / edge1Pixels.length;
    }
}
