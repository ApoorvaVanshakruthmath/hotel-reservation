package service;

import model.Customer;
import model.IRoom;
import model.Reservation;

import java.util.*;

public class ReservationService {
    private static final ReservationService INSTANCE = new ReservationService();
    private final Map<String, IRoom> rooms = new HashMap<>();
    private final List<Reservation> reservations = new ArrayList<>();

    private ReservationService() {}

    public static ReservationService getInstance() {
        return INSTANCE;
    }

    public void addRoom(IRoom room) {
        if (room == null || room.getRoomNumber() == null || room.getRoomNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Room number cannot be null or empty.");
        }
        if (room.getRoomPrice() < 0) {
            throw new IllegalArgumentException("Room price cannot be negative.");
        }
        if (rooms.containsKey(room.getRoomNumber())) {
            throw new IllegalArgumentException("Room with number " + room.getRoomNumber() + " already exists.");
        }
        rooms.put(room.getRoomNumber(), room);
    }

    public IRoom getARoom(String roomId) {
        return rooms.get(roomId);
    }

    public Collection<IRoom> getAllRooms() {
        return rooms.values();
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        if (room == null) {
            throw new IllegalArgumentException("Invalid room selection.");
        }
        if (!checkOutDate.after(checkInDate)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date.");
        }

        for (Reservation existing : reservations) {
            if (existing.getRoom().equals(room) &&
                    isDateRangeOverlapping(existing.getCheckInDate(), existing.getCheckOutDate(), checkInDate, checkOutDate)) {

                System.out.println("Room is already booked for the selected dates.");

                Date[] newDates = findNextAvailableDates(room, checkInDate, checkOutDate);
                if (newDates != null) {
                    System.out.println("Suggested new dates: Check-in " + newDates[0] + ", Check-out " + newDates[1]);

                    Reservation newReservation = new Reservation(customer, room, newDates[0], newDates[1]);
                    reservations.add(newReservation);
                    System.out.println("Room successfully booked for the new dates.");
                    return newReservation;
                } else {
                    System.out.println("No available dates found.");
                    return null;
                }
            }
        }

        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        reservations.add(reservation);
        return reservation;
    }

    private Date[] findNextAvailableDates(IRoom room, Date checkInDate, Date checkOutDate) {
        Date newCheckIn = new Date(checkOutDate.getTime());
        Date newCheckOut = new Date(newCheckIn.getTime() + (checkOutDate.getTime() - checkInDate.getTime()));

        while (true) {
            boolean conflict = false;

            for (Reservation reservation : reservations) {
                if (reservation.getRoom().equals(room) &&
                        isDateRangeOverlapping(reservation.getCheckInDate(), reservation.getCheckOutDate(), newCheckIn, newCheckOut)) {

                    conflict = true;
                    newCheckIn = new Date(reservation.getCheckOutDate().getTime());
                    newCheckOut = new Date(newCheckIn.getTime() + (checkOutDate.getTime() - checkInDate.getTime()));
                    break;
                }
            }

            if (!conflict) {
                return new Date[]{newCheckIn, newCheckOut};
            }
        }
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        Collection<IRoom> availableRooms = new ArrayList<>(rooms.values());

        for (Reservation reservation : reservations) {
            if (isDateRangeOverlapping(reservation.getCheckInDate(), reservation.getCheckOutDate(), checkInDate, checkOutDate)) {
                availableRooms.remove(reservation.getRoom());
            }
        }

        return availableRooms;
    }

    private boolean isDateRangeOverlapping(Date start1, Date end1, Date start2, Date end2) {
        return start1.before(end2) && start2.before(end1);
    }

    public Collection<Reservation> getCustomersReservation(Customer customer) {
        List<Reservation> customerReservations = new ArrayList<>();
        for (Reservation reservation : reservations) {
            if (reservation.getCustomer().equals(customer)) {
                customerReservations.add(reservation);
            }
        }
        return customerReservations;
    }

    public Collection<Reservation> getAllReservations() {
        return reservations;
    }

    public void printAllReservations() {
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            reservations.forEach(System.out::println);
        }
    }
}
