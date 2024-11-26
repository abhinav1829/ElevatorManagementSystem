package com.elevator.manage;

import java.util.PriorityQueue;
import java.util.concurrent.locks.ReentrantLock;

public class Elevator {
    private volatile int currentFloor;
    private volatile boolean isMoving;
    private volatile boolean doorOpen;
    private volatile boolean goingUp;
    private final PriorityQueue<Request> requestQueue;
    private static final int MOVE_TIME = 5; // seconds per floor
    private static final int DOOR_WAIT_TIME = 5; // seconds wait on floor
    static final int MAX_FLOORS = 5;
    private final ReentrantLock lock = new ReentrantLock();
    private volatile boolean isProcessingRequest = false;

    public Elevator() {
        this.currentFloor = 0;
        this.isMoving = false;
        this.doorOpen = false;
        this.goingUp = true;
        this.requestQueue = new PriorityQueue<>();
    }

    public void addRequest(Request request) {
        lock.lock();
        try {
            requestQueue.offer(request);
        } finally {
            lock.unlock();
        }
    }

    public void moveElevator() {
        new Thread(() -> {
            while (true) {
                Request nextRequest = null;
                lock.lock();
                try {
                    if (!requestQueue.isEmpty() && !isProcessingRequest) {
                        nextRequest = requestQueue.poll();
                        isProcessingRequest = true;
                    }
                } finally {
                    lock.unlock();
                }

                if (nextRequest != null) {
                    try {
                        processRequest(nextRequest);
                    } finally {
                        isProcessingRequest = false;
                    }
                } else {
                    isMoving = false;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }).start();
    }

    private void processRequest(Request request) {
        try {
            if (request.getFloor() != currentFloor) {
                moveToFloor(request.getFloor());
            }
            handleDoorOperation();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void moveToFloor(int targetFloor) throws InterruptedException {
        goingUp = targetFloor > currentFloor;
        isMoving = true;
        doorOpen = false;  // Ensure door is closed before moving

        while (currentFloor != targetFloor) {
            Thread.sleep(MOVE_TIME * 1000);
            currentFloor += goingUp ? 1 : -1;
            printStatus();
        }
        isMoving = false;  // Stop moving when target floor is reached
    }

    private void handleDoorOperation() throws InterruptedException {
        isMoving = false;  // Ensure elevator is marked as not moving
        doorOpen = true;
        printDoorStatus("Opening");
        Thread.sleep(DOOR_WAIT_TIME * 1000);
        doorOpen = false;
        printDoorStatus("Closing");
        // Add small delay after closing to ensure state is updated
        Thread.sleep(100);
    }

    public void printStatus() {
        System.out.println("Floor: " + currentFloor +
                ", Direction: " + (goingUp ? "Up" : "Down") +
                ", isMoving: " + isMoving +
                ", isDoorOpen: " + doorOpen);
    }

    public void printDoorStatus(String action) {
        System.out.println("Door is " + action + " on floor " + currentFloor);
    }

    public PriorityQueue<Request> getRequestQueue() {
        return requestQueue;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public boolean isDoorOpen() {
        return doorOpen;
    }

    public boolean isMoving() {
        return isMoving;
    }
}