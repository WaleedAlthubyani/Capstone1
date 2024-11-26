package org.example.ecommercewebsite.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommercewebsite.ApiResponse.ApiResponse;
import org.example.ecommercewebsite.Model.Product;
import org.example.ecommercewebsite.Service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<ArrayList<Product>>> getAllProducts() {
        ArrayList<Product> products = productService.getAllProducts();
        return ResponseEntity.status(200).body(new ApiResponse<>(products));
    }


    @PostMapping("/add")
    public ResponseEntity<ApiResponse<String>> addProduct(@RequestBody @Valid Product product, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse<>(Objects.requireNonNull(errors.getFieldError()).getDefaultMessage()));
        }
        int result = productService.createNewProduct(product);

        switch (result){
            case 0: return ResponseEntity.status(400).body(new ApiResponse<>("Product's ID already exists"));
            case 1: return ResponseEntity.status(404).body(new ApiResponse<>("Category not found"));
            default: return ResponseEntity.status(201).body(new ApiResponse<>("Product created successfully"));
        }
    }

    @PutMapping("/update/{product_id}")
    public ResponseEntity<ApiResponse<String>> updateProduct(@PathVariable String product_id, @RequestBody @Valid Product product, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse<>(Objects.requireNonNull(errors.getFieldError()).getDefaultMessage()));
        }

        int result = productService.updateProduct(product_id, product);
        switch (result) {
            case 0:
                return ResponseEntity.status(404).body(new ApiResponse<>("Product not found"));
            case 1:
                return ResponseEntity.status(400).body(new ApiResponse<>("Product's new ID does not match its original ID"));
            default:
                return ResponseEntity.status(200).body(new ApiResponse<>("Product updated successfully"));
        }
    }

    @DeleteMapping("/delete/{product_id}")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable String product_id) {
        boolean isDeleted = productService.deleteProduct(product_id);

        if (isDeleted)
            return ResponseEntity.status(200).body(new ApiResponse<>("Product deleted successfully"));

        return ResponseEntity.status(404).body(new ApiResponse<>("Product not found"));
    }

    /*
     * #4 of the 5 endpoints
     *
     * In this endpoint we can display some of the bestselling products
     *
     */

    @GetMapping("/get-best-sellers/{limit}")
    public ResponseEntity<ApiResponse<ArrayList<Product>>> getBestSellers(@PathVariable int limit){
        ArrayList<Product> bestSellers=productService.getBestSellers(limit);
        return ResponseEntity.status(200).body(new ApiResponse<>(bestSellers));
    }

    /*
    * 1 Bonus endpoint
    *
    * this endpoint get all the reviews of a product
    * if the product doesn't have reviews return an empty arraylist
    *
    */
    @GetMapping("/get-reviews/{product_id}")
    public ResponseEntity<?> getReviews(@PathVariable String product_id){
        ArrayList<String> reviews=productService.getReviews(product_id);

        if (reviews==null)
            return ResponseEntity.status(404).body("Product not found");

        return ResponseEntity.status(200).body(reviews);
    }

    /*
     * This method ia for testing only and must be removed before deployment
     */

//    @PostMapping("/add-all")
//    public ResponseEntity<ApiResponse<String>> addAll(@RequestBody ArrayList<Product> products ){
//        for(Product p: products){
//            productService.createNewProduct(p);
//        }
//        return ResponseEntity.status(200).body(new ApiResponse<>("done"));
//
//    }
}
