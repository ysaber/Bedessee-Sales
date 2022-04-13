package com.thebedesseegroup.sales.orderhistory;

import com.thebedesseegroup.sales.shoppingcart.ShoppingCartProduct;

/**
 * Saved Item POJO
 */
public class SavedItem {

    private String mOrderId;

    private ShoppingCartProduct mShoppingCartProduct;

    public SavedItem(String orderId, ShoppingCartProduct shoppingCartProduct) {
        mOrderId = orderId;
        mShoppingCartProduct = shoppingCartProduct;
    }

    public String getOrderId() {
        return mOrderId;
    }

    public void setOrderId(String orderId) {
        mOrderId = orderId;
    }

    public ShoppingCartProduct getShoppingCartProduct() {
        return mShoppingCartProduct;
    }

    public void setShoppingCartProduct(ShoppingCartProduct shoppingCartProduct) {
        mShoppingCartProduct = shoppingCartProduct;
    }
}
