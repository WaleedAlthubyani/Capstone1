package org.example.ecommercewebsite.Service;

import org.example.ecommercewebsite.Model.MerchantStock;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class MerchantStockService {

    HashMap<String, MerchantStock> merchantStocks = new HashMap<>();

    public ArrayList<MerchantStock> getAllMerchantStocks(){
        return new ArrayList<>(merchantStocks.values());
    }

    public boolean addNewMerchantStocks(MerchantStock merchantStock){
        if (merchantStocks.containsKey(merchantStock.getId()))
            return false;//fail merchant stock already exist

        merchantStocks.put(merchantStock.getId(),merchantStock);
        return true;//success
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
