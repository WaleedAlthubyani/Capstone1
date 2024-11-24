package org.example.ecommercewebsite.Service;

import org.example.ecommercewebsite.Model.Merchant;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
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
}
