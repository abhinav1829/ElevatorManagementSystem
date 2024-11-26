package com.elevator.manage;

public class Request implements Comparable<Request> {
    private final int floor;
    private final boolean upDirection;

    public Request(int floor, boolean upDirection) {
        if (floor < 0 || floor > Elevator.MAX_FLOORS) {
            throw new IllegalArgumentException("Invalid floor number");
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return floor == request.floor && upDirection == request.upDirection;
    }

    @Override
    public int hashCode() {
        return 31 * floor + (upDirection ? 1 : 0);
    }
}