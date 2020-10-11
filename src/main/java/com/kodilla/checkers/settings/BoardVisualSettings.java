package com.kodilla.checkers.settings;

public class BoardVisualSettings {

    private final double pieceMargin;
    private final double startX;
    private final double startY;
    private final double sideLength;

    public BoardVisualSettings(double pieceMargin, double startX, double startY, double sideLength) {
        this.pieceMargin = pieceMargin;
        this.startX = startX;
        this.startY = startY;
        this.sideLength = sideLength;
    }

    public double getPieceMargin() {
        return pieceMargin;
    }

    public double getStartX() {
        return startX;
    }

    public double getStartY() {
        return startY;
    }

    public double getSideLength() {
        return sideLength;
    }
}
