package org.example.ecommercewebsite.Service;

import lombok.RequiredArgsConstructor;
import org.example.ecommercewebsite.Model.MerchantStock;
import org.example.ecommercewebsite.Model.Product;
import org.example.ecommercewebsite.Model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class UserService {

    HashMap<String, User> users = new HashMap<>();
    HashMap<String,String> askingForARefundList=new HashMap<>();
    HashMap<String,ArrayList<Product>> purchaseHistory=new HashMap<>();

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
                    break;
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

        //send the purchased item to the bestsellers list
        productService.bought(productId);

        //reduce the balance by the price of the product
        users.get(userId).setBalance(users.get(userId).getBalance()-productService.products.get(productId).getPrice());

        if (purchaseHistory.containsKey(userId))
            purchaseHistory.get(userId).add(productService.products.get(productId));
        else {
            ArrayList<Product> products = new ArrayList<>();
            products.add(productService.products.get(productId));
            purchaseHistory.put(userId,products);
        }

        return 6;
    }

    public ArrayList<Product> getPurchaseHistory(String userId){
        return purchaseHistory.get(userId);
    }

    public int addReviewToProduct(String userId, String productId,String message){
        int result=validatingUserIdProductIdMessage(userId,productId,message);

        if (result<4)
            return result;

        if (productService.products.get(productId).getReviews()==null){
            ArrayList<String> reviews=new ArrayList<>();
            reviews.add(message);
            productService.products.get(productId).setReviews(reviews);
        }else
            productService.products.get(productId).getReviews().add(message);
        return 4;//success
    }

    public int requestRefund(String userId, String productId,String message){
        int result=validatingUserIdProductIdMessage(userId,productId,message);

        if (result<4)
            return result;

        askingForARefundList.put(userId+" "+productId,message);
        return 4;
    }

    public HashMap<String,String> getRefundList(String userId){
        HashMap<String,String> result=new HashMap<>();

        if (!users.containsKey(userId)){
            result.put("result0","User not found");
            return result;
        }
        if (!users.get(userId).getRole().equals("Admin")){
            result.put("result1","You are not authorized to do this action");
            return result;
        }

        return askingForARefundList;
    }

    public int decideRefund(String adminId,String requesterId,String productId,boolean decision){
        if (!users.containsKey(adminId))
            return 0;//fail admin not found
        if (!users.get(adminId).getRole().equals("Admin"))
            return 1;//fail not authorized
        if (!users.containsKey(requesterId))
            return 2;//fail requester not found
        if (!productService.products.containsKey(productId))
            return 3;//fail product not found
        if (!askingForARefundList.containsKey(requesterId+" "+productId))
            return 4;//fail refund request not found

        if (adminId.equals(requesterId))
            return 5;//fail you can't decide your own refund

        if (!decision){
            askingForARefundList.remove(requesterId+" "+productId);
            return 6;//success
        }

        users.get(requesterId).setBalance(users.get(requesterId).getBalance()+productService.products.get(productId).getPrice());
        askingForARefundList.remove(requesterId+" "+productId);

        return 7;//success
    }

    public int banMerchant(String userId,String merchantId,String reason){
        if (!users.containsKey(userId))
            return 0;//fail user not found
        if (!users.get(userId).getRole().equals("Admin"))
            return 1;//fail not authorized
        if (!merchantService.merchants.containsKey(merchantId))
            return 2;//fail merchant not found

        merchantService.deleteMerchant(merchantId);
        merchantService.bannedMerchants.put(merchantId,reason);

        for (MerchantStock m : merchantStockService.merchantStocks.values()){
            if (m.getMerchant_id().equals(merchantId)){
                merchantStockService.merchantStocks.remove(m.getId());
                break;
            }
        }
        return 3;

    }

    public int validatingUserIdProductIdMessage(String userId, String productId,String message){
        if (!users.containsKey(userId))
            return 0;//fail user not found

        ArrayList<Product> purchased=purchaseHistory.get(userId);
        if (purchased==null)
            return 1;

        boolean isPurchased=false;

        for (Product product : purchased) {
            if (product.getId().equals(productId)) {
                isPurchased = true;
                break;
            }
        }

        if (!isPurchased)
            return 1;//fail can't review a product you didn't buy

        int messageLimit=500;
        if (message.length()>messageLimit)
            return 2;//review too long must be 500 characters or fewer
        if (message.isEmpty())
            return 3;

        return 4;
    }


}
