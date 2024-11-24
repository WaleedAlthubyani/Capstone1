package org.example.ecommercewebsite.Service;

import org.example.ecommercewebsite.Model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class CategoryService {
    HashMap<String, Category> categories = new HashMap<>();

    public ArrayList<Category> getAllCategories(){
        return new ArrayList<>(categories.values());
    }

    public boolean createNewCategory(Category category){
        if (categories.containsKey(category.getId()))
            return false;//fail id already exist

        categories.put(category.getId(),category);
        return true;
    }

    public int updateCategory(String categoryId, Category category){
        if (!categories.containsKey(categoryId))
            return 0;//fail user not found
        if (!category.getId().equals(categoryId))
            return 1;//fail id not matching

        categories.put(categoryId,category);
        return 2;
    }

    public boolean deleteCategory(String categoryId){
        if (!categories.containsKey(categoryId))
            return false;

        categories.remove(categoryId);
        return true;
    }
}
