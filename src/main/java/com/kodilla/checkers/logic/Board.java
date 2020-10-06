package com.kodilla.checkers.logic;

import com.kodilla.checkers.io.Piece;

public class Board {

    private final Piece[][] pieces;
    private final int startCount;
    private final int sideCount;

    public Board(int sideCount, int startCount) {
        this.startCount = startCount;
        this.sideCount = sideCount;

        pieces = new Piece[sideCount][sideCount];
        for (int i = 0; i < pieces.length; i++)
            for (int j = 0; j < pieces[i].length; j++)
                pieces[i][j] = new Piece();

        for (int j = 0; j < startCount; j++)
            for (int i = (j % 2 == 0) ? 1 : 0; i < sideCount; i += 2) {
                pieces[i][j].setBlack();
                pieces[sideCount - 1 - i][sideCount - 1 - j].setWhite();
            }
    }

    public Board(Board board) {
        startCount = board.startCount;
        sideCount = board.sideCount;

        pieces = new Piece[sideCount][sideCount];
        for (int i = 0; i < pieces.length; i++)
            for (int j = 0; j < pieces[i].length; j++)
                pieces[i][j] = new Piece(board.pieces[i][j]);
    }

    public void reset() {
        for (Piece[] piece : pieces)
            for (Piece value : piece) value.setEmpty();

        for (int j = 0; j < startCount; j++)
            for (int i = (j % 2 == 0) ? 1 : 0; i < pieces.length; i += 2) {
                pieces[i][j].setBlack();
                pieces[pieces.length - 1 - i][pieces.length - 1 - j].setWhite();
            }
    }

    public Piece get(BoardPosition position) {
        if (position.inBounds(pieces.length)) {
            return pieces[position.getX()][position.getY()];
        } else {
            return null;
        }
    }

    public Piece get(int x, int y) {
        return get(new BoardPosition(x, y));
    }

    public int side() {
        return pieces.length;
    }

    public void set(BoardPosition boardPosition, Piece piece) {
        pieces[boardPosition.getX()][boardPosition.getY()] = new Piece(piece);
    }

}
