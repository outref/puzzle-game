package com.nikonets.puzzle.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class GameBoard {
    private String originalImageUrl;
    private List<GameTile> tilesList;
    private List<List<GameTile>> tilesTable;
}
