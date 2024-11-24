package org.example.ecommercewebsite.Service;

import org.example.ecommercewebsite.Model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class ProductService {

    HashMap<String, Product> products = new HashMap<>();

    public ArrayList<Product> getAllProducts(){
        return new ArrayList<>(products.values());
    }

    public boolean createNeaProduct(Product product){
        if (products.containsKey(product.getId()))
            return false;//fail duplicate item

        products.put(product.getId(),product);
        return true;
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
}
