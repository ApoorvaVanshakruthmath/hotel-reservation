package api;

import model.Customer;
import model.IRoom;
import model.Reservation;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.Date;


public class HotelResource {

    private HotelResource() {}
    private static final HotelResource INSTANCE = new HotelResource();

    public static HotelResource getInstance() {
        return INSTANCE;
    }

    private final CustomerService customerService = CustomerService.getInstance();
    private final ReservationService reservationService = ReservationService.getInstance();

    public Customer getCustomer(String email) {
        return customerService.getCustomer(email);
    }

    public void createACustomer(String email, String firstName, String lastName) {
        customerService.addCustomer(email, firstName, lastName);
    }

    public IRoom getRoom(String roomNumber) {
        return reservationService.getARoom(roomNumber);
    }

    public Collection<IRoom> findARoom(Date checkIn, Date checkOut) {
        return reservationService.findRooms(checkIn, checkOut);
    }


    public Reservation bookARoom(String customerEmail, IRoom room, Date checkIn, Date checkOut) {
        Customer customer = getCustomer(customerEmail);
        if (customer == null) {
            System.out.println("Customer not found. Please create an account first.");
            return null;
        }
        return reservationService.reserveARoom(customer, room, checkIn, checkOut);
    }

    public Collection<Reservation> getCustomersReservations(String customerEmail) {
        Customer customer = getCustomer(customerEmail);
        return customer == null ? null : reservationService.getCustomersReservation(customer);
    }
}
