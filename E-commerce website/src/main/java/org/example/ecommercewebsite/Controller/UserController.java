package org.example.ecommercewebsite.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommercewebsite.ApiResponse.ApiResponse;
import org.example.ecommercewebsite.Model.User;
import org.example.ecommercewebsite.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<ArrayList<User>>> getAllUsers() {
        ArrayList<User> users = userService.getAllUsers();
        return ResponseEntity.status(200).body(new ApiResponse<>(users));
    }


    @PostMapping("/add")
    public ResponseEntity<ApiResponse<String>> addUser(@RequestBody @Valid User user, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse<>(Objects.requireNonNull(errors.getFieldError()).getDefaultMessage()));
        }
        boolean isAdded = userService.createNewUser(user);

        if (isAdded)
            return ResponseEntity.status(201).body(new ApiResponse<>("User created successfully"));

        return ResponseEntity.status(400).body(new ApiResponse<>("User's ID already exists"));
    }

    @PutMapping("/update/{user_id}")
    public ResponseEntity<ApiResponse<String>> updateUser(@PathVariable String user_id, @RequestBody @Valid User user, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse<>(Objects.requireNonNull(errors.getFieldError()).getDefaultMessage()));
        }

        int result = userService.updateUser(user_id, user);
        switch (result) {
            case 0:
                return ResponseEntity.status(404).body(new ApiResponse<>("User not found"));
            case 1:
                return ResponseEntity.status(400).body(new ApiResponse<>("User's new ID does not match its original ID"));
            default:
                return ResponseEntity.status(200).body(new ApiResponse<>("User updated successfully"));
        }
    }

    @DeleteMapping("/delete/{user_id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable String user_id) {
        boolean isDeleted = userService.deleteUser(user_id);

        if (isDeleted)
            return ResponseEntity.status(200).body(new ApiResponse<>("User deleted successfully"));

        return ResponseEntity.status(404).body(new ApiResponse<>("User not found"));
    }

    @PostMapping("/buy-product/{user_id}/{product_id}/{merchant_id}")
    public ResponseEntity<ApiResponse<String>> buyProduct(@PathVariable String user_id,@PathVariable String product_id,@PathVariable String merchant_id){
        int result = userService.buyProduct(user_id,product_id,merchant_id);

        switch (result){
            case 0: return ResponseEntity.status(404).body(new ApiResponse<>("User not found"));
            case 1: return ResponseEntity.status(404).body(new ApiResponse<>("Product not found"));
            case 2: return ResponseEntity.status(404).body(new ApiResponse<>("Merchant not found"));
            case 3: return ResponseEntity.status(400).body(new ApiResponse<>("Merchant doesn't sell this product"));
            case 4: return ResponseEntity.status(400).body(new ApiResponse<>("Product is sold out"));
            case 5: return ResponseEntity.status(404).body(new ApiResponse<>("User doesn't have enough money to buy this product"));
            default: return ResponseEntity.status(404).body(new ApiResponse<>("Product bought successfully"));
        }
    }
}
