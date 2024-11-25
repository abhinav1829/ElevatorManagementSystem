package com.elevator.manage;

public class Request implements Comparable<Request> {
    private int floor;
    private boolean upDirection;

    public Request(int floor, boolean upDirection) {
        this.floor = floor;
        this.upDirection = upDirection;
    }

    public int getFloor() {
        return floor;
    }

    public boolean isUpDirection() {
        return upDirection;
    }

    @Override
    public int compareTo(Request other) {
        if (this.floor == other.floor) return 0;
        return (this.floor < other.floor) ? -1 : 1;
    }
}