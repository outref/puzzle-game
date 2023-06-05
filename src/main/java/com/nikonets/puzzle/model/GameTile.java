package com.nikonets.puzzle.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class GameTile {
    private final Integer correctPos;
    private Integer currentPos;
    private Integer rotation;
    private String imageUrl;
}
