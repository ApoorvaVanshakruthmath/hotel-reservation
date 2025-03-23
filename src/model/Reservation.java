package model;

import java.util.Date;

public class Reservation {
    private final Customer customer;
    private final IRoom room;
    private final Date checkInDate;
    private final Date checkOutDate;

    public Reservation(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null.");
        }
        if (checkInDate == null || checkOutDate == null || checkInDate.after(checkOutDate)) {
            throw new IllegalArgumentException("Invalid check-in or check-out date.");
        }

        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }
    public Customer getCustomer() {
        return customer;
    }

    public IRoom getRoom() {
        return room;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    @Override
    public String toString() {
        return "Reservation: " + customer + ", Room: " + room.getRoomNumber() +
                ", Check-in: " + checkInDate + ", Check-out: " + checkOutDate;
    }
}
