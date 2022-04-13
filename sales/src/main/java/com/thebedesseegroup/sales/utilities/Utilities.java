package com.thebedesseegroup.sales.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Display;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.thebedesseegroup.sales.R;
import com.thebedesseegroup.sales.customview.QtySelector;
import com.thebedesseegroup.sales.orderhistory.SavedItem;
import com.thebedesseegroup.sales.orderhistory.SavedOrder;
import com.thebedesseegroup.sales.product.Product;
import com.thebedesseegroup.sales.provider.Contract;
import com.thebedesseegroup.sales.provider.ProviderUtils;
import com.thebedesseegroup.sales.salesman.SalesmanManager;
import com.thebedesseegroup.sales.sharedprefs.SharedPrefsManager;
import com.thebedesseegroup.sales.shoppingcart.ShoppingCart;
import com.thebedesseegroup.sales.shoppingcart.ShoppingCartProduct;
import com.thebedesseegroup.sales.store.Statement;

import java.io.File;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Class containing public static utility functions that are used throughout the app.
 */
public class Utilities {

    public static int [] screenDimens;


    /**
     * Checks if an internet connection (cellular or wifi) exists
     *
     * @param context Context
     * @return True if internet exists, false otherwise
     */
    public static boolean isInternetPresent(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static void removeProductFromShoppingCart(final Context context, final Product product, final OnProductUpdatedListener onProductUpdatedListener) {

        final String orderId = ShoppingCart.getCurrentOrderId(context);

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setCancelable(false);

        final TextView textView = new TextView(context);
        textView.setText("Are you sure you want to remove all " + product.getBrand() + " " + product.getDescription() + " from your shopping cart?");
        textView.setTextColor(Color.BLUE);
        textView.setTextSize(20);
        textView.setPadding(16, 16, 16, 16);

        alertDialog.setView(textView);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.getContentResolver().delete(Contract.SavedItem.CONTENT_URI, Contract.SavedItemColumns.COLUMN_ORDER_ID + " = '" + orderId + "' AND " + Contract.SavedItemColumns.COLUMN_PRODUCT_NUMBER + " = '" + product.getNumber() + "'", null);
                onProductUpdatedListener.onUpdated(0, QtySelector.ItemType.CASE);
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }


    /**
     * Update the shopping cart from anywhere in the app. From here, the user can either
     * add a new product or update the quantity of a product already in the shopping cart.
     *
     * @param product Product to add/update
     * @param selectedQty Quantity of product
     * @param itemType {@link com.thebedesseegroup.sales.customview.QtySelector.ItemType} of item
     */
    public static void updateShoppingCart(final Context context, final Product product, final int selectedQty, final String price, final QtySelector.ItemType itemType, final OnProductUpdatedListener onProductUpdatedListener) {

        boolean updatedQty = false;

        final String orderId = ShoppingCart.getCurrentOrderId(context);
        final ShoppingCartProduct productToSave = new ShoppingCartProduct(product, selectedQty, itemType);
        final SavedItem savedItem = new SavedItem(orderId, productToSave);
        final ContentValues values = ProviderUtils.savedItemToContentValues(savedItem);

        for (final ShoppingCartProduct shoppingCartProduct : ShoppingCart.getCurrentShoppingCart().getProducts()) {
            if(shoppingCartProduct.getProduct().getNumber().equals(product.getNumber())) {

                updatedQty = true;

                shoppingCartProduct.setEnteredPrice(price);

                /** User added same item type */
                if (shoppingCartProduct.getItemType().equals(itemType)) {
                    incrementProductInShoppingCart(context, shoppingCartProduct, selectedQty,
                            //Increment click listener
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final ShoppingCartProduct productToSave2 = new ShoppingCartProduct(product, shoppingCartProduct.getQuantity() + selectedQty, itemType);
                                    final SavedItem savedItem2 = new SavedItem(orderId, productToSave2);
                                    final ContentValues values = ProviderUtils.savedItemToContentValues(savedItem2);

                                    shoppingCartProduct.setQuantity(shoppingCartProduct.getQuantity() + selectedQty);
                                    shoppingCartProduct.setItemType(itemType);

                                    context.getContentResolver().update(Contract.SavedItem.CONTENT_URI, values, Contract.SavedItemColumns.COLUMN_ORDER_ID + " = '" + orderId + "' AND " + Contract.SavedItemColumns.COLUMN_PRODUCT_NUMBER + " = '" + product.getNumber() + "'", null);
                                    Toast.makeText(context, "Added " + selectedQty + " " + product.getBrand() + " " + product.getDescription() + " to your shopping cart.", Toast.LENGTH_SHORT).show();
                                    if (onProductUpdatedListener != null) {
                                        onProductUpdatedListener.onUpdated(shoppingCartProduct.getQuantity(), itemType);
                                    }
                                }},
                            //Replace click listener
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final ShoppingCartProduct productToSave2 = new ShoppingCartProduct(product, shoppingCartProduct.getQuantity() + selectedQty, itemType);
                                    final SavedItem savedItem2 = new SavedItem(orderId, productToSave2);
                                    final ContentValues values = ProviderUtils.savedItemToContentValues(savedItem2);

                                    shoppingCartProduct.setQuantity(selectedQty);
                                    shoppingCartProduct.setItemType(itemType);

                                    context.getContentResolver().update(Contract.SavedItem.CONTENT_URI, values, Contract.SavedItemColumns.COLUMN_ORDER_ID + " = '" + orderId + "' AND " + Contract.SavedItemColumns.COLUMN_PRODUCT_NUMBER + " = '" + product.getNumber() + "'", null);
                                    Toast.makeText(context, "Added " + selectedQty + " " + product.getBrand() + " " + product.getDescription() + " to your shopping cart.", Toast.LENGTH_SHORT).show();
                                    if (onProductUpdatedListener != null) {
                                        onProductUpdatedListener.onUpdated(shoppingCartProduct.getQuantity(), itemType);
                                    }
                                }
                            });
                }

                /** User added different item type */
                else {
                    switchProductTypeInCart(context, shoppingCartProduct, selectedQty, itemType, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            shoppingCartProduct.setQuantity(selectedQty);
                            shoppingCartProduct.setItemType(itemType);
                            context.getContentResolver().update(Contract.SavedItem.CONTENT_URI, values, Contract.SavedItemColumns.COLUMN_ORDER_ID + " = '" + orderId + "' AND " +  Contract.SavedItemColumns.COLUMN_PRODUCT_NUMBER + " = '" + product.getNumber() + "'", null);
                            Toast.makeText(context, "Added " + selectedQty + " " + product.getBrand() + " " + product.getDescription() + " to your shopping cart.", Toast.LENGTH_SHORT).show();
                            if (onProductUpdatedListener != null) {
                                onProductUpdatedListener.onUpdated(selectedQty, itemType);
                            }
                        }
                    });
                }
            }
        }

        if(!updatedQty) {
            final ShoppingCartProduct shoppingCartProduct = new ShoppingCartProduct(product, selectedQty, itemType);
            shoppingCartProduct.setEnteredPrice(price);
            ShoppingCart.getCurrentShoppingCart().addProduct(shoppingCartProduct);
            Toast.makeText(context, "Added " + selectedQty + " " + product.getBrand() + " " + product.getDescription() + " to your shopping cart.", Toast.LENGTH_SHORT).show();

            final Cursor cursor = context.getContentResolver().query(Contract.SavedOrder.CONTENT_URI, null, Contract.SavedOrderColumns.COLUMN_ID + " = '" + savedItem.getOrderId() + "'", null, null);
            if (cursor.moveToFirst()) {
                final SavedOrder order = ProviderUtils.cursorToSavedOrder(cursor);

                if (order != null) {
                    final ContentValues contentValues = new ContentValues(1);
                    contentValues.put(Contract.SavedOrderColumns.COLUMN_NUM_PRODUCTS, order.getNumProducts() + 1);
                    context.getContentResolver().update(Contract.SavedOrder.CONTENT_URI, contentValues, Contract.SavedOrderColumns.COLUMN_ID + " = '" + orderId + "'", null);
                }
            }

            context.getContentResolver().insert(Contract.SavedItem.CONTENT_URI, values);
        }

    }

    public interface OnProductUpdatedListener {
        void onUpdated(int qty, QtySelector.ItemType itemType);
    }

    private static void switchProductTypeInCart(Context context, ShoppingCartProduct shoppingCartProduct, int selectedQty, QtySelector.ItemType itemType, DialogInterface.OnClickListener clickListener) {
        final int currQty = shoppingCartProduct.getQuantity();
        final String currType = shoppingCartProduct.getItemType().name();

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setCancelable(false);

        final TextView textView = new TextView(context);
        textView.setText("You currently have " + currQty + " " + currType + "S of this product in your shopping cart.\n\nAre you sure you want to REMOVE THESE " + currQty + " " + currType + "S AND REPLACE THEM WITH " + selectedQty + " " + itemType.name() + "S?");
        textView.setTextColor(Color.BLUE);
        textView.setTextSize(20);
        textView.setPadding(16, 16, 16, 16);

        alertDialog.setView(textView);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", clickListener);
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private static void incrementProductInShoppingCart(Context context, ShoppingCartProduct shoppingCartProduct, int newQty, DialogInterface.OnClickListener clickListener, DialogInterface.OnClickListener replaceClickListener) {

        final int currQty = shoppingCartProduct.getQuantity();
        final String itemTypeName = shoppingCartProduct.getItemType().name();

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setCancelable(false);

        final TextView textView = new TextView(context);
        textView.setText("You currently have " + currQty + " " + itemTypeName + "S of this product in your shopping cart.\n\nWould you like to add " + newQty + " more " + itemTypeName + "S for a total of " + (newQty + currQty) + "?\n\nOr would you like to replace the " + currQty + " you have for a new total of " + newQty + "?");
        textView.setTextColor(Color.BLUE);
        textView.setTextSize(20);
        textView.setPadding(16, 16, 16, 16);

        alertDialog.setView(textView);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ADD QTY\nNEW TOTAL: " + (currQty + newQty), clickListener);
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "REPLACE QTY\nNEW TOTAL: " + newQty, replaceClickListener);
        alertDialog.show();
    }


    /**
     * Decodes a bitmap to the required size efficiently.
     *
     * @param file Absolute file path
     * @param reqWidth Width
     * @param reqHeight Height
     * @return Decoded Bitmap
     */
    public static Bitmap decodeSampledBitmapFromFile(String file, int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file, options);
    }


    /**
     * Calculates inSample size for image loading.
     *
     * @param options {@link android.graphics.BitmapFactory.Options}
     * @param reqWidth int
     * @param reqHeight int
     * @return Sample size
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    /**
     * Gets the width and height of the screen. If null is passed, this method returns the previously
     * requested parameters.
     *
     * @param activity {@link android.app.Activity}
     * @return int [0] = width, int[1] = height
     */
    public static int [] getScreenDimensInPx(Activity activity) {
        if(activity != null) {
            Display display = activity.getWindowManager().getDefaultDisplay();

            Point size = new Point();
            display.getSize(size);

            int width = size.x;
            int height = size.y;

            screenDimens = new int[]{width, height};
        }

        return screenDimens;
    }


    /**
     * Performs a Toast with {@linkplain android.widget.Toast#setDuration(int)} set to {@linkplain android.widget.Toast#LENGTH_LONG}
     *
     * @param context {@linkplain android.content.Context}
     * @param message {@linkplain java.lang.String}
     */
    public static void longToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }


    /**
     * Performs a Toast with {@linkplain android.widget.Toast#setDuration(int)} set to {@linkplain android.widget.Toast#LENGTH_SHORT}
     *
     * @param context {@linkplain android.content.Context}
     * @param message {@linkplain java.lang.String}
     */
    public static void shortToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    /**
     * Forces the overflow icon to show regardless of device configuration.
     */
    public static void showOverflowIcon(Context context) {
        try {
            ViewConfiguration config = ViewConfiguration.get(context);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ignored) { }
    }


    /**
     * Displays the outstanding balance of a store.
     *
     * @param activity Context from which to show the dialog.
     * @param balance Balance of the store.
     */
    public static void showOutstandingBalance(final Activity activity, Float balance, String lastCollectDate, final String statementUrl) {

        if(!lastCollectDate.equals("")){
            lastCollectDate = "\n" + "LAST COLLECT DATE: " + lastCollectDate;
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setTitle("OUTSTANDING BALANCE")
                .setMessage("AMOUNT OWED: $" + balance + lastCollectDate )
                .setNegativeButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        activity.finish();
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("View Statement", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Statement.show(activity, Statement.DocType.STATEMENT, statementUrl);
                    }
                })
                .setIcon(R.drawable.ic_alert);
        builder.show();
    }


    /**
     * Removes the soft keyboard from the screen.
     *
     * @param activity Activity displaying the soft keyboard.
     */
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null){
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }


    /**
     * Searches for any app containing the word "calculator" in the package name and runs it.
     * Stolen (but modified) from SO. Works great!
     */
    public static void launchRegularCalculator(Context context) {
        final ArrayList<HashMap<String,Object>> items = new ArrayList<>();
        final PackageManager pm = context.getPackageManager();
        final List<PackageInfo> packs = pm.getInstalledPackages(0);
        for (PackageInfo pi : packs) {
            if( pi.packageName.toLowerCase().contains("calculator")){
                final HashMap<String, Object> map = new HashMap<>();
                map.put("appName", pi.applicationInfo.loadLabel(pm));
                map.put("packageName", pi.packageName);
                items.add(map);
            }
        }

        if(items.size() >= 1){
            String packageName = (String) items.get(0).get("packageName");
            final Intent i = pm.getLaunchIntentForPackage(packageName);
            if (i != null)
                context.startActivity(i);
        }
        else{
            Toast.makeText(context, "No calculator app found!", Toast.LENGTH_LONG).show();
        }

    }


    public static void shareImage(final Context context, final String filePath) {
        final Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(filePath));
        context.startActivity(Intent.createChooser(share, "Share Image"));
    }


    public static String emptyIfNull(final String string) {
        return TextUtils.isEmpty(string) ? "" : string;
    }


    public static String getVersionString(final Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public static String getSavedOrderId(final Context context, final String storeName, final Date date) {
        final String dateKey = DateFormat.getDateTimeInstance().format(date);
        final String salesmanEmailKey = SalesmanManager.getCurrentSalesman(context).getEmail().substring(0, 4);

        return storeName + "_" + salesmanEmailKey + dateKey;
    }


    /**
     * Checks if a date (long) is 7 days old or not
     *
     * @param when
     * @return
     */
    public static boolean is7DaysOld(long when) {
        final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
        return (int) ((new Date().getTime() - when)/ DAY_IN_MILLIS) > 7;
    }


    public static void installAp(final Activity activity) {

        final SharedPrefsManager sharedPrefsManager = new SharedPrefsManager(activity);
        final String appPath = sharedPrefsManager.getSugarSyncDir() + sharedPrefsManager.getApkPath();
        final Intent installIntent = new Intent(Intent.ACTION_VIEW);
        installIntent.setDataAndType(Uri.fromFile(new File(appPath)), "application/vnd.android.package-archive");
        installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


//        final Uri packageURI = Uri.parse("package:" + activity.getApplication().getPackageName());
//        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);

        activity.startActivity(installIntent);
//        activity.startActivity(uninstallIntent);
    }

}
