package com.nikonets.puzzle.model;

import java.util.List;

public class GameTile {
    public static final List<Integer> ROTATION_DEGREES = List.of(0, 90, 180, 270);

    private int correctPos;
    private int currentPos;
    private int rotation;
    private String rotationStyle;
    private String url;

    public GameTile(int correctPos, int currentPos, int rotation, String url) {
        this.correctPos = correctPos;
        this.currentPos = currentPos;
        this.url = url;
        this.rotation = rotation;
        this.rotationStyle = "transform:rotate(" + rotation + "deg);";
    }

    public int getCorrectPos() {
        return correctPos;
    }

    public String getUrl() {
        return url;
    }

    public Integer getCurrentPos() {
        return currentPos;
    }

    public int getRotation() {
        return rotation;
    }

    public String getRotationStyle() {
        return rotationStyle;
    }

    public void setCurrentPos(Integer currentPos) {
        this.currentPos = currentPos;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
        rotationStyle = "transform:rotate(" + rotation + "deg);";
    }
}
