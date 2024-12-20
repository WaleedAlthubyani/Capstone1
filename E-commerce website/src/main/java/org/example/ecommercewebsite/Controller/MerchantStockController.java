package org.example.ecommercewebsite.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommercewebsite.ApiResponse.ApiResponse;
import org.example.ecommercewebsite.Model.MerchantStock;
import org.example.ecommercewebsite.Service.MerchantService;
import org.example.ecommercewebsite.Service.MerchantStockService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/merchant-stock")
@RequiredArgsConstructor
public class MerchantStockController {

    private final MerchantStockService merchantStockService;
    private final MerchantService merchantService;

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<ArrayList<MerchantStock>>> getAllMerchantStocks(){
                ArrayList<MerchantStock> merchantStocks = merchantStockService.getAllMerchantStocks();
                return ResponseEntity.status(200).body(new ApiResponse<>(merchantStocks));
            }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<String>> addMerchantStock(@RequestBody @Valid MerchantStock merchantStock, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse<>(Objects.requireNonNull(errors.getFieldError()).getDefaultMessage()));
        }
        int result = merchantStockService.addNewMerchantStocks(merchantStock,merchantService.getAllMerchants());

        switch(result){
            case 0: return ResponseEntity.status(400).body(new ApiResponse<>("Merchant stock's ID already exists"));
            case 1: return ResponseEntity.status(404).body(new ApiResponse<>("Product not found"));
            case 2: return ResponseEntity.status(404).body(new ApiResponse<>("Merchant not found"));
            default:return ResponseEntity.status(201).body(new ApiResponse<>("Merchant stock created successfully"));
        }
    }

    @PutMapping("/update/{merchant_stock_id}")
    public ResponseEntity<ApiResponse<String>> updateMerchantStock(@PathVariable String merchant_stock_id, @RequestBody @Valid MerchantStock merchantStock, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse<>(Objects.requireNonNull(errors.getFieldError()).getDefaultMessage()));
        }

        int result = merchantStockService.updateMerchantStock(merchant_stock_id, merchantStock);
        switch (result) {
            case 0:
                return ResponseEntity.status(404).body(new ApiResponse<>("Merchant stock not found"));
            case 1:
                return ResponseEntity.status(400).body(new ApiResponse<>("Merchant stock's new ID does not match its original ID"));
            default:
                return ResponseEntity.status(200).body(new ApiResponse<>("Merchant stock updated successfully"));
        }
    }

    @DeleteMapping("/delete/{merchant_stock_id}")
    public ResponseEntity<ApiResponse<String>> deleteMerchant(@PathVariable String merchant_stock_id) {
        boolean isDeleted = merchantStockService.deleteMerchantStock(merchant_stock_id);

        if (isDeleted)
            return ResponseEntity.status(200).body(new ApiResponse<>("Merchant stock deleted successfully"));

        return ResponseEntity.status(404).body(new ApiResponse<>("Merchant stock not found"));
    }

    /*
     * This method ia for testing only and must be removed before deployment
     */
//    @PostMapping("/add-all")
//    public ResponseEntity<ApiResponse<String>> addAll(@RequestBody ArrayList<MerchantStock> merchantStocks){
//        for(MerchantStock ms:merchantStocks){
//            merchantStockService.addNewMerchantStocks(ms,merchantService.getAllMerchants());
//        }
//        return ResponseEntity.status(200).body(new ApiResponse<>("done"));
//
//    }
}
