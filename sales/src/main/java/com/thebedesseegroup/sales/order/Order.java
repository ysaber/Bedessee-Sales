package com.thebedesseegroup.sales.order;

import com.thebedesseegroup.sales.salesman.Salesman;
import com.thebedesseegroup.sales.shoppingcart.ShoppingCart;
import com.thebedesseegroup.sales.store.Store;

/**
 * Order object.
 */
public class Order {

    private Salesman mSalesman;
    private Store mStore;
    private ShoppingCart mShoppingCart;

    public Salesman getSalesman() { return mSalesman; }

    public void setSalesman(Salesman salesman) {
        mSalesman = salesman;
    }

    public Store getStore() {
        return mStore;
    }

    public void setStore(Store store) {
        mStore = store;
    }

    public ShoppingCart getShoppingCart() {
        return mShoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        mShoppingCart = shoppingCart;
    }
}
