package com.elevator.manage;

import java.util.PriorityQueue;
import java.util.function.BooleanSupplier;

public class Elevator {
	int currentFloor;
    private boolean isMoving;
    private boolean doorOpen;
    private boolean goingUp;
    private PriorityQueue<Request> requestQueue;
    private static final int MOVE_TIME = 5000; // seconds per floor
    private static final int DOOR_WAIT_TIME = 5000; // seconds wait on floor
    static final int maxFloors = 5; // Global variable for max floors

    public Elevator() {
        this.currentFloor = 0; // Start at ground floor
        this.isMoving = false;
        this.doorOpen = false;
        this.goingUp = true; // Default direction is up
        this.setRequestQueue(new PriorityQueue<>());
    }

    public synchronized void addRequest(Request request) {
        getRequestQueue().offer(request);
    }

    public void moveElevator() {
        new Thread(() -> {
            while (true) {
                try {
                    Request nextRequest = null;

                    synchronized (this) {
                        if (!getRequestQueue().isEmpty()) {
                            nextRequest = getRequestQueue().peek();
                        }
                    }

                    if (nextRequest != null && nextRequest.getFloor() != currentFloor) {
                        isMoving = true;
                        goingUp = nextRequest.getFloor() > currentFloor;
                        currentFloor += goingUp ? 1 : -1;
                        Thread.sleep(MOVE_TIME); // Simulate elevator movement
                        printStatus();
                    } else if (nextRequest != null) {
                        // Elevator reaches the target floor
                        synchronized (this) {
                            getRequestQueue().poll(); // Remove completed request
                        }
                        doorOpen = true;
                        printDoorStatus("Opening");
                        Thread.sleep(DOOR_WAIT_TIME); // Wait on floor
                        doorOpen = false;
                        printDoorStatus("Closing");
                    } else {
                        isMoving = false;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void printStatus() {
        System.out.println("Floor: " + currentFloor + ", Direction: " + (goingUp ? "Up" : "Down") + ", isMoving: " + isMoving + ", isDoorOpen: " + doorOpen);
    }

    public void printDoorStatus(String action) {
        System.out.println("Door is " + action + " on floor " + currentFloor);
    }

	public PriorityQueue<Request> getRequestQueue() {
		return requestQueue;
	}

	public void setRequestQueue(PriorityQueue<Request> requestQueue) {
		this.requestQueue = requestQueue;
	}
	
	public int getCurrentFloor() {
		return this.currentFloor;
	}

	public boolean isMoving() {
		// TODO Auto-generated method stub
		return this.isMoving;
	}
}