package org.example.ecommercewebsite.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommercewebsite.ApiResponse.ApiResponse;
import org.example.ecommercewebsite.Model.Merchant;
import org.example.ecommercewebsite.Service.MerchantService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/merchant")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<ArrayList<Merchant>>> getAllMerchants(){
            ArrayList<Merchant> merchants = merchantService.getAllMerchants();
            return ResponseEntity.status(200).body(new ApiResponse<>(merchants));
        }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<String>> addMerchant(@RequestBody @Valid Merchant merchant, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse<>(Objects.requireNonNull(errors.getFieldError()).getDefaultMessage()));
        }
        int result = merchantService.createNewMerchant(merchant);

        switch (result){
            case 0: return ResponseEntity.status(400).body(new ApiResponse<>("Merchant's ID already exists"));
            case 1: return ResponseEntity.status(400).body(new ApiResponse<>("Merchant is on the banned list"));
            default: return ResponseEntity.status(201).body(new ApiResponse<>("Merchant created successfully"));
        }

    }

    @PutMapping("/update/{merchant_id}")
    public ResponseEntity<ApiResponse<String>> updateMerchant(@PathVariable String merchant_id, @RequestBody @Valid Merchant merchant, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse<>(Objects.requireNonNull(errors.getFieldError()).getDefaultMessage()));
        }

        int result = merchantService.updateMerchant(merchant_id, merchant);
        switch (result) {
            case 0:
                return ResponseEntity.status(404).body(new ApiResponse<>("Merchant not found"));
            case 1:
                return ResponseEntity.status(400).body(new ApiResponse<>("Merchant's new ID does not match its original ID"));
            default:
                return ResponseEntity.status(200).body(new ApiResponse<>("Merchant updated successfully"));
        }
    }

    @DeleteMapping("/delete/{merchant_id}")
    public ResponseEntity<ApiResponse<String>> deleteMerchant(@PathVariable String merchant_id) {
        boolean isDeleted = merchantService.deleteMerchant(merchant_id);

        if (isDeleted)
            return ResponseEntity.status(200).body(new ApiResponse<>("Merchant deleted successfully"));

        return ResponseEntity.status(404).body(new ApiResponse<>("Merchant not found"));
    }

    @PutMapping("/add-stocks/{product_id}/{merchant_id}/{stocks_amount}")
    public ResponseEntity<ApiResponse<String>> addStocks(@PathVariable String product_id, @PathVariable String merchant_id,@PathVariable int stocks_amount) {
        int result = merchantService.addStocks(product_id,merchant_id,stocks_amount);
        switch (result){
            case 0: return ResponseEntity.status(404).body(new ApiResponse<>("Product not found"));
            case 1: return ResponseEntity.status(404).body(new ApiResponse<>("Merchant not found"));
            case 3: return ResponseEntity.status(400).body(new ApiResponse<>("Merchant doesn't sell this product"));
            default: return ResponseEntity.status(200).body(new ApiResponse<>("Stocks added successfully"));
        }
    }

    /*
     * This method ia for testing only and must be removed before deployment
     */
//    @PostMapping("/add-all")
//    public ResponseEntity<ApiResponse<String>> addAll(@RequestBody ArrayList<Merchant> merchants ){
//        for(Merchant m: merchants){
//            merchantService.createNewMerchant(m);
//        }
//        return ResponseEntity.status(200).body(new ApiResponse<>("done"));
//    }
}
