package com.kodilla.checkers.gamelogic;

import com.kodilla.checkers.gameio.BoardIO;

import java.util.List;

import static java.lang.Math.min;

public class PlayerAI {

    boolean active;

    public PlayerAI() {
        active = false;
    }

    public void runTurn(BoardIO boardIO) {
        GameTreeNode root = new GameTreeNode(new BoardIO(boardIO));
        constructGameTree(root, 3);
        if (!root.getChildren().isEmpty())
            boardIO.update(root.getChildren().get(0).getData());
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

    private void constructGameTree(GameTreeNode node, int depth) {
        if (depth < 0) return;
        List<BoardPosition> moves = node.getData().getBoardLogic().longestAvailableMoves(1, !node.getData().getLastColor());

        for (BoardPosition move : moves) {
            BoardIO newData = new BoardIO(node.getData());
            newData.highlightMoves(move);
            newData.attemptMove(newData.getBoardLogic().getMove(move).get(0));
            GameTreeNode newChild = new GameTreeNode(newData);
            constructGameTree(newChild, depth - 1);
            node.addChild(newChild);
        }
    }
}
