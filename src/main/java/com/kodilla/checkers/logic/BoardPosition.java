package com.kodilla.checkers.logic;

import java.util.ArrayList;
import java.util.List;

public class BoardPosition {
    private final int x;
    private final int y;
    public List<BoardPosition> route;

    public BoardPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public BoardPosition(BoardPosition pos) {
        x = pos.x;
        y = pos.y;
        if (pos.route != null)
            route = new ArrayList<>(pos.route);
    }

    public void addToRoute(BoardPosition step) {
        if (route == null)
            route = new ArrayList<>();
        route.add(step);
    }

    public int routeLen() {
        if (route == null)
            return 0;
        else return route.size();
    }

    public List<BoardPosition> getRoute() {
        if (route == null)
            return new ArrayList<>();
        return route;
    }

    public void setRoute(List<BoardPosition> _route) {
        route = new ArrayList<>(_route);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public BoardPosition getRouteLast() {
        return route.get(route.size() - 1);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof BoardPosition))
            return false;
        BoardPosition oo = (BoardPosition)o;
        return oo.x == x && oo.y == y;
    }

    public boolean inBounds(int sideCount) {
        return x >= 0 && y >= 0 && x < sideCount && y < sideCount;
    }

    public BoardPosition add(BoardPosition pos) {
        return new BoardPosition(x + pos.x, y + pos.y);
    }

    public BoardPosition add(int xAdd, int yAdd) {
        BoardPosition retVal = new BoardPosition(x + xAdd, y + yAdd);
        if (route != null)
            retVal.route = new ArrayList<>(route);
        return retVal;
    }

    public BoardPosition avg(BoardPosition pos) {
        return new BoardPosition((x + pos.x) / 2, (y + pos.y) / 2);
    }
}
