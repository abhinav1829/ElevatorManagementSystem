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
        return Integer.compare(this.floor, other.floor);
    }

    public int getTargetFloor() {
        return this.floor;
    }
}
