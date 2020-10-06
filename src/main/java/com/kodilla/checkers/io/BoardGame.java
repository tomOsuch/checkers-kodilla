package com.kodilla.checkers.io;

import com.kodilla.checkers.logic.Board;
import com.kodilla.checkers.logic.BoardPosition;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.*;

public class BoardGame {

    private final double pieceMargin;
    private final double startX;
    private final double startY;
    private final double sideLength;
    private final double unitLength;
    private Board board;
    private List<BoardPosition> legalPos;
    boolean lastColor;
    boolean gameOver;
    boolean opponentSet;

    public BoardGame(double startX, double startY, double sideLength, double pieceMargin, int sideCount, int startCount) {
        this.startX = startX;
        this.startY = startY;
        this.sideLength = sideLength;
        this.pieceMargin = pieceMargin;
        this.unitLength = sideLength / sideCount;
        board = new Board(sideCount, startCount);
        legalPos = new ArrayList<>();
        lastColor = true;
        gameOver = false;
        opponentSet = false;
    }

    public BoardGame(BoardGame boardGame) {
        startX = boardGame.startX;
        startY = boardGame.startY;
        sideLength = boardGame.sideLength;
        pieceMargin = boardGame.pieceMargin;
        unitLength = boardGame.unitLength;
        board = new Board(boardGame.board);
        legalPos = new ArrayList<>(boardGame.legalPos);
        lastColor = boardGame.lastColor;
        gameOver = boardGame.gameOver;
        opponentSet = boardGame.opponentSet;
    }


    public void update(BoardGame boardGame) {
        board = new Board(boardGame.board);
        legalPos = new ArrayList<>(boardGame.legalPos);
        lastColor = boardGame.lastColor;
        gameOver = boardGame.gameOver;
        opponentSet = boardGame.opponentSet;
    }

    public void reset() {
        board.reset();
        lastColor = true;
        gameOver = false;
        opponentSet = false;
    }

    public void highlightMoves(BoardPosition from) {

        List<BoardPosition> longest = longestAvailableMoves(2, !lastColor);

        if (longest.isEmpty() && from.inBounds(board.side()) &&
                !board.get(from).isEmpty() && board.get(from).color() != lastColor)
            legalPos = getMoves(from);

        else for (BoardPosition strike : longest)
            legalPos.addAll(getMoves(strike));
    }

    public void attemptMove(BoardPosition to) {
        if (legalPos.contains(to)) {
            lastColor = !lastColor;
            board.set(to, board.get(legalPos.get(legalPos.indexOf(to)).getRouteLast()));

            for (BoardPosition step : legalPos.get(legalPos.indexOf(to)).getRoute())
                board.get(step).setEmpty();

            findCrown();
        }

        legalPos.clear();
    }

    public void draw(GraphicsContext graphicsContext) {
        graphicsContext.setFill(Color.LIGHTSALMON);
        for (int i = 0; i < board.side(); i++)
            for (int j = (i % 2 == 0) ? 1 : 0; j < board.side(); j += 2)
                graphicsContext.fillRect(startX + j * unitLength, startY + i * unitLength, unitLength, unitLength);

        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.strokeRect(startX, startY, sideLength, sideLength);

        for (BoardPosition pos : legalPos) {
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
                board.get(i, j).draw(graphicsContext, startX + i * unitLength,
                        startY + j * unitLength, pieceMargin, unitLength);
    }

    public String message() {
        if (!longestAvailableMoves(2, !lastColor).isEmpty())
            return "Strike available";
        else if (isGameOver())
            return "Game over! Click somewhere to continue";
        else return "Turn: " + (lastColor ? "White" : "Black");
    }


    public boolean someLegalPos() {
        return !legalPos.isEmpty();
    }


    public boolean isGameOverDelayed() {
        return gameOver;
    }

    public boolean getLastColor() {
        return lastColor;
    }


    public BoardPosition decodeMouse(double mouseX, double mouseY) {
        if (mouseX > startX && mouseY > startY && mouseX < startX + sideLength &&
                mouseY < startY + sideLength) // range check
            return new BoardPosition( (int)((mouseX - startX) / unitLength),
                    (int)((mouseY - startY) / unitLength ));
        else return null;
    }


    public List<BoardPosition> getMoves(BoardPosition from) {
        List<BoardPosition> result;

        if (board.get(from).isCrown())
            result = getStrikesCrown(from);
        else result = getStrikes(from);

        final int[] shifts = {-1, 1};
        if (result.isEmpty() && !board.get(from).isEmpty()) {
            if (board.get(from).isCrown())
                for (int shiftX : shifts)
                    for (int shiftY : shifts) {
                        BoardPosition to = from.add(shiftX, shiftY);
                        while (to.inBounds(board.side()) && board.get(to).isEmpty()) {
                            result.add(to);
                            to = to.add(shiftX, shiftY);
                        }
                    }
            else for (int shift : shifts) {
                BoardPosition move = from.add(new BoardPosition(shift,
                        board.get(from).color() ? 1 : -1));
                if (board.get(move) != null && board.get(move).isEmpty())
                    result.add(new BoardPosition(move));
            } }

        for (BoardPosition pos : result)
            pos.addToRoute(new BoardPosition(from));

        return result;
    }

    private List<BoardPosition> getStrikes(BoardPosition from) {
        Queue<BoardPosition> search = new LinkedList<>(); search.add(from);
        List<BoardPosition> result = new ArrayList<>();
        final int[] offsets = {-2, 2};

        while (!search.isEmpty()) {
            boolean finalPos = true;
            for (int offX : offsets)
                for (int offY : offsets) {
                    BoardPosition to = new BoardPosition(search.peek().getX() + offX,
                            search.peek().getY() + offY);
                    to.setRoute(search.peek().getRoute());

                    if (to.inBounds(board.side()) && board.get(to).isEmpty() &&
                            !board.get(to.avg(search.peek())).isEmpty() &&
                            board.get(from).color() !=
                                    board.get(to.avg(search.peek())).color() &&
                            !to.getRoute().contains(to.avg(search.peek()))) {
                        to.addToRoute(new BoardPosition(to.avg(search.peek())));
                        search.add(to);
                        finalPos = false;
                    }
                }

            if (finalPos && !search.peek().equals(from))
                result.add(search.peek());
            search.poll();
        }

        return filterShorter(result);
    }

    private List<BoardPosition> getStrikesCrown(BoardPosition from) {
        Queue<BoardPosition> search = new LinkedList<>(); search.add(from);
        List<BoardPosition> result = new ArrayList<>();
        final int[] direction = {-1, 1};

        while (!search.isEmpty()) {
            boolean finalPos = true;
            for (int dirX : direction)
                for (int dirY : direction) {
                    BoardPosition pos = search.peek().add(dirX, dirY);
                    BoardPosition strike = null;
                    pos.setRoute(new ArrayList<>(search.peek().getRoute()));

                    while (pos.inBounds(board.side()) &&
                            (board.get(pos).isEmpty() ||
                                    (pos.add(dirX, dirY).inBounds(board.side()) &&
                                            board.get(pos.add(dirX, dirY)).isEmpty() &&
                                            board.get(from).color() != board.get(pos).color()))) {
                        if (!board.get(pos).isEmpty() && board.get(from).color()
                                != board.get(pos).color() && !pos.getRoute().contains(pos) &&
                                pos.add(dirX, dirY).inBounds(board.side()) &&
                                board.get(pos.add(dirX, dirY)).isEmpty()) {
                            strike = new BoardPosition(pos);
                            finalPos = false;
                            pos = pos.add(dirX, dirY);
                            pos.addToRoute(strike);
                        }
                        if (strike != null && !pos.equals(strike))
                            search.add(pos);
                        pos = pos.add(dirX, dirY);
                    }
                }
            if (finalPos && !search.peek().equals(from))
                result.add(search.peek());

            search.poll();
        }
        return filterShorter(result);
    }

    public List<BoardPosition> longestAvailableMoves(int minDepth, boolean color) {
        List<BoardPosition> result = new ArrayList<>();

        for (int i = 0; i < board.side(); i++)
            for (int j = 0; j < board.side(); j++)
                if (!board.get(i, j).isEmpty() &&
                        board.get(i, j).color() == color) {
                    List<BoardPosition> legalPos = getMoves(new BoardPosition(i, j));
                    if (!legalPos.isEmpty()) {
                        if (legalPos.get(0).routeLen() > minDepth) {
                            result.clear();
                            minDepth = legalPos.get(0).routeLen();
                        }
                        if (legalPos.get(0).routeLen() == minDepth)
                            result.add(new BoardPosition(i, j));
                    }
                }
        return result;
    }

    private void findCrown() {
        for (int i = 0; i < board.side(); i++) {
            if (!board.get(i, 0).isEmpty() && !board.get(i, 0).color())
                board.get(i, 0).setCrown();
            if (!board.get(i, board.side() - 1).isEmpty() &&
                    board.get(i, board.side() - 1).color())
                board.get(i, board.side() - 1).setCrown();
        }
    }

    private boolean isGameOver() {
        gameOver = longestAvailableMoves(1, true).isEmpty() ||
                longestAvailableMoves(1, false).isEmpty();
        return gameOver;
    }

    private List<BoardPosition> filterShorter(List<BoardPosition> route) {
        int maxDepth = route.isEmpty() ? 0 : route.get(route.size() - 1).routeLen();
        Iterator<BoardPosition> it = route.iterator();

        while (it.hasNext()) {
            BoardPosition pos = it.next();
            if (pos.routeLen() != maxDepth)
                it.remove();
        }

        return route;
    }

    public Board getBoard() {
        return board;
    }

    public boolean isOpponentSet() {
        return opponentSet;
    }

    public boolean turn() {
        return !lastColor;
    }

    public void setOpponent() {
        opponentSet = true;
    }
}
