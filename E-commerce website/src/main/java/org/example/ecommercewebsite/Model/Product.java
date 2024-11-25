package org.example.ecommercewebsite.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;

@Data
@AllArgsConstructor
public class Product {

    @NotEmpty(message = "Please enter the product ID")
    @Pattern(regexp = "^[0-9]{12}$",message = "Please provide the UPC which consist of 12 digits")//length of the universal product code
    private String id;

    @NotEmpty(message = "Please enter the name of your product")
    @Size(min = 4,message = "Name can't be less than 4 characters in length")
    private String name;

    @Positive(message = "The product's price must be a positive number")
    private double price;

    @NotEmpty(message = "Please enter the product's category's ID")
    @Pattern(regexp = "^[0-9]{10}$",message = "category's ID must be 10 digits long")
    private String categoryID;
}
