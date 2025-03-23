package ui;

import api.AdminResource;
import model.Customer;
import model.IRoom;
import java.util.*;
import java.util.Collection;
import java.util.Scanner;

public class AdminMenu {
    private static final AdminResource adminResource = AdminResource.getInstance();

    private static final Scanner sc = new Scanner(System.in);

    public static void startAdminMenu() {
        String choice;
        do {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. See all Customers");
            System.out.println("2. See all Rooms");
            System.out.println("3. See all Reservations");
            System.out.println("4. Add a Room");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");
            choice = sc.nextLine();

            switch (choice) {
                case "1":
                    seeAllCustomers();
                    break;
                case "2":
                    seeAllRooms();
                    break;
                case "3":
                    seeAllReservations();
                    break;
                case "4":
                    addRoom();
                    break;
                case "5":
                    System.out.println("Returning to Main Menu...");
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1-5.");
            }
        } while (true);
    }

    private static void seeAllCustomers() {
        Collection<Customer> customers = adminResource.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            for (Customer customer : customers) {
                System.out.println(customer);
            }
        }
    }

    private static void seeAllRooms() {
        Collection<IRoom> rooms = adminResource.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("No rooms available.");
        } else {
            for (IRoom room : rooms) {
                System.out.println(room);
            }
        }
    }

    private static void seeAllReservations() {
        adminResource.displayAllReservations();
    }

    private static void addRoom() {
        System.out.print("Enter room number: ");
        String roomNumber = sc.nextLine();
        System.out.print("Enter room price: ");
        double price = Double.parseDouble(sc.nextLine());
        System.out.print("Enter room type (SINGLE/DOUBLE): ");
        String roomType = sc.nextLine().toUpperCase();

        if (!roomType.equals("SINGLE") && !roomType.equals("DOUBLE")) {
            System.out.println("Invalid room type! Use SINGLE or DOUBLE.");
            return;
        }

        IRoom room = new model.Room(roomNumber, price, model.RoomType.valueOf(roomType));

        adminResource.addRooms(Collections.singletonList(room));

        System.out.println("Room added successfully!");
    }

}
