package com.kodilla.checkers.gameio;


import com.kodilla.checkers.gamelogic.Board;
import com.kodilla.checkers.gamelogic.BoardLogic;
import com.kodilla.checkers.gamelogic.BoardPosition;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class BoardIO {

    BoardLogic boardLogic = new BoardLogic();

    private final double pieceMargin;
    private final double startX;
    private final double startY;
    private final double sideLength;
    private final double unitLength;
    private Board board;
    private List<BoardPosition> positionList;
    boolean lastColor;
    boolean gameOver;
    boolean opponentSet;

    public BoardLogic getBoardLogic() {
        return boardLogic;
    }

    public BoardIO(double startX, double startY, double sideLength, double pieceMargin, int sideCount, int startCount) {
        this.startX = startX;
        this.startY = startY;
        this.sideLength = sideLength;
        this.pieceMargin = pieceMargin;
        unitLength = sideLength / sideCount;
        board = new Board(sideCount, startCount);
        positionList = new ArrayList<>();
        lastColor = true;
        gameOver = false;
        opponentSet = false;
    }

    public BoardIO(BoardIO boardIO) {
        startX = boardIO.startX;
        startY = boardIO.startY;
        sideLength = boardIO.sideLength;
        pieceMargin = boardIO.pieceMargin;
        unitLength = boardIO.unitLength;
        board = new Board(boardIO.board);
        positionList = new ArrayList<>(boardIO.positionList);
        lastColor = boardIO.lastColor;
        gameOver = boardIO.gameOver;
        opponentSet = boardIO.opponentSet;
    }

    public void update(BoardIO boardIO) {
        board = new Board(boardIO.board);
        positionList = new ArrayList<>(boardIO.positionList);
        lastColor = boardIO.lastColor;
        gameOver = boardIO.gameOver;
        opponentSet = boardIO.opponentSet;
    }

    public void reset() {
        board.reset();
        lastColor = true;
        gameOver = false;
        opponentSet = false;
    }

    public void highlightMoves(BoardPosition from) {
        List<BoardPosition> longest = boardLogic.longestAvailableMoves(2, isLastColor());

        if (longest.isEmpty() && from.inBounds(board.side()) && !board.get(from).isEmpty() && board.get(from).color() != isLastColor()) {
            positionList = boardLogic.getMove(from);
        } else {
            for (BoardPosition strike : longest) {
                positionList.addAll(boardLogic.getMove(strike));
            }
        }
    }

    public void attemptMove(BoardPosition to) {
        if (positionList.contains(to)) {
            lastColor = !lastColor;
            board.set(to, board.get(positionList.get(positionList.indexOf(to)).getRouteLast()));
            for (BoardPosition step : positionList.get(positionList.indexOf(to)).getRoute()) {
                board.get(step).setEmpty();
            }
        }
        positionList.clear();
    }

    public void paintingTheGameBoard(GraphicsContext graphicsContext) {
        graphicsContext.setFill(Color.LIGHTSALMON);
        for (int i = 0; i < board.side(); i++)
            for (int j = (i % 2 == 0) ? 1 : 0; j < board.side(); j += 2)
                graphicsContext.fillRect(startX + j * unitLength, startY + i * unitLength, unitLength, unitLength);

        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.strokeRect(startX, startY, sideLength, sideLength);

        for (BoardPosition pos : positionList) {
            graphicsContext.setFill(Color.ORANGE);
            graphicsContext.fillRect(startX + pos.getX() * unitLength,
                    startY + pos.getY() * unitLength, unitLength, unitLength);
            graphicsContext.setFill(Color.LIGHTYELLOW);
            if (pos.route != null)
                for (BoardPosition step : pos.route)
                    graphicsContext.fillRect(startX + step.getX() * unitLength,
                            startY + step.getY() * unitLength, unitLength, unitLength);
        }

        for (int i = 0; i < board.side(); i++)
            for (int j = 0; j < board.side(); j++)
                board.get(i, j).paintingThePiece(graphicsContext, startX + i * unitLength,
                        startY + j * unitLength, pieceMargin, unitLength);
    }

    public boolean someLegalPos() {
        return !positionList.isEmpty();
    }

    public Board getBoard() {
        return board;
    }

    public boolean isLastColor() {
        return lastColor;
    }

    public boolean isOpponentSet() {
        return opponentSet;
    }

    public boolean turn() {
        return !lastColor;
    }

    public void setOpponentSet() {
        opponentSet = true;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public List<BoardPosition> getPositionList() {
        return positionList;
    }

    public boolean getLastColor() {
        return lastColor;
    }

    public BoardPosition decodeMouse(double mouseX, double mouseY) {
        if (mouseX > startX && mouseY > startY && mouseX < startX + sideLength && mouseY < startY + sideLength) {
            return new BoardPosition( (int)((mouseX - startX) / unitLength), (int)((mouseY - startY)/ unitLength));
        } else {
            return null;
        }
    }
}
