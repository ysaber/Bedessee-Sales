package com.thebedesseegroup.sales.shoppingcart;

import android.content.Context;
import android.database.Cursor;
import android.os.SystemClock;

import com.thebedesseegroup.sales.orderhistory.SavedItem;
import com.thebedesseegroup.sales.provider.Contract;
import com.thebedesseegroup.sales.provider.ProviderUtils;
import com.thebedesseegroup.sales.sharedprefs.SharedPrefsManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Shopping Cart Manager.
 */
public class ShoppingCart implements Serializable {

    private static ShoppingCart sCurrentShoppingCart;

    private String mComment;
    private String mContact;

    private long mMillisStart;
    private long mMillisEnd;
    private String mSaleDuration;
    private String mUploadTime = "11:23 AM 2014/01/01";

//    final private HashSet<ShoppingCartProduct> mProducts = new HashSet<>();
    final private ArrayList<ShoppingCartProduct> mProducts = new ArrayList<>();


    private static String sCurrentOrderId;


    public static ShoppingCart getCurrentShoppingCart() {
        if(sCurrentShoppingCart == null) {
            sCurrentShoppingCart = new ShoppingCart();
        }
        return sCurrentShoppingCart;
    }

    public static void setCurrentOrderId(final Context context, final String currentOrderId) {
        sCurrentOrderId = currentOrderId;

        SharedPrefsManager sharedPrefsManager = new SharedPrefsManager(context);
        sharedPrefsManager.setCurrentOrderId(currentOrderId);
    }

    public static String getCurrentOrderId(final Context context) {
        if(sCurrentOrderId == null) {
            SharedPrefsManager sharedPrefsManager = new SharedPrefsManager(context);
            sCurrentOrderId = sharedPrefsManager.getCurrentOrderId();
        }
        return sCurrentOrderId;
    }

    public void addProduct(ShoppingCartProduct product) {
        mProducts.add(product);
        Collections.sort(mProducts);
    }

//    public HashSet<ShoppingCartProduct> getProducts() {
//        return mProducts;
//    }
    public ArrayList<ShoppingCartProduct> getProducts() {
        return mProducts;
    }

//    public ArrayList<ShoppingCartProduct> getProductsArrayList() {
//        return new ArrayList<>(mProducts);
//    }

    public void clearProducts() {
        mProducts.clear();
    }


    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        mComment = comment;
    }

    public void clearComment() {
        mComment = null;
    }
    public void clearContact() {
        mContact = null;
    }

    public String getContact() {
        return mContact;
    }

    public void setContact(String contact) {
        mContact = contact;
    }

    public void startTimer() {
        mMillisStart = SystemClock.uptimeMillis();
    }

    public void stopTimer(){
        mMillisEnd = SystemClock.uptimeMillis() - mMillisStart;
        mSaleDuration = String.format("%d:%d:%d",
                TimeUnit.MILLISECONDS.toHours(mMillisEnd),
                TimeUnit.MILLISECONDS.toMinutes(mMillisEnd) - TimeUnit.MILLISECONDS.toMinutes(TimeUnit.MILLISECONDS.toHours(mMillisEnd)),
                TimeUnit.MILLISECONDS.toSeconds(mMillisEnd) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mMillisEnd))
        );
    }

    public void setSaleDuration(String duration) {
        mSaleDuration = duration;
    }

    public String getSaleDuration() {
        return mSaleDuration;
    }

    public void setUploadTime(String time) {
        mUploadTime = time;
    }

    public String getUploadTime() {
        return mUploadTime;
    }



    public static ShoppingCart getSavedOrder(final Context context, final String savedOrderId) {

        final List<SavedItem> savedItems = new ArrayList<>();

        final Cursor cursor = context.getContentResolver().query(Contract.SavedItem.CONTENT_URI, null, Contract.SavedItemColumns.COLUMN_ORDER_ID + " = '" + savedOrderId + "'", null, null);

        while (cursor.moveToNext()) {
            final SavedItem savedItem = ProviderUtils.cursorToSavedItem(context, cursor);
            savedItems.add(savedItem);
        }

        final ShoppingCart shoppingCart = new ShoppingCart();

        for (final SavedItem savedItem : savedItems) {
            shoppingCart.addProduct(savedItem.getShoppingCartProduct());
        }

        return shoppingCart;
    }


    public static void setCurrentShoppingCart(final ShoppingCart shoppingCart) {
        sCurrentShoppingCart = shoppingCart;
    }

    public void clear() {
        clearComment();
        clearContact();
        clearProducts();
    }
}
