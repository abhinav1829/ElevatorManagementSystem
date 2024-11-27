package com.elevator.manage;

import java.util.PriorityQueue;

public class Elevator {
	int currentFloor;
	private boolean isMoving;
	private boolean doorOpen;
	private boolean goingUp;
	private PriorityQueue<Request> requestQueue;
	private static final int MOVE_TIME = 5000;
	private static final int DOOR_WAIT_TIME = 5000; 
	static final int maxFloors = 5; 

	public Elevator() {
		this.currentFloor = 0; 
		this.isMoving = false;
		this.doorOpen = false;
		this.goingUp = true; 
		this.requestQueue = new PriorityQueue<>(new RequestComparator(currentFloor, goingUp));
	}

	public synchronized void addRequest(Request request) {
		getRequestQueue().offer(request);
		rebuildQueue();
	}

	private synchronized void rebuildQueue() {
		PriorityQueue<Request> newQueue = new PriorityQueue<>(new RequestComparator(currentFloor, goingUp));
		newQueue.addAll(this.requestQueue);
		this.requestQueue = newQueue;
	}

	public void moveElevator() {
		new Thread(() -> {
			while (true) {
				try {
					Request nextRequest = null;

					synchronized (this) {
						if (!getRequestQueue().isEmpty()) {
							nextRequest = getRequestQueue().peek();
						} else {
							isMoving = false;
							continue;
						}
					}

					if (nextRequest != null && nextRequest.getFloor() != currentFloor) {
						isMoving = true;
						goingUp = nextRequest.getFloor() > currentFloor;
						currentFloor += goingUp ? 1 : -1;
						Thread.sleep(MOVE_TIME); 
						printStatus();
						rebuildQueue();
					} else if (nextRequest != null) {
						synchronized (this) {
							getRequestQueue().poll(); 
						}
						doorOpen = true;
						printDoorStatus("Opening");
						Thread.sleep(DOOR_WAIT_TIME);
						doorOpen = false;
						printDoorStatus("Closing");
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public PriorityQueue<Request> getRequestQueue() {
		return this.requestQueue;
	}

	public void printStatus() {
		System.out.println("Floor: " + currentFloor + ", Direction: " + (goingUp ? "Up" : "Down") + ", isMoving: "
				+ isMoving + ", isDoorOpen: " + doorOpen);
	}

	public void printDoorStatus(String action) {
		System.out.println("Door is " + action + " on floor " + currentFloor);
	}

	public int getCurrentFloor() {
		return this.currentFloor;
	}

	public boolean isMoving() {
		// TODO Auto-generated method stub
		return this.isMoving;
	}
}