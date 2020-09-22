package com.kodilla.checkers.gamelogic;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class BoardPosition {

    private final int x;
    private final int y;
    public List<BoardPosition> route;

    public BoardPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BoardPosition that = (BoardPosition) o;

        if (x != that.x) return false;
        return y == that.y;
    }

    public boolean inBounds(int sideCount) {
        return x >= 0 && y>= 0 && x < sideCount && y < sideCount;
    }

    public boolean isNextTo(BoardPosition position) {
        return abs(x - position.x) == 1 && abs(y - position.y) == 1;
    }

    public BoardPosition(BoardPosition boardPosition) {
        x = boardPosition.x;;
        y = boardPosition.y;

        if (boardPosition.route != null) {
            route = new ArrayList<>(boardPosition.route);
        }
    }

    public void addToRoute(BoardPosition step) {
        if (route == null) route = new ArrayList<>();
        route.add(step);
    }

    public int routeLength() {
        if (route == null) return 0;
        else return route.size();
    }

    public List<BoardPosition> getRoute() {
        if (route == null) return new ArrayList<>();
        else return route;
    }

    public void setRoute(List<BoardPosition> route) {
        this.route = new ArrayList<>(route);
    }

    public BoardPosition getRouteLast() {
        return route.get(route.size() - 1);
    }

    public BoardPosition add(BoardPosition position) {
        return new BoardPosition(x + position.x, y + position.y);
    }

    public BoardPosition add(int x, int y) {
        BoardPosition position = new BoardPosition(this.x + x, this.y + y);
        if (route != null) position.route = new ArrayList<>(route);
        return position;
    }

    public BoardPosition avg(BoardPosition position) {
        return new BoardPosition((x + position.x) / 2, (y + position.y) / 2);
    }
}
