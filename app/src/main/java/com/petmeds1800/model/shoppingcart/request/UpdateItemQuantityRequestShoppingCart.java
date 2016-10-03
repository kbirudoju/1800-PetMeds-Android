package com.petmeds1800.model.shoppingcart.request;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Sarthak on 9/28/2016.
 */

public class UpdateItemQuantityRequestShoppingCart implements Serializable {

    HashMap<String,String> mCommerceIDQuantityMap = new HashMap<>();

    public UpdateItemQuantityRequestShoppingCart(HashMap<String, String> mCommerceIDQuantityMap) {
        this.mCommerceIDQuantityMap = mCommerceIDQuantityMap;
    }

    public HashMap<String, String> getmCommerceIDQuantityMap() {
        return mCommerceIDQuantityMap;
    }

    public void setmCommerceIDQuantityMap(HashMap<String, String> mCommerceIDQuantityMap) {
        this.mCommerceIDQuantityMap = mCommerceIDQuantityMap;
    }

    public void set_dynSessConf(String _dynSessConf) {
        this.mCommerceIDQuantityMap.put("_dynSessConf",_dynSessConf);
    }
}
