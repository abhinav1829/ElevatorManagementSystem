package _ElevatorManagementSystem;

import com.elevator.manage.Elevator;
import com.elevator.manage.Request;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorRequestTest {

    @Test
    void testAddRequestToQueue() {
        Elevator elevator = new Elevator();
        Request request = new Request(3, true);
        elevator.addRequest(request);
        assertTrue(elevator.getRequestQueue().contains(request));
    }

    @Test
    void testSynchronizedBlockEntry() throws InterruptedException {
        Elevator elevator = new Elevator();
        final int numThreads = 5;
        CountDownLatch latch = new CountDownLatch(numThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < numThreads; i++) {
            final int finalI = i;
            executorService.submit(() -> {
                elevator.addRequest(new Request(finalI, true));
                latch.countDown();
            });
        }

        latch.await();
        executorService.shutdown();

        assertEquals(numThreads, elevator.getRequestQueue().size());
    }

    @Test
    void testOfferRequestToQueue() {
        Elevator elevator = new Elevator();
        Request request = new Request(3, true);
        elevator.addRequest(request);
        assertTrue(elevator.getRequestQueue().contains(request));
    }

    @Test
    void testSynchronizedBlockExit() throws InterruptedException {
        Elevator elevator = new Elevator();
        final int numThreads = 5;
        CountDownLatch latch = new CountDownLatch(numThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < numThreads; i++) {
            final int finalI = i;
            executorService.submit(() -> {
                elevator.addRequest(new Request(finalI, true));
                latch.countDown();
            });
        }

        latch.await();
        executorService.shutdown();

        assertEquals(numThreads, elevator.getRequestQueue().size());
    }

    @Test
    void testAddRequestCompletion() {
        Elevator elevator = new Elevator();
        Request request = new Request(3, true);
        assertDoesNotThrow(() -> elevator.addRequest(request));
    }
}