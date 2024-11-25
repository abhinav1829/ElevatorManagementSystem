package com.elevator.manage;

import java.util.Scanner;

public class ElevatorManagementSystemApplication {
	public static void main(String[] args) {
        Elevator elevator = new Elevator();
        elevator.moveElevator();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter 1 for outside request, 2 for inside request, 3 to show status, 4 to exit:");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> {
                    System.out.println("Enter source floor (0-" + (Elevator.maxFloors - 1) + ") and direction (0 for Down, 1 for Up):");
                    int sourceFloor = scanner.nextInt();
                    int direction = scanner.nextInt();
                    if ((sourceFloor == 0 && direction == 0) || (sourceFloor == Elevator.maxFloors - 1 && direction == 1)) {
                        System.out.println("Invalid request for this floor.");
                        break;
                    }
                    elevator.addRequest(new Request(sourceFloor, direction == 1));
                }
                case 2 -> {
                    System.out.println("Enter destination floor (0-" + (Elevator.maxFloors - 1) + "):");
                    int destinationFloor = scanner.nextInt();
                    elevator.addRequest(new Request(destinationFloor, destinationFloor > elevator.currentFloor));
                }
                case 3 -> elevator.printStatus();
                case 4 ->{
                	scanner.close();
                	System.out.println("Bye");
                	System.exit(0);
                }
                default -> System.out.println("Invalid input. Try again.");
            }
        }
    }
}
