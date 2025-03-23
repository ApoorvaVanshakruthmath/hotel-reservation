package ui;

import api.HotelResource;
import model.IRoom;
import model.Reservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

public class MainMenu {
    private static final HotelResource hotelResource = HotelResource.getInstance();
    private static final Scanner sc = new Scanner(System.in);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {
        startMainMenu();
    }

    public static void startMainMenu() {
        String choice;
        do {
            System.out.println("\nWelcome to the Hotel Reservation System");
            System.out.println("1. Find and reserve a room");
            System.out.println("2. See my reservations");
            System.out.println("3. Create an account");
            System.out.println("4. Admin");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    findAndReserveRoom();
                    break;
                case "2":
                    viewMyReservations();
                    break;
                case "3":
                    createAccount();
                    break;
                case "4":
                    AdminMenu.startAdminMenu();
                    break;
                case "5":
                    System.out.println("Exiting the system. Thank you!");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1-5.");
            }
        } while (!choice.equals("5"));
    }

    private static void findAndReserveRoom() {
        System.out.print("Enter your email: ");
        String email = sc.nextLine().trim();

        Date checkIn = getValidDate("Enter check-in date (yyyy-MM-dd): ");
        Date checkOut = getValidDate("Enter check-out date (yyyy-MM-dd): ");

        Collection<IRoom> availableRooms = hotelResource.findARoom(checkIn, checkOut);

        if (availableRooms.isEmpty()) {
            System.out.println("No available rooms for the selected dates.");
            System.out.println("Searching for available rooms 7 days later...");

            Date newCheckIn = addDays(checkIn, 7);
            Date newCheckOut = addDays(checkOut, 7);
            availableRooms = hotelResource.findARoom(newCheckIn, newCheckOut);

            if (availableRooms.isEmpty()) {
                System.out.println("No rooms available even for the next 7 days.");
                return;
            } else {
                System.out.println("Recommended Rooms for " + newCheckIn + " to " + newCheckOut + ":");
                for (IRoom room : availableRooms) {
                    System.out.println(room);
                }
            }
        } else {
            System.out.println("Available Rooms:");
            for (IRoom room : availableRooms) {
                System.out.println(room);
            }
        }

        System.out.print("Enter room number to book: ");
        String roomNumber = sc.nextLine().trim();
        IRoom room = hotelResource.getRoom(roomNumber);

        if (room != null) {
            Reservation reservation = hotelResource.bookARoom(email, room, checkIn, checkOut);
            System.out.println("Reservation successful: " + reservation);
        } else {
            System.out.println("Invalid room number. Please try again.");
        }
    }


    private static Date getValidDate(String prompt) {
        Date date = null;
        Date today=new Date();
        while (date == null) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                date = dateFormat.parse(input);
                if(date.before(today)){
                    date=null;
                    System.out.print("Date cannot be in past");
                }
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
            }
        }
        return date;
    }


    private static Date addDays(Date date, int days) {
        if (date == null) return null;
        long timeInMs = date.getTime();
        return new Date(timeInMs + (days * 24L * 60 * 60 * 1000)); // Add days in milliseconds
    }


    private static void viewMyReservations() {
        System.out.print("Enter your email: ");
        String email = sc.nextLine().trim();
        Collection<Reservation> reservations = hotelResource.getCustomersReservations(email);

        if (reservations == null || reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            for (Reservation reservation : reservations) {
                System.out.println(reservation);
            }
        }
    }

    private static void createAccount() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your email: ");
        String email = scanner.nextLine().trim();
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            System.out.println("Invalid email format. Please try again.");
            return;
        }

        if (hotelResource.getCustomer(email) != null) {
            System.out.println("An account with this email already exists.");
            return;
        }
        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine().trim();
        if (firstName.isEmpty()) {
            System.out.println("First name cannot be empty.");
            return;
        }

        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine().trim();
        if (lastName.isEmpty()) {
            System.out.println("Last name cannot be empty.");
            return;
        }

        try {
            HotelResource.getInstance().createACustomer(email, firstName, lastName);
            System.out.println("Account created successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }



}
