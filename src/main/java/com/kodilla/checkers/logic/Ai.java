package com.kodilla.checkers.logic;

import com.kodilla.checkers.io.BoardGame;

public interface Ai {

    void runTurn(BoardGame boardGame);

    void constructGameTree(GameTreeNode node, int depth);
}
