package com.kodilla.checkers.logic;

import com.kodilla.checkers.io.BoardGame;

import java.util.List;

public class PlayerAI implements Ai {

    boolean active;

    public PlayerAI() {
        active = false;
    }

    @Override
    public void runTurn(BoardGame boardGame) {
        GameTreeNode root = new GameTreeNode(new BoardGame(boardGame));
        constructGameTree(root, 3);
        if (!root.getChildren().isEmpty())
            boardGame.update(root.getChildren().get(0).getData());
        return;
    }

    @Override
    public void constructGameTree(GameTreeNode node, int depth) {
        if (depth < 0)
            return;

        List<BoardPosition> moves = node.getData().longestAvailableMoves(1,
                !node.getData().getLastColor());

        for (BoardPosition move : moves) {
            BoardGame newData = new BoardGame(node.getData());
            newData.highlightMoves(move);
            newData.attemptMove(newData.getMoves(move).get(0));
            GameTreeNode newChild = new GameTreeNode(newData);
            constructGameTree(newChild, depth - 1);
            node.addChild(newChild);
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive() {
        active = true;
    }

    public void setInactive() {
        active = false;
    }
}
