package com.thebedesseegroup.sales.salesmanstore;

import com.thebedesseegroup.sales.salesman.Salesman;
import com.thebedesseegroup.sales.store.Store;

/**
 * Salesman and Store relation model
 */
public class SalesmanStore {

    private Salesman mSalesman;
    private Store mStore;

    public SalesmanStore(Salesman salesman, Store store) {
        mSalesman = salesman;
        mStore = store;
    }

    public SalesmanStore() {
    }

    public Salesman getSalesman() {
        return mSalesman;
    }

    public void setSalesman(Salesman salesman) {
        mSalesman = salesman;
    }

    public Store getStore() {
        return mStore;
    }

    public void setStore(Store store) {
        mStore = store;
    }

    @Override
    public String toString() {
        return "SalesmanStore{" +
                "mSalesman=" + mSalesman +
                ", mStore=" + mStore +
                '}';
    }
}
