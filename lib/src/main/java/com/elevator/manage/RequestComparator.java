package com.elevator.manage;

import java.util.Comparator;

public class RequestComparator implements Comparator<Request> {
    private int currentFloor;
    private boolean goingUp;

    public RequestComparator(int currentFloor, boolean goingUp) {
        this.currentFloor = currentFloor;
        this.goingUp = goingUp;
    }

    @Override
    public int compare(Request r1, Request r2) {
        boolean r1SameDirection = (r1.getFloor() > currentFloor) == goingUp;
        boolean r2SameDirection = (r2.getFloor() > currentFloor) == goingUp;

        if (r1SameDirection && !r2SameDirection) {
            return -1; 
        } else if (!r1SameDirection && r2SameDirection) {
            return 1; 
        } else {
            return Integer.compare(Math.abs(r1.getFloor() - currentFloor), Math.abs(r2.getFloor() - currentFloor));
        }
    }
}
