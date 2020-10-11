package com.kodilla.checkers.io;

import com.kodilla.checkers.logic.PlayerAI;
import javafx.scene.Scene;

public class ClickHandler {

    public void handle(Scene primaryScene, Message message, BoardGame board, PlayerAI aiPlayer) throws ClickException {
        primaryScene.setOnMouseClicked(e -> {
            if (!board.isOpponentSet())
                return;
            if (board.isGameOverDelayed())
                board.reset();

            if (board.turn() && aiPlayer.isActive())
                aiPlayer.runTurn(board);
            else if (board.someLegalPos())
                board.attemptMove(board.decodeMouse(e.getX(), e.getY()));
            else
                board.highlightMoves(board.decodeMouse(e.getX(), e.getY()));

            board.getGraphicsContext().clearRect(0, 0, board.getGraphicsContext().getCanvas().getWidth(),
                    board.getGraphicsContext().getCanvas().getHeight());
            board.draw();
            if (board.isOpponentSet())
                message.drawMessage(board.getGraphicsContext(), board.message(), 50.0, 40.0, 22.0);
            else
                message.drawMessage(board.getGraphicsContext(), "Click C-computer or H-human player",
                        50.0, 40.0, 22.0);
        });

        primaryScene.setOnKeyPressed(event -> {
            if (!board.isOpponentSet() && event.getText().equals("h")) {
                board.setOpponent();
                aiPlayer.setInactive();
            }
            else if (!board.isOpponentSet() && event.getText().equals("c")) {
                board.setOpponent();
                aiPlayer.setActive();
            }

            board.getGraphicsContext().clearRect(0, 0, board.getGraphicsContext().getCanvas().getWidth(),
                    board.getGraphicsContext().getCanvas().getHeight());
            board.draw();
            if (board.isOpponentSet())
                message.drawMessage(board.getGraphicsContext(), board.message(), 50.0, 40.0, 22.0);
        });

    }
}
