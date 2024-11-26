package _ElevatorManagementSystem;

import com.elevator.manage.Elevator;
import com.elevator.manage.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorTest {
    private Elevator elevator;

    @BeforeEach
    void setup() {
        elevator = new Elevator();
    }

    @Test
    void testEmptyQueuePath() throws InterruptedException {
        elevator.moveElevator();
        TimeUnit.SECONDS.sleep(2);

        assertFalse(elevator.isMoving());
        assertTrue(elevator.getRequestQueue().isEmpty());
    }

    @Test
    void testSingleRequestProcessingPath() throws InterruptedException {
        elevator.addRequest(new Request(2, true));
        elevator.moveElevator();

        TimeUnit.SECONDS.sleep(15);

        assertEquals(2, elevator.getCurrentFloor());
        assertFalse(elevator.isMoving());
        assertTrue(elevator.getRequestQueue().isEmpty());
    }

    @Test
    void testConcurrentRequestProcessing() throws InterruptedException {
        elevator.addRequest(new Request(2, true));
        elevator.addRequest(new Request(4, true));
        elevator.moveElevator();

        TimeUnit.SECONDS.sleep(5);
        elevator.addRequest(new Request(3, true));

        TimeUnit.SECONDS.sleep(30);

        assertTrue(elevator.getRequestQueue().isEmpty());
        assertFalse(elevator.isMoving());
    }

    @Test
    void testMovingStateTransitions() throws InterruptedException {
        elevator.addRequest(new Request(3, true));
        elevator.moveElevator();

        TimeUnit.SECONDS.sleep(7);
        assertTrue(elevator.isMoving());

        TimeUnit.SECONDS.sleep(20);
        assertFalse(elevator.isMoving());
        assertEquals(3, elevator.getCurrentFloor());
    }

    @Test
    void testRequestQueueOrder() throws InterruptedException {
        elevator.addRequest(new Request(4, true));
        elevator.addRequest(new Request(2, true));
        elevator.addRequest(new Request(3, true));
        elevator.moveElevator();

        TimeUnit.SECONDS.sleep(25);
        elevator.addRequest(new Request(1, false));
        TimeUnit.SECONDS.sleep(35);

        assertFalse(elevator.isMoving());
        assertTrue(elevator.getRequestQueue().isEmpty());
    }

    @Test
    void testInterruptedProcessing() throws InterruptedException {
        // Start with a single request
        Request request = new Request(5, true);
        elevator.addRequest(request);

        // Start the elevator
        elevator.moveElevator();

        // Wait for elevator to start processing
        TimeUnit.SECONDS.sleep(2);

        // Verify the initial state
        assertFalse(elevator.getRequestQueue().isEmpty());

        // Wait for the request to complete
        TimeUnit.SECONDS.sleep(15);

        // Verify final state
        assertTrue(elevator.getRequestQueue().isEmpty(), "Request queue should be empty after completion");
        assertEquals(5, elevator.getCurrentFloor(), "Elevator should be at the target floor");
    }
}