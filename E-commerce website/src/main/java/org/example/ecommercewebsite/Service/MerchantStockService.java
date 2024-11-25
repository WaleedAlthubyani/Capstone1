package org.example.ecommercewebsite.Service;

import lombok.RequiredArgsConstructor;
import org.example.ecommercewebsite.Model.Merchant;
import org.example.ecommercewebsite.Model.MerchantStock;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class MerchantStockService {

    HashMap<String, MerchantStock> merchantStocks = new HashMap<>();

    private final ProductService productService;


    public ArrayList<MerchantStock> getAllMerchantStocks(){
        return new ArrayList<>(merchantStocks.values());
    }

    public int addNewMerchantStocks(MerchantStock merchantStock, ArrayList<Merchant> merchants){
        if (merchantStocks.containsKey(merchantStock.getId()))
            return 0;//fail merchant stock already exist
        if (!productService.products.containsKey(merchantStock.getProduct_id()))
            return 1;//fail product not found
        boolean merchantFound=false;
        for (Merchant merchant:merchants){
            if (merchant.getId().equals(merchantStock.getMerchant_id())) {
                merchantFound = true;
                break;
            }
        }
        if (!merchantFound)
            return 2;//fail merchant not found


        merchantStocks.put(merchantStock.getId(),merchantStock);
        return 3;//success
    }

    public int updateMerchantStock(String merchantStockId,MerchantStock merchantStock){
        if (!merchantStocks.containsKey(merchantStockId))
            return 0;//fail merchant stock not found
        if (!merchantStock.getId().equals(merchantStockId))
            return 1;//fail merchant stock ID must match

        merchantStocks.put(merchantStockId,merchantStock);
        return 2;//success
    }

    public boolean deleteMerchantStock(String merchantStockId){
        if (!merchantStocks.containsKey(merchantStockId))
            return false;//fail merchant stock not found

        merchantStocks.remove(merchantStockId);
        return true;//success
    }
}
