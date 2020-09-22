package com.kodilla.checkers.gamelogic;

import com.kodilla.checkers.gameio.BoardIO;

import java.util.*;

public class BoardLogic {



    private Board board;



    public List<BoardPosition> getMove(BoardPosition boardPosition) {
        List<BoardPosition> result;

        if (board.get(boardPosition).isCrown()) result = getStrikesCrown(boardPosition);
        else result = getStrikes(boardPosition);

        final int[] shifts = {-1, 1};

        if (result.isEmpty() && !board.get(boardPosition).isEmpty()) {
            if (board.get(boardPosition).isCrown()) {
                for (int shiftX : shifts) {
                    for (int shiftY : shifts) {
                        BoardPosition to = boardPosition.add(shiftX, shiftY);
                        while (to.inBounds(board.side()) && board.get(to).isEmpty()) {
                            result.add(to);
                            to = to.add(shiftX, shiftY);
                        }
                    }
                }
            } else for (int shift : shifts) {
                BoardPosition move = boardPosition.add(new BoardPosition(shift, board.get(boardPosition).color() ? 1 : -1));
                if (board.get(move) != null && board.get(move).isEmpty()) {
                    result.add(new BoardPosition(move));
                }
            }
        }

        for (BoardPosition pos : result) {
            pos.addToRoute(new BoardPosition(boardPosition));
        }

        return result;
    }

    public List<BoardPosition> longestAvailableMoves(int minDepth, boolean color) {
        List<BoardPosition> result = new ArrayList<>();

        for (int i = 0; i < board.side(); i++)
            for (int j = 0; j < board.side(); j++)
                if (!board.get(i, j).isEmpty() &&
                        board.get(i, j).color() == color) {
                    List<BoardPosition> _legalPos = getMove(new BoardPosition(i, j));

                    if (!_legalPos.isEmpty()) {
                        if (_legalPos.get(0).routeLength() > minDepth) {
                            result.clear();
                            minDepth = _legalPos.get(0).routeLength();
                        }
                        if (_legalPos.get(0).routeLength() == minDepth){
                            result.add(new BoardPosition(i, j));
                        }
                    }
                }
        return result;
    }

    // ------------------ metody prywatne ---------------------

    private List<BoardPosition> getStrikes(BoardPosition from) {

        Queue<BoardPosition> search = new LinkedList<>();
        search.add(from);
        List<BoardPosition> result = new ArrayList<>();
        final int[] offsets = {-2, 2};

        while (!search.isEmpty()) {
            boolean finalPosition = true;

            for (int offsetX : offsets) {
                for (int offsetY : offsets) {
                    BoardPosition to = new BoardPosition(search.peek().getX() + offsetX, search.peek().getY() + offsetY);
                    to.setRoute(search.peek().getRoute());

                    if (to.inBounds(board.side()) && board.get(to).isEmpty() && !board.get(to.avg(search.peek())).isEmpty() &&
                        board.get(from).color() != board.get(to.avg(search.peek())).color() &&
                        !to.getRoute().contains(to.avg(search.peek()))) {
                        to.addToRoute(new BoardPosition(to.avg(search.peek())));
                        search.add(to);
                        finalPosition = false;
                    }
                }
            }

            if (finalPosition && !search.peek().equals(from)) {
                result.add(search.peek());
            }
            search.poll();
        }

        return result;
    }

    private List<BoardPosition> getStrikesCrown(BoardPosition from) {
        Queue<BoardPosition> search = new LinkedList<>();
        List<BoardPosition> result = new ArrayList<>();
        final int[] direction = {-1, 1};

        while (!search.isEmpty()) {
            boolean finalPosition = true;
            for (int dirX : direction) {
                for (int dirY : direction) {
                    BoardPosition position = search.peek().add(dirX, dirY);
                    BoardPosition strike = null;

                    position.setRoute(new ArrayList<>(search.peek().getRoute()));

                    while (position.inBounds(board.side())&& (board.get(position).isEmpty() || (position.add(dirX, dirY).inBounds(board.side()) &&
                            board.get(position.add(dirX, dirY)).isEmpty() && board.get(from).color() != board.get(position).color()))) {

                        if (!board.get(position).isEmpty() && board.get(from).color() != board.get(position).color() && !position.getRoute().contains(position) &&
                                position.add(dirX, dirY).inBounds(board.side()) && board.get(position.add(dirX, dirY)).isEmpty()) {
                            strike = new BoardPosition(position);
                            finalPosition = false;
                            position = position.add(dirX, dirY);
                            position.addToRoute(strike);
                        }
                    }
                }
            }
            if (finalPosition && !search.peek().equals(from)) {
                result.add(search.peek());
            }
            search.poll();
        }

        return result;
    }
}
