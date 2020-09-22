package com.kodilla.checkers;


import com.kodilla.checkers.gameio.BoardIO;
import com.kodilla.checkers.gamelogic.PlayerAI;
import com.kodilla.checkers.gamelogic.BoardLogic;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class CheckersApp extends Application {

    private BoardLogic boardLogic;


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Checkers");

        Group root = new Group();
        Scene primaryScene = new Scene(root);
        primaryStage.setScene(primaryScene);

        Canvas canvas = new Canvas(700, 700);
        root.getChildren().add(canvas);

        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        BoardIO boardIO = new BoardIO(50.0, 50.0, 600.0, 0.1, 8, 3);
        PlayerAI playerAI = new PlayerAI();
        boardIO.paintingTheGameBoard(graphicsContext);

        primaryScene.setOnMouseClicked(
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent e)
                    {
                        if (!boardIO.isOpponentSet())
                            return;
                        if (boardIO.isGameOver())
                            boardIO.reset();

                        if (boardIO.turn() && playerAI.isActive())
                            playerAI.runTurn(boardIO);
                        else if (boardIO.someLegalPos())
                            boardIO.attemptMove(boardIO.decodeMouse(e.getX(), e.getY()));
                        else
                            boardIO.highlightMoves(boardIO.decodeMouse(e.getX(), e.getY()));

                        graphicsContext.clearRect(0, 0, graphicsContext.getCanvas().getWidth(),
                                graphicsContext.getCanvas().getHeight());
                        boardIO.paintingTheGameBoard(graphicsContext);


                    }
                });

        primaryScene.setOnKeyPressed(
                new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        if (!boardIO.isOpponentSet() && event.getText().equals("h")) {
                            boardIO.setOpponentSet();
                            playerAI.setInactive();
                            System.out.println("Ok");
                        }
                        else if (!boardIO.isOpponentSet() && event.getText().equals("c")) {
                            boardIO.setOpponentSet();
                            playerAI.setActive();
                        }

                        graphicsContext.clearRect(0, 0, graphicsContext.getCanvas().getWidth(),
                                graphicsContext.getCanvas().getHeight());
                        boardIO.paintingTheGameBoard(graphicsContext);
                        if (boardIO.isOpponentSet())
                            System.out.printf("lost");
                    }
                });

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
