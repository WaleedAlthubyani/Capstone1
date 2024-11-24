package org.example.ecommercewebsite.Service;

import lombok.RequiredArgsConstructor;
import org.example.ecommercewebsite.Model.MerchantStock;
import org.example.ecommercewebsite.Model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class UserService {

    HashMap<String, User> users = new HashMap<>();

    public ArrayList<User> getAllUsers(){
        return new ArrayList<>(users.values());
    }

    public boolean createNewUser(User user){
        if (users.containsKey(user.getId()))
            return false;//fail user already exist

        users.put(user.getId(),user);
        return true;
    }

    public int updateUser(String userId,User user){
        if (!users.containsKey(userId))
            return 0;//fail user not found
        if (!userId.equals(user.getId()))
            return 1;//fail user ID must match

        users.put(userId,user);
        return 2;
    }

    public boolean deleteUser(String userId){
        if (!users.containsKey(userId))
            return false;//fail user not found

        users.remove(userId);
        return true;
    }

    private final ProductService productService;
    private final MerchantService merchantService;
    private final MerchantStockService merchantStockService;
    public int buyProduct(String userId,String productId, String merchantId){
        if (!users.containsKey(userId))
            return 0;//fail user not found
        if (!productService.products.containsKey(productId))
            return 1;//fail product not found
        if (!merchantService.merchants.containsKey(merchantId))
            return 2;//fail merchant not found

        String merchantStockId="";
        for (MerchantStock merchantStock:merchantStockService.merchantStocks.values()){
            if (merchantStock.getMerchant_id().equals(merchantId)){
                if (merchantStock.getProduct_id().equals(productId)){
                    merchantStockId=merchantStock.getId();
                }
            }
        }

        if (merchantStockId.isEmpty())
            return 3;//fail merchant doesn't sell product

        if (merchantStockService.merchantStocks.get(merchantStockId).getStock()<=0)
            return 4;//fail sold out

        if (users.get(userId).getBalance()<productService.products.get(productId).getPrice())
            return 5;//fail not enough money

        //reduce stock by 1
        merchantStockService.merchantStocks.get(merchantStockId).setStock(merchantStockService.merchantStocks.get(merchantStockId).getStock()-1);

        //reduce the balance by the price of the product
        users.get(userId).setBalance(users.get(userId).getBalance()-productService.products.get(productId).getPrice());

        return 6;
    }
}
