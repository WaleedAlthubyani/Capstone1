package org.example.ecommercewebsite.Service;

import lombok.RequiredArgsConstructor;
import org.example.ecommercewebsite.Model.Merchant;
import org.example.ecommercewebsite.Model.MerchantStock;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class MerchantService {

    HashMap<String, Merchant> merchants = new HashMap<>();

    public ArrayList<Merchant> getAllMerchants(){
        return new ArrayList<>(merchants.values());
    }

    public boolean createNewMerchant(Merchant merchant){
        if (merchants.containsKey(merchant.getId()))
            return false;//fail merchant already exist

        merchants.put(merchant.getId(),merchant);
        return true;//success
    }

    public int updateMerchant(String merchantId, Merchant merchant){
        if (!merchants.containsKey(merchantId))
            return 0;//fail merchant not found
        if (!merchantId.equals(merchant.getId()))
            return 1;//fail id must match

        merchants.put(merchantId,merchant);
        return 2;//success
    }

    public boolean deleteMerchant(String merchantId){
        if (!merchants.containsKey(merchantId))
            return false;//fail merchant not found

        merchants.remove(merchantId);
        return true;//success
    }

    private final ProductService productService;
    private final MerchantStockService merchantStockService;
    public int addStocks(String productId, String merchantId, int stockAmount){
        if (!productService.products.containsKey(productId))
            return 0;//fail product not found
        if (!merchants.containsKey(merchantId))
            return 1;//fail merchant not found

        for (MerchantStock merchantStock:merchantStockService.merchantStocks.values()){
            if (merchantStock.getMerchant_id().equals(merchantId)){//look for the merchant in the merchant stock map
                if (merchantStock.getProduct_id().equals(productId)) {//look for the product id that matches the merchant id in the merchant stock map
                    merchantStock.setStock(merchantStock.getStock()+stockAmount);//increment the stock by the amount we have
                    return 2;//success
                }
            }
        }
        return 3;//merchant doesn't sell this product
    }
}
