package com.nikonets.puzzle.model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
public class SolverTile {
    private final BufferedImage tileImage;
    private final List<Edge> edges = initEdges();
    private boolean placed = false;
    private int tableX;
    private int tableY;

    private List<Edge> initEdges() {
        List<Edge> edgesList = new ArrayList<>();
        for (Edge.Side side : Edge.Side.values()) {
            edgesList.add(new Edge(side));
        }
        return edgesList;
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    public class Edge {
        private final Side side;
        private final Map<Edge, Double> compatabilityList = new HashMap<>();
        private int[] pixels;
        private boolean available = true;

        public SolverTile getTile() {
            return SolverTile.this;
        }

        public enum Side {
            TOP,
            BOTTOM,
            LEFT,
            RIGHT
        }
    }
}
