package com.nikonets.puzzle.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class GameTile {
    public static final List<Integer> ROTATION_DEGREES = List.of(0, 90, 180, 270);

    private final Integer correctPos;
    private Integer currentPos;
    private Integer rotation;
    private String imageUrl;
}
