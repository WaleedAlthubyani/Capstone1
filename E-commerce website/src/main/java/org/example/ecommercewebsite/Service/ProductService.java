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
    ArrayList<String> bestSellerList=new ArrayList<>();
    ArrayList<Integer> bestSellerAmount=new ArrayList<>();

    public ArrayList<Product> getAllProducts(){
        return new ArrayList<>(products.values());
    }

    public int createNewProduct(Product product){
        if (products.containsKey(product.getId()))
            return 0;//fail duplicate item
        if (!categoryService.categories.containsKey(product.getCategoryId()))
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
        for (int i = 0; i < bestSellerList.size(); i++) {
            if (bestSellerList.get(i).equals(productId)){
                bestSellerAmount.set(i,bestSellerAmount.get(i)+1);
                sortBestSellerList(i);
                return;
            }
        }

        bestSellerList.add(productId);
        bestSellerAmount.add(1);
    }

    public ArrayList<Product> getBestSellers(int limit){
        ArrayList<Product> bestSellers=new ArrayList<>();

        if (limit>bestSellerList.size())
            limit=bestSellerList.size();

        for (int i = 0; i < limit; i++) {
            bestSellers.add(products.get(bestSellerList.get(i)));
        }

        return bestSellers;
    }

    public void sortBestSellerList(int i){
        if (i==0)
            return;

        int temp;
        for (int j = i; j > 0 ; j--) {
            if (bestSellerAmount.get(j)>bestSellerAmount.get(j-1)){
                temp=bestSellerAmount.get(j-1);
                bestSellerAmount.set(j-1,bestSellerAmount.get(j));
                bestSellerAmount.set(j,temp);
            }
        }
    }

    public ArrayList<String> getReviews(String productId){
        if (!products.containsKey(productId)){
            return null;
        }
        if (products.get(productId).getReviews()==null){
            ArrayList<String> reviews=new ArrayList<>();
            products.get(productId).setReviews(reviews);
        }

        return products.get(productId).getReviews();
    }
}
