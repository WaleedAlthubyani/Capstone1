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

@RestController
@RequestMapping("/api/v1/merchant")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;

    @GetMapping("/get")
    public ResponseEntity getAllMerchants(){
            ArrayList<Merchant> merchants = merchantService.getAllMerchants();
            return ResponseEntity.status(200).body(merchants);
        }


        @PostMapping("/add")
        public ResponseEntity addMerchant(@RequestBody @Valid Merchant merchant, Errors errors){
            if (errors.hasErrors()){
                return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
            }
            boolean isAdded = merchantService.createNewMerchant(merchant);

            if (isAdded)
                return ResponseEntity.status(201).body(new ApiResponse("Merchant created successfully"));

            return ResponseEntity.status(400).body(new ApiResponse("Merchant's ID already exists"));
        }

        @PutMapping("/update/{merchant_id}")
        public ResponseEntity updateMerchant(@PathVariable String merchant_id,@RequestBody @Valid Merchant merchant, Errors errors){
            if (errors.hasErrors()){
                return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
            }

            int result = merchantService.updateMerchant(merchant_id,merchant);
            switch (result){
                case 0: return ResponseEntity.status(404).body(new ApiResponse("Merchant not found"));
                case 1: return ResponseEntity.status(400).body(new ApiResponse("Merchant's new ID does not match its original ID"));
                default: return ResponseEntity.status(200).body(new ApiResponse("Merchant updated successfully"));
            }
        }

        @DeleteMapping("/delete/{merchant_id}")
        public ResponseEntity deleteMerchant(@PathVariable String merchant_id){
            boolean isDeleted= merchantService.deleteMerchant(merchant_id);

            if (isDeleted)
                return ResponseEntity.status(200).body(new ApiResponse("Merchant deleted successfully"));

            return ResponseEntity.status(404).body(new ApiResponse("Merchant not found"));
        }
}
