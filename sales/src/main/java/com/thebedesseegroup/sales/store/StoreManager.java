package com.thebedesseegroup.sales.store;

/**
 * Simple class to manage current store.
 */
public class StoreManager {

    private static Store mCurrentStore;

    public static Store getCurrentStore() {
        return mCurrentStore;
    }

    public static void setCurrentStore(Store currentStore) {
        mCurrentStore = currentStore;
    }

    public static void clearCurrentStore() {
        mCurrentStore = null;
    }

    public static boolean isStoreSelected() {
        return mCurrentStore != null;
    }
}
