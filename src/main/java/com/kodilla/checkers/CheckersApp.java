package com.kodilla.checkers;


import com.kodilla.checkers.io.BoardGame;
import com.kodilla.checkers.settings.BoardVisualSettings;
import com.kodilla.checkers.io.Message;
import com.kodilla.checkers.io.ClickHandler;
import com.kodilla.checkers.logic.PlayerAI;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class CheckersApp extends Application {


    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("Checkers");

        Message message = new Message();
        Group root = new Group();
        Scene primaryScene = new Scene(root);
        primaryStage.setScene(primaryScene);
        BoardVisualSettings boardSettings = new BoardVisualSettings(0.1, 50, 50, 600);
        BoardGame board = new BoardGame(boardSettings);
        root.getChildren().add(board.getCanvas());
        PlayerAI aiPlayer = new PlayerAI();

        board.draw();
        message.drawMessage(board.getGraphicsContext(), "Click C-computer or H-human opponent",
                50.0, 40.0, 22.0);
        ClickHandler clickHandler = new ClickHandler();

        clickHandler.handle(primaryScene, message, board, aiPlayer);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
