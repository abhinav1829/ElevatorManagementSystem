/*
 * This source file was generated by the Gradle 'init' task
 */
package com.elevator.test;

import com.elevator.manage.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ElevatorTest {
	private Elevator elevator;

	@BeforeEach
	void setup() {
		elevator = new Elevator();
	}

	//MOVE ELEVATOR METHODS
	
	@Test
	void testMoveElevator_Path1_nextRequest() throws InterruptedException {
		// Path: [1,2,3,4,6,7,8,16]
		elevator.addRequest(new Request(2, true)); // Request for floor 2, going up

		waitForElevatorToReachFloor(elevator,2); // Allow the elevator to process the request
		assertEquals(2, elevator.getCurrentFloor());
		assertFalse(elevator.getRequestQueue().isEmpty()); // Request still there
	}

	@Test
	void testMoveElevator_Path2_doorOpen() throws InterruptedException {
		// Path: [1,2,3,4,6,10,12,13,14,16]
		elevator.addRequest(new Request(0, true)); // Request for the current floor (0)

		waitForElevatorToReachFloor(elevator,0); // Allow the elevator to process the request
		assertEquals(0, elevator.getCurrentFloor());
//		TimeUnit.MILLISECONDS.sleep(11000);
//		assertTrue(elevator.getRequestQueue().isEmpty()); // Request is completed
	}

	@Test
	void testMoveElevator_Path3_currentFloorLoop() throws InterruptedException {
		// Path: [1,2,3,4,6,7,8,9,2,3,4,6,7,8,16]
		elevator.addRequest(new Request(3, true)); // Request for floor 3
		elevator.addRequest(new Request(1, true)); // Request for floor 1

		waitForElevatorToReachFloor(elevator,3); // Allow the elevator to process multiple requests
		assertEquals(3, elevator.getCurrentFloor()); // Ends at the higher floor
	}

	@Test
	void testMoveElevator_Path4_isMovingIdleState() throws InterruptedException {
		// Path: [1,2,3,4,6,10,11,2,3,4,6,7,8,16]
		elevator.addRequest(new Request(0, true)); // Current floor is 0, no movement required
		waitForElevatorToReachFloor(elevator,0); // Allow the elevator to process idle state
		assertEquals(0, elevator.getCurrentFloor());
		assertFalse(elevator.isMoving()); // Elevator should be idle after handling
	}

	@Test
	void testMoveElevator_MultiplePathsWithRequestQueue() throws InterruptedException {
		// Covers paths involving requestQueue
		// Path: [1,2,3,4,5,6,7,8,16]
		elevator.addRequest(new Request(4, true)); // Request for floor 4
		elevator.addRequest(new Request(2, true)); // Request for floor 2

		waitForElevatorToReachFloor(elevator,4); // Allow the elevator to process
		assertEquals(4, elevator.getCurrentFloor()); // Ends at the highest request
		assertTrue(elevator.getRequestQueue().isEmpty());
	}

	@Test
	void testMoveElevator_GoingDown() throws InterruptedException {
		// Covers goingDown paths
		elevator.addRequest(new Request(2, true)); // First move up to floor 2
		elevator.addRequest(new Request(1, false)); // Then move down to floor 1

		waitForElevatorToReachFloor(elevator,1);
		System.out.println("TEST CURRENT FLOOR _ "+elevator.toString());
		assertEquals(1, elevator.getCurrentFloor());
	}

	@Test
	void testMoveElevator_DoorOpenAndCloseMultipleTimes() throws InterruptedException {
		// Covers doorOpen path multiple times
		elevator.addRequest(new Request(1, true)); // Request floor 1
		elevator.addRequest(new Request(3, true)); // Then floor 3
		elevator.addRequest(new Request(0, false)); // Then back to 0

		waitForElevatorToReachFloor(elevator,0);
		assertEquals(0, elevator.getCurrentFloor());
		assertFalse(elevator.isMoving());
		assertTrue(elevator.getRequestQueue().isEmpty());
	}

	//MENU Methods
	
	@Test
	void testElevatorDuPath1() throws InterruptedException {
		// Covers path: [1, 2, 3, 4, 5, 6, 7, 17, 5, 6]
		elevator.moveElevator(); // Start the elevator's movement thread

		elevator.addRequest(new Request(2, true)); // Request to go to floor 2
		waitForElevatorToReachFloor(elevator, 2);

		assertEquals(2, elevator.getCurrentFloor());
	}

	@Test
	void testElevatorDuPath2() throws InterruptedException {
		// Covers path: [1, 2, 3, 4, 5, 6, 7, 14, 15, 16, 5, 6]
		elevator.moveElevator(); // Start the elevator's movement thread

		elevator.addRequest(new Request(4, true)); // Request to go to floor 4
		waitForElevatorToReachFloor(elevator, 4);

		assertEquals(4, elevator.getCurrentFloor());
	}

	@Test
	void testElevatorDuPath3() throws InterruptedException {
		// Covers path: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 5, 6]
		elevator.moveElevator(); // Start the elevator's movement thread

		elevator.addRequest(new Request(3, true)); // Request to go to floor 3
		elevator.addRequest(new Request(4, true)); // Request to go to floor 4
		waitForElevatorToReachFloor(elevator, 4);

		assertEquals(4, elevator.getCurrentFloor());
	}

	@Test
	void testScannerDuPath() throws InterruptedException {
		// Covers path: [1, 2, 3, 4, 5, 6, 7, 19, 20]
		elevator.moveElevator(); // Start the elevator's movement thread

		elevator.addRequest(new Request(1, true)); // Request to go to floor 1
		waitForElevatorToReachFloor(elevator, 1);

		assertEquals(1, elevator.getCurrentFloor());
	}

	@Test
	void testSourceFloorDuPath1() throws InterruptedException {
		// Covers path: [1, 2, 3, 4, 5, 6, 7, 8, 9, 5, 6]
		elevator.moveElevator(); // Start the elevator's movement thread

		elevator.addRequest(new Request(2, true)); // Request to go to floor 2
		waitForElevatorToReachFloor(elevator, 2);

		assertEquals(2, elevator.getCurrentFloor());
	}

	@Test
	void testDirectionDuPath3() throws InterruptedException {
		// Covers path: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 5, 6]
		elevator.moveElevator(); // Start the elevator's movement thread

		elevator.addRequest(new Request(2, true)); // Request to go to floor 2
		elevator.addRequest(new Request(4, true)); // Request to go to floor 4
		waitForElevatorToReachFloor(elevator, 4);

		assertEquals(4, elevator.getCurrentFloor());
	}

	@Test
	void testDestinationFloorDuPath2() throws InterruptedException {
		// Covers path: [1, 2, 3, 4, 5, 6, 7, 14, 15, 16, 5, 6]
		elevator.moveElevator(); // Start the elevator's movement thread

		elevator.addRequest(new Request(2, true)); // Request to go to floor 2
		elevator.addRequest(new Request(4, true)); // Request to go to floor 4
		waitForElevatorToReachFloor(elevator, 4);

		assertEquals(4, elevator.getCurrentFloor());
	}

	/**
	 * Helper method to wait for the elevator to reach a specific floor.
	 */
	private void waitForElevatorToReachFloor(Elevator elevator, int targetFloor) throws InterruptedException {
		int maxWaitTime = 50; // Max wait time in seconds
		while (maxWaitTime > 0 && elevator.getCurrentFloor() != targetFloor) {
			TimeUnit.MILLISECONDS.sleep(1000); // Check the elevator's floor every 1000ms
			maxWaitTime--;
		}

		if (elevator.getCurrentFloor() != targetFloor) {
			throw new AssertionError("Elevator did not reach the target floor within the expected time.");
		}
	}
	
	
	// Add Request tests
	
	@Test
    void testAddRequest_SingleRequest() {
        // Test path: [1,2,3,4,5]
        Request request = new Request(3, true); // Request for floor 3, going up
        elevator.addRequest(request);

        // Verify that the request is added to the queue
        PriorityQueue<Request> queue = elevator.getRequestQueue();
        assertFalse(queue.isEmpty());
        assertEquals(request, queue.peek());
    }

    @Test
    void testAddRequest_MultipleRequests() {
        // Test path: [1,2,3,4,5] with multiple requests
        Request request1 = new Request(2, true); // Request for floor 2, going up
        Request request2 = new Request(4, true); // Request for floor 4, going up
        elevator.addRequest(request1);
        elevator.addRequest(request2);

        // Verify that both requests are added in the correct priority order
        PriorityQueue<Request> queue = elevator.getRequestQueue();
        assertEquals(2, queue.size());
        assertEquals(request1, queue.poll());
        assertEquals(request2, queue.poll());
    }

    @Test
    void testAddRequest_EmptyQueue() {
        // Test path: [1,2,3,4,5] with an empty queue
        Request request = new Request(1, true); // Request for floor 1, going up
        elevator.addRequest(request);

        // Verify that the queue contains only this request
        PriorityQueue<Request> queue = elevator.getRequestQueue();
        assertEquals(1, queue.size());
        assertEquals(request, queue.peek());
    }

    @Test
    void testAddRequest_AlreadyExistingRequest() {
        // Test path: [1,2,3,4,5] with a duplicate request
        Request request = new Request(2, true); // Request for floor 2, going up
        elevator.addRequest(request);
        elevator.addRequest(request); // Add the same request again

        // Verify that the queue handles duplicate requests (priority queue can store duplicates)
        PriorityQueue<Request> queue = elevator.getRequestQueue();
        assertEquals(2, queue.size());
        assertEquals(request, queue.poll());
        assertEquals(request, queue.poll());
    }
}