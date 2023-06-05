package com.nikonets.puzzle.model;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class TileEdge {
    private final SolverTile tile;
    private final Side side;
    private final Map<TileEdge, Double> compatabilityList = new HashMap<>();
    private int[] pixels;
    private boolean available = true;

    public enum Side {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT
    }
}
