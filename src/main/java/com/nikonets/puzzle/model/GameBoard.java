package com.nikonets.puzzle.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GameBoard {
    private String refImageUrl;
    private List<GameTile> tilesList;
    private List<List<GameTile>> tilesTable;
}
