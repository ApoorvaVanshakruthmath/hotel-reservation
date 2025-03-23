package model;

public class Driver {
    public static void main(String[] args) {
        try {
            Customer customerInvalid = new Customer("First", "Second", "email");
            System.out.println(customerInvalid);
        } catch (IllegalArgumentException e) {
            System.out.println("Exception caught: " + e.getMessage());
        }
    }
}
