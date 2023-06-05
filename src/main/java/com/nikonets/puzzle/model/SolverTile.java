package com.nikonets.puzzle.model;

import java.awt.image.BufferedImage;
import java.util.List;
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
    private List<TileEdge> tileEdges;
    private boolean placed = false;
    private int tableX;
    private int tableY;
}
