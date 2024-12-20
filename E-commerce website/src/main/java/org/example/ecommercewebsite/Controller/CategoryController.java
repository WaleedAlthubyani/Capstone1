package org.example.ecommercewebsite.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommercewebsite.ApiResponse.ApiResponse;
import org.example.ecommercewebsite.Model.Category;
import org.example.ecommercewebsite.Service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<ArrayList<Category>>> getAllCategories(){
        ArrayList<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.status(200).body(new ApiResponse<>(categories));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<String>> addCategory(@RequestBody @Valid Category category, Errors errors){
        if (errors.hasErrors()){
            return ResponseEntity.status(400).body(new ApiResponse<>(Objects.requireNonNull(errors.getFieldError()).getDefaultMessage()));
        }
        boolean isCreated = categoryService.createNewCategory(category);

        if (isCreated)
            return ResponseEntity.status(201).body(new ApiResponse<>("Category created successfully"));

        return ResponseEntity.status(400).body(new ApiResponse<>("Category's ID already exists"));
    }

    @PutMapping("/update/{category_id}")
    public ResponseEntity<ApiResponse<String>> updateCategory(@PathVariable String category_id,@RequestBody @Valid Category category, Errors errors){
        if (errors.hasErrors()){
            return ResponseEntity.status(400).body(new ApiResponse<>(Objects.requireNonNull(errors.getFieldError()).getDefaultMessage()));
        }

        int result = categoryService.updateCategory(category_id,category);
        switch (result){
            case 0: return ResponseEntity.status(404).body(new ApiResponse<>("Category not found"));
            case 1: return ResponseEntity.status(400).body(new ApiResponse<>("Category's new ID does not match its original ID"));
            default: return ResponseEntity.status(200).body(new ApiResponse<>("Category updated successfully"));
        }
    }

    @DeleteMapping("/delete/{category_id}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable String category_id){
        boolean isDeleted= categoryService.deleteCategory(category_id);

        if (isDeleted)
            return ResponseEntity.status(200).body(new ApiResponse<>("Category deleted successfully"));

        return ResponseEntity.status(404).body(new ApiResponse<>("Category not found"));
    }

    /*
    * This method ia for testing only and must be removed before deployment
    */

//    @PostMapping("/add-all")
//    public ResponseEntity<ApiResponse<String>> addAll(@RequestBody ArrayList<Category> categories){
//        for(Category c: categories){
//            categoryService.createNewCategory(c);
//        }
//        return ResponseEntity.status(200).body(new ApiResponse<>("done"));
//
//    }
}
