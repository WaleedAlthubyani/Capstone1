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

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/get")
    public ResponseEntity getAllCategories(){
        ArrayList<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.status(200).body(categories);
    }

    @PostMapping("/add")
    public ResponseEntity addCategory(@RequestBody @Valid Category category, Errors errors){
        if (errors.hasErrors()){
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        boolean isCreated = categoryService.createNewCategory(category);

        if (isCreated)
            return ResponseEntity.status(201).body(new ApiResponse("Category created successfully"));

        return ResponseEntity.status(400).body(new ApiResponse("Category's ID already exists"));
    }

    @PutMapping("/update/{category_id}")
    public ResponseEntity updateCategory(@PathVariable String category_id,@RequestBody @Valid Category category, Errors errors){
        if (errors.hasErrors()){
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }

        int result = categoryService.updateCategory(category_id,category);
        switch (result){
            case 0: return ResponseEntity.status(404).body(new ApiResponse("Category not found"));
            case 1: return ResponseEntity.status(400).body(new ApiResponse("Category's new ID does not match its original ID"));
            default: return ResponseEntity.status(200).body(new ApiResponse("Category updated successfully"));
        }
    }

    @DeleteMapping("/delete/{category_id}")
    public ResponseEntity deleteCategory(@PathVariable String category_id){
        boolean isDeleted= categoryService.deleteCategory(category_id);

        if (isDeleted)
            return ResponseEntity.status(200).body(new ApiResponse("Category deleted successfully"));

        return ResponseEntity.status(404).body(new ApiResponse("Category not found"));
    }

}
