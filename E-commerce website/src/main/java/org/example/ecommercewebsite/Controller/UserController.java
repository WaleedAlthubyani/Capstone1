package org.example.ecommercewebsite.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommercewebsite.ApiResponse.ApiResponse;
import org.example.ecommercewebsite.Model.Product;
import org.example.ecommercewebsite.Model.User;
import org.example.ecommercewebsite.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
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
            default: return ResponseEntity.status(200).body(new ApiResponse<>("Product bought successfully"));
        }
    }

    @GetMapping("/get-purchase-history/{user_id}")
    public ResponseEntity<ApiResponse<ArrayList<Product>>> getPurchaseList(@PathVariable String user_id){
        return ResponseEntity.status(200).body(new ApiResponse<>(userService.getPurchaseHistory(user_id)));
    }

    /*
    * #1 of the 5 endpoints
    *
    * In this endpoint the user can review a product that they purchased
    *
    * */
    @PostMapping("/add-review-to-product/{user_id}/{product_id}/{message}")
    public ResponseEntity<ApiResponse<String>> addReviewToProduct(@PathVariable String user_id,@PathVariable String product_id,@PathVariable String message){
        int result = userService.addReviewToProduct(user_id,product_id,message);

        switch (result){
            case 0: return ResponseEntity.status(404).body(new ApiResponse<>("User not found"));
            case 1: return ResponseEntity.status(400).body(new ApiResponse<>("You can't review a product you didn't buy"));
            case 2: return ResponseEntity.status(400).body(new ApiResponse<>("Your review can't be more than 500 characters in length"));
            default: return ResponseEntity.status(201).body(new ApiResponse<>("review submitted successfully"));
        }
    }

    /*
    * #2 of the 5 endpoints
    *
    * In this endpoint the user can request a refund on a product that they purchased
    *
    */
        @PostMapping("/request-refund/{user_id}/{product_id}/{message}")
    public ResponseEntity<ApiResponse<String>> requestRefund (@PathVariable String user_id,@PathVariable String product_id,@PathVariable String message){
        int result = userService.requestRefund(user_id,product_id,message);

        switch (result){
            case 0: return ResponseEntity.status(404).body(new ApiResponse<>("User not found"));
            case 1: return ResponseEntity.status(400).body(new ApiResponse<>("You can't ask for refund on something you didn't buy"));
            case 2: return ResponseEntity.status(400).body(new ApiResponse<>("The refund request message can't be more than 500 characters"));
            case 3: return ResponseEntity.status(400).body(new ApiResponse<>("Refund request message can't be empty"));
            default: return ResponseEntity.status(200).body(new ApiResponse<>("Refund request created successfully"));
        }
    }

    /*
    * bonus 2
    *
    * this end point give an admin user the list of refund requests
    */
    @GetMapping("/get-refund-list/{user_id}")
    public ResponseEntity<ApiResponse<?>> getRefundList(@PathVariable String user_id){
        HashMap<String,String> result= userService.getRefundList(user_id);

        if (result.containsKey("result0"))
            return ResponseEntity.status(404).body(new ApiResponse<>("User not found"));
        if (result.containsKey("result1"))
            return ResponseEntity.status(401).body(new ApiResponse<>("You are not authorized to do this action"));

        return ResponseEntity.status(200).body(new ApiResponse<>(result));
    }

    /*
     * #3 of the 5 endpoints
     *
     * In this endpoint an admin can decide the result of refund request
     *
     */
    @PutMapping("/decide-refund/{admin_id}/{requester_id}/{product_id}/{decision}")
    public ResponseEntity<ApiResponse<String>> decideRefund(@PathVariable String admin_id, @PathVariable String requester_id, @PathVariable String product_id, @PathVariable boolean decision){
        int result = userService.decideRefund(admin_id,requester_id,product_id,decision);

        switch (result){
            case 0: return ResponseEntity.status(404).body(new ApiResponse<>("Admin not found"));
            case 1: return ResponseEntity.status(401).body(new ApiResponse<>("You are not authorized to do this action"));
            case 2: return ResponseEntity.status(404).body(new ApiResponse<>("Requesting user not found"));
            case 3: return ResponseEntity.status(404).body(new ApiResponse<>("Product not found"));
            case 4: return ResponseEntity.status(400).body(new ApiResponse<>("There isn't a refund request matching user and product"));
            case 5: return ResponseEntity.status(400).body(new ApiResponse<>("You can't make decisions on your own request"));
            case 6: return ResponseEntity.status(200).body(new ApiResponse<>("Request denied successfully"));
            default: return ResponseEntity.status(200).body(new ApiResponse<>("Request approved successfully"));
        }
    }

    /*
     * #5 of the 5 endpoints
     *
     * In this endpoint an admin can decide the result of refund request
     *
     */
    @DeleteMapping("/ban-merchant/{user_id}/{merchant_id}/{reason}")
    public ResponseEntity<ApiResponse<String>> banMerchant(@PathVariable String user_id, @PathVariable String merchant_id,@PathVariable String reason){
        int result= userService.banMerchant(user_id,merchant_id,reason);

        switch (result){
            case 0: return ResponseEntity.status(404).body(new ApiResponse<>("User not found"));
            case 1: return ResponseEntity.status(401).body(new ApiResponse<>("You are not authorized to do this action"));
            case 2: return ResponseEntity.status(404).body(new ApiResponse<>("Merchant not found"));
            default: return ResponseEntity.status(200).body(new ApiResponse<>("Merchant banned successfully"));
        }
    }

    /*
     * This method ia for testing only and must be removed before deployment
     */
//    @PostMapping("/add-all")
//    public ResponseEntity<ApiResponse<String>> addAll(@RequestBody ArrayList<User> users){
//        for(User u:users){
//            userService.createNewUser(u);
//        }
//        return ResponseEntity.status(200).body(new ApiResponse<>("done"));
//
//    }
}
