package com.nikonets.puzzle.service.solver.impl;

import com.nikonets.puzzle.model.SolverTile;
import com.nikonets.puzzle.model.TileEdge;
import com.nikonets.puzzle.service.solver.TilesCompatabilityService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TilesCompatabilityServiceImpl implements TilesCompatabilityService {
    private static final double PIXEL_MAX_VALUE = 16777216;

    @Override
    public List<SolverTile> setTilesCompatabilityBySides(List<SolverTile> tilesList,
                                                         TileEdge.Side side1,
                                                         TileEdge.Side side2) {
        for (SolverTile tile1 : tilesList) {
            TileEdge tileEdge1 = tile1.getTileEdges().stream()
                    .filter(e -> e.getSide() == side1)
                    .findFirst()
                    .get();
            for (SolverTile tile2 : tilesList) {
                if (tile2 != tile1) {
                    TileEdge tileEdge2 = tile2.getTileEdges().stream()
                            .filter(e -> e.getSide() == side2)
                            .findFirst()
                            .get();
                    double compatability = calculateEdgesCompatability(tileEdge1, tileEdge2);
                    tileEdge1.getCompatabilityList().put(tileEdge2, compatability);
                    tileEdge2.getCompatabilityList().put(tileEdge1, compatability);
                }
            }
        }
        return tilesList;
    }

    private double calculateEdgesCompatability(TileEdge tileEdge1, TileEdge tileEdge2) {
        if (tileEdge1.getPixels().length != tileEdge2.getPixels().length) {
            return 0; //sides of different length = incompatible
        }
        int[] edge1Pixels = tileEdge1.getPixels();
        int[] edge2Pixels = tileEdge2.getPixels();
        double sum = 0;
        for (int i = 0; i < edge1Pixels.length; i++) {
            sum += (PIXEL_MAX_VALUE - Math.abs(edge1Pixels[i] - edge2Pixels[i])) / PIXEL_MAX_VALUE;
        }
        return sum / edge1Pixels.length;
    }
}
