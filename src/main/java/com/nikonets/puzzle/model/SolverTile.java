package com.nikonets.puzzle.model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolverTile {
    private final BufferedImage tileImage;
    private final List<Edge> edges = initEdges();
    private boolean placed = false;
    private int tableX;
    private int tableY;
    private String fileName;

    public SolverTile(BufferedImage tileImage) {
        this.tileImage = tileImage;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public BufferedImage getTileImage() {
        return tileImage;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setPlaced(boolean placed) {
        this.placed = placed;
    }

    public boolean isPlaced() {
        return placed;
    }

    public int getTableX() {
        return tableX;
    }

    public void setTableX(int tableX) {
        this.tableX = tableX;
    }

    public int getTableY() {
        return tableY;
    }

    public void setTableY(int tableY) {
        this.tableY = tableY;
    }

    private List<Edge> initEdges() {
        List<Edge> edgesList = new ArrayList<>();
        for (Edge.Side side : Edge.Side.values()) {
            edgesList.add(new Edge(side));
        }
        return edgesList;
    }

    public class Edge {
        private int[] pixels;
        private final Side side;
        private final Map<Edge, Double> compatabilityList = new HashMap<>();
        private boolean available = true;

        public Edge(Side side) {
            this.side = side;
        }

        public int[] getPixels() {
            return pixels;
        }

        public Side getSide() {
            return side;
        }

        public boolean isAvailable() {
            return available;
        }

        public Map<Edge, Double> getCompatabilityList() {
            return compatabilityList;
        }

        public void setPixels(int[] pixels) {
            this.pixels = pixels;
        }

        public void setAvailable(boolean available) {
            this.available = available;
        }

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
