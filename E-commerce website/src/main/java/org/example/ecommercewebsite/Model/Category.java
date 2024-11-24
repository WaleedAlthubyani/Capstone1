package org.example.ecommercewebsite.Model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Category {

    @NotEmpty(message = "Please enter the category's ID")
    @Pattern(regexp = "^[0-9]{10}$",message = "category's ID must be 10 digits long")
    private String id;

    @NotEmpty(message = "Please enter the category's name")
    @Size(min = 4,message = "Name can't be less than 4 characters in length")
    private String name;
}
