package com.kodilla.checkers.io;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Piece {
    private boolean empty;
    private boolean color;
    private boolean crown;

    public Piece() {
        empty = true;
        color = false;
        crown = false;
    }

    public Piece(Piece piece) {
        empty = piece.empty;
        color = piece.color;
        crown = piece.crown;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean color() {
        return color;
    }

    public boolean isCrown() {
        return crown;
    }

    public void  setWhite() {
        empty = false;
        color = false;
        crown = false;
    }

    public void  setBlack() {
        empty = false;
        color = true;
        crown = false;
    }

    public void setCrown() {
        crown = true;
    }

    public void setEmpty() {
        empty = true;
    }

    public void draw(GraphicsContext graphicsContext, double x, double y, double margin, double unitLength) {
        if (empty)
            return;

        if (color)
            graphicsContext.setFill(Color.BLACK);
        else graphicsContext.setFill(Color.WHITE);

        graphicsContext.fillOval(x + margin * unitLength, y + margin * unitLength,
                (1 - 2 * margin) * unitLength, (1 - 2 * margin) * unitLength);

        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.strokeOval(x + margin * unitLength, y + margin * unitLength,
                (1 - 2 * margin) * unitLength, (1 - 2 * margin) * unitLength);

        if (color)
            graphicsContext.setStroke(Color.WHITE);
        if (crown)
            graphicsContext.strokeOval(x + 2*margin*unitLength, y + 2*margin*unitLength,
                    (1 - 4*margin) * unitLength, (1 - 4*margin) * unitLength);
    }
}
