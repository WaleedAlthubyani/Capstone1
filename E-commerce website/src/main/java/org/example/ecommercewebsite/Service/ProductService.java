package org.example.ecommercewebsite.Service;

import lombok.RequiredArgsConstructor;
import org.example.ecommercewebsite.Model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final CategoryService categoryService;
    HashMap<String, Product> products = new HashMap<>();
    private HashMap<String,Product> bestSellerList=new HashMap<>();

    public ArrayList<Product> getAllProducts(){
        return new ArrayList<>(products.values());
    }

    public int createNewProduct(Product product){
        if (products.containsKey(product.getId()))
            return 0;//fail duplicate item
        if (!categoryService.categories.containsKey(product.getCategoryID()))
            return 1;//fail category not found

        products.put(product.getId(),product);
        return 2;//success
    }

    public int updateProduct(String productID,Product product){
        if (!products.containsKey(productID))
            return 0;//fail product not found
        if (!productID.equals(product.getId()))
            return 1;//fail id must match

        products.put(productID,product);
        return 2;//success
    }

    public boolean deleteProduct(String productID){
        if (!products.containsKey(productID))
            return false;//fail product not found

        products.remove(productID);
        return true;//success
    }

    public void bought(String productId){
        if (!bestSellerList.containsKey(productId))
            bestSellerList.put(productId,products.get(productId));
    }

    public ArrayList<Product> getBestSellers(){
        return new ArrayList<>(bestSellerList.values());
    }
}
