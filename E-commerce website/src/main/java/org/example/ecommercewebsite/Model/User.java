package org.example.ecommercewebsite.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {

    @NotEmpty(message = "Please enter the user's ID")
    @Pattern(regexp = "^[0-9]{10}$",message = "User's ID must be 10 digits long")
    private String id;

    @NotEmpty(message = "Please enter the user's username")
    @Size(min = 6,message = "Username must be more than 5 characters in length")
    private String username;

    @NotEmpty(message = "Please enter the user's password")
    @Size(min = 7)
    @Pattern(regexp = "^[a-zA-Z0-9]{6,}$")
    private String password;

    @NotEmpty(message = "Please enter the user's email")
    @Email(message = "Please enter a valid email address")
    private String email;

    @NotEmpty(message = "Please enter the user's role")
    @Pattern(regexp = "^(Admin|Customer)$",message = "User can only be Admin or Customer")
    private String role;

    @Positive(message = "balance must be a positive number")
    private double balance;
}
