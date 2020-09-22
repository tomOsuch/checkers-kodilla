package com.kodilla.checkers.gamelogic;

import com.kodilla.checkers.gameio.BoardIO;

import java.util.ArrayList;
import java.util.List;

public class GameTreeNode {

    private List<GameTreeNode> children = new ArrayList<>();
    private final GameTreeNode parent = null;
    private BoardIO data = null;

    public GameTreeNode(BoardIO data) {
        this.data = data;
    }

    public List<GameTreeNode> getChildren() {
        return children;
    }

    public void addChild(GameTreeNode newChild) {
        children.add(newChild);
    }

    public BoardIO getData() {
        return data;
    }
}
