package com.kodilla.checkers.io;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Message {

    public void drawMessage(GraphicsContext gc, String str, double x, double y, double size) {
        gc.setFont(new Font("Arial", size));
        gc.setFill(Color.BLACK);
        gc.fillText(str, x, y);
    }
}
