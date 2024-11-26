package com.elevator.manage;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ElevatorManagementSystemApplication {
    public static void main(String[] args) {
        Elevator elevator = new Elevator();
        elevator.moveElevator();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                try {
                    System.out.println("Enter 1 for outside request, 2 for inside request, 3 to show status, 4 to exit:");

                    // Handle potential non-integer input
                    int choice;
                    try {
                        choice = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        scanner.nextLine(); // Clear the invalid input
                        System.out.println("Invalid input. Please enter a number.");
                        continue;
                    }

                    switch (choice) {
                        case 1 -> {
                            System.out.println("Enter source floor (0-" + (Elevator.MAX_FLOORS - 1) + ") and direction (0 for Down, 1 for Up):");
                            try {
                                int sourceFloor = scanner.nextInt();
                                int direction = scanner.nextInt();

                                // Validate floor range
                                if (sourceFloor < 0 || sourceFloor >= Elevator.MAX_FLOORS) {
                                    System.out.println("Invalid floor. Please enter a floor between 0 and " + (Elevator.MAX_FLOORS - 1));
                                    continue;
                                }

                                // Check for invalid requests at top or bottom floors
                                if ((sourceFloor == 0 && direction == 0) ||
                                        (sourceFloor == Elevator.MAX_FLOORS - 1 && direction == 1)) {
                                    System.out.println("Invalid request for this floor.");
                                    continue;
                                }

                                elevator.addRequest(new Request(sourceFloor, direction == 1));
                            } catch (InputMismatchException e) {
                                scanner.nextLine(); // Clear the invalid input
                                System.out.println("Invalid input. Please enter valid floor and direction.");
                            }
                        }
                        case 2 -> {
                            System.out.println("Enter destination floor (0-" + (Elevator.MAX_FLOORS - 1) + "):");
                            try {
                                int destinationFloor = scanner.nextInt();

                                // Validate floor range
                                if (destinationFloor < 0 || destinationFloor >= Elevator.MAX_FLOORS) {
                                    System.out.println("Invalid floor. Please enter a floor between 0 and " + (Elevator.MAX_FLOORS - 1));
                                    continue;
                                }

                                elevator.addRequest(new Request(destinationFloor, destinationFloor > elevator.getCurrentFloor()));

                            } catch (InputMismatchException e) {
                                scanner.nextLine(); // Clear the invalid input
                                System.out.println("Invalid input. Please enter a valid floor.");
                            }
                        }
                        case 3 -> elevator.printStatus();
                        case 4 -> {
                            System.out.println("Bye");
                            System.exit(0);
                        }
                        default -> System.out.println("Invalid input. Try again.");
                    }
                } catch (Exception e) {
                    System.out.println("An unexpected error occurred: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
}