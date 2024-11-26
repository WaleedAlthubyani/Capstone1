package org.example.ecommercewebsite.Model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class MerchantStock {

    @NotEmpty(message = "Please enter the merchant's stock's ID")
    @Pattern(regexp = "^[0-9]{10}$",message = "stock's ID must be 10 digits long")
    private String id;

    @NotEmpty(message = "Please enter the product ID")
    @Pattern(regexp = "^[0-9]{12}$",message = "Please provide the UPC which consist of 12 digits")
    private String product_id;

    @NotEmpty(message = "Please provide the merchant's ID")
    @Pattern(regexp = "^[0-9]{10}$",message = "Merchant's ID must be 10 digits long")
    private String merchant_id;

    @Positive(message = "stock can only be a positive number")
    @Min(value = 11,message = "Starting stocks must be more than 10")
    private int stock;
}
