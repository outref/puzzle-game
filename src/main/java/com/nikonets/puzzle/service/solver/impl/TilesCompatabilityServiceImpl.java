package com.nikonets.puzzle.service.solver.impl;

import com.nikonets.puzzle.model.SolverTile;
import com.nikonets.puzzle.service.solver.TilesCompatabilityService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TilesCompatabilityServiceImpl implements TilesCompatabilityService {
    private static final double PIXEL_MAX_VALUE = 16777216;

    @Override
    public List<SolverTile> setAllTilesCompatability(List<SolverTile> tilesList) {
        //bottom+top
        for (SolverTile tile1 : tilesList) {
            SolverTile.Edge bottom = tile1.getEdges().stream()
                    .filter(e -> e.getSide() == SolverTile.Edge.Side.BOTTOM)
                    .findFirst()
                    .get();
            for (SolverTile tile2 : tilesList) {
                if (tile2 != tile1) {
                    SolverTile.Edge top = tile2.getEdges().stream()
                            .filter(e -> e.getSide() == SolverTile.Edge.Side.TOP)
                            .findFirst()
                            .get();
                    double compatability = calculateEdgesCompatability(bottom, top);
                    bottom.getCompatabilityList().put(top, compatability);
                    top.getCompatabilityList().put(bottom, compatability);
                }
            }
        }

        //right+left
        for (SolverTile tile1 : tilesList) {
            SolverTile.Edge right = tile1.getEdges().stream()
                    .filter(e -> e.getSide() == SolverTile.Edge.Side.RIGHT)
                    .findFirst()
                    .get();
            for (SolverTile tile2 : tilesList) {
                if (tile2 != tile1) {
                    SolverTile.Edge left = tile2.getEdges().stream()
                            .filter(e -> e.getSide() == SolverTile.Edge.Side.LEFT)
                            .findFirst()
                            .get();
                    double compatability = calculateEdgesCompatability(right, left);
                    right.getCompatabilityList().put(left, compatability);
                    left.getCompatabilityList().put(right, compatability);
                }
            }
        }
        return tilesList;
    }

    private double calculateEdgesCompatability(SolverTile.Edge edge1, SolverTile.Edge edge2) {
        int[] edge1Pixels = edge1.getPixels();
        int[] edge2Pixels = edge2.getPixels();
        double sum = 0;
        for (int i = 0; i < edge1Pixels.length; i++) {
            sum += (PIXEL_MAX_VALUE - Math.abs(edge1Pixels[i] - edge2Pixels[i])) / PIXEL_MAX_VALUE;
        }
        return sum / edge1Pixels.length;
    }
}
