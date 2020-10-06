package com.kodilla.checkers.logic;

import com.kodilla.checkers.io.BoardGame;

import java.util.ArrayList;
import java.util.List;

public class GameTreeNode {
    private List<GameTreeNode> children = new ArrayList<>();
    private GameTreeNode parent = null;
    private BoardGame data = null;

    public GameTreeNode(BoardGame rootData) {
        data = rootData;
    }

    public BoardGame getData() {
        return data;
    }

    public List<GameTreeNode> getChildren() {
        return children;
    }

    public void addChild(GameTreeNode newChild) {
        children.add(newChild);
    }
}
