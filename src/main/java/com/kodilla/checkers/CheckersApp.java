package com.kodilla.checkers;


import com.kodilla.checkers.io.BoardGame;
import com.kodilla.checkers.logic.PlayerAI;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class CheckersApp extends Application {

    private void drawMessage(GraphicsContext gc, String str, double x, double y, double size) {
        gc.setFont(new Font("Arial", size));
        gc.setFill(Color.BLACK);
        gc.fillText(str, x, y);
    }

    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("Checkers");

        Group root = new Group();
        Scene primaryScene = new Scene(root);
        primaryStage.setScene(primaryScene);

        Canvas canvas = new Canvas(700, 700);
        root.getChildren().add(canvas);

        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        BoardGame board = new BoardGame(50.0, 50.0, 600.0, 0.1, 8, 3);
        PlayerAI aiPlayer = new PlayerAI();
        board.draw(graphicsContext);
        drawMessage(graphicsContext, "Click C-computer or H-human opponent",
                50.0, 40.0, 22.0);

        primaryScene.setOnMouseClicked(
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent e)
                    {
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

                        graphicsContext.clearRect(0, 0, graphicsContext.getCanvas().getWidth(),
                                graphicsContext.getCanvas().getHeight());
                        board.draw(graphicsContext);
                        if (board.isOpponentSet())
                            drawMessage(graphicsContext, board.message(), 50.0, 40.0, 22.0);
                        else
                            drawMessage(graphicsContext, "Click C-computer or H-human player",
                                    50.0, 40.0, 22.0);
                    }
                });

        primaryScene.setOnKeyPressed(
                new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        if (!board.isOpponentSet() && event.getText().equals("h")) {
                            board.setOpponent();
                            aiPlayer.setInactive();
                        }
                        else if (!board.isOpponentSet() && event.getText().equals("c")) {
                            board.setOpponent();
                            aiPlayer.setActive();
                        }

                        graphicsContext.clearRect(0, 0, graphicsContext.getCanvas().getWidth(),
                                graphicsContext.getCanvas().getHeight());
                        board.draw(graphicsContext);
                        if (board.isOpponentSet())
                            drawMessage(graphicsContext, board.message(), 50.0, 40.0, 22.0);
                    }
                });

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
