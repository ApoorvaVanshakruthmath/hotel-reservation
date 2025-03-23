package model;

import java.util.Objects;
import java.util.regex.Pattern;

public class Customer {
    private final String firstName;
    private final String lastName;
    private final String email;

    // Email validation pattern
    private static final String email_regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final Pattern email_pattern = Pattern.compile(email_regex);

    // Constructor with validation and try-catch block
    public Customer(String firstName, String lastName, String email) {
        try {
            this.firstName = validateName(firstName, "First name");
            this.lastName = validateName(lastName, "Last name");
            this.email = validateEmail(email);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error creating customer: " + e.getMessage());
        }
    }

    private String validateName(String name, String fieldName) {
        Objects.requireNonNull(name, fieldName + " cannot be null.");
        name = name.trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
        return name;
    }

    private String validateEmail(String email) {
        Objects.requireNonNull(email, "Email cannot be null.");
        if (!email_pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Customer: " + firstName + " " + lastName + ", Email: " + email;
    }
}
