package service;

import model.Customer;
import java.util.*;

public class CustomerService {
    private static final CustomerService reference = new CustomerService();
    private final Map<String, Customer> customers = new HashMap<>();

    private CustomerService() {}

    public static CustomerService getInstance() {
        return reference;
    }

    public void addCustomer(String email, String firstName, String lastName) {
        if(customers.containsKey(email)){
            throw new IllegalArgumentException("This email already exists");
        }
        Customer customer = new Customer(firstName, lastName, email);
        customers.put(email, customer);
    }

    public Customer getCustomer(String customerEmail) {
        return customers.get(customerEmail);
    }

    public Collection<Customer> getAllCustomers() {
        return customers.values();
    }
}
