package com.thebedesseegroup.sales.order;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.text.format.DateFormat;

import com.thebedesseegroup.sales.customview.QtySelector;
import com.thebedesseegroup.sales.product.Product;
import com.thebedesseegroup.sales.salesman.SalesmanManager;
import com.thebedesseegroup.sales.sharedprefs.SharedPrefsManager;
import com.thebedesseegroup.sales.shoppingcart.ShoppingCart;
import com.thebedesseegroup.sales.shoppingcart.ShoppingCartProduct;
import com.thebedesseegroup.sales.store.NewStoreDialog;
import com.thebedesseegroup.sales.store.Store;
import com.thebedesseegroup.sales.store.StoreManager;
import com.thebedesseegroup.sales.utilities.Utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class contains one single utility function to sendShoppingCart an email via the Gmail app.
 */
public class GMailUtils {

    final private static String GMAIL_PACKAGE_NAME = "com.google.android.gm";

    private static Activity sActivity;
    private static ShoppingCart sShoppingCart;

    public static void sendShoppingCart(Activity activity, ShoppingCart shoppingCart) {

        sActivity = activity;
        sShoppingCart = shoppingCart;
        final Intent intent = getGmailIntent(activity);

        final SharedPrefsManager prefsManager = new SharedPrefsManager(activity);

        //some samples on adding more then one email address
        final String aEmailList[] = prefsManager.getOrderEmailRecips().split(",");
//        final String aEmailList[] = {"sales@bedessee.com", "noreply@bedessee.com", "invor@bedessee.com"};
        final String emailCC[] = {SalesmanManager.getCurrentSalesman(sActivity).getEmail()};

        //all the extras that will be passed to the email app
        intent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
        intent.putExtra(android.content.Intent.EXTRA_CC, emailCC);
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "APP~" + DateFormat.format("yyyy-MM-dd hh:mmaa", new Date()).toString() + "~" + StoreManager.getCurrentStore().getName().split("-")[0] + "~" + StoreManager.getCurrentStore().getAddress().split("\\,")[0] + ".");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, getBody());

        //start the app
        activity.startActivity(intent);
    }


    public static void sendAttachment(final Activity activity, final String subject, final String attachment) {

        final Intent intent = getGmailIntent(activity);
//        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(attachment));
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, new SharedPrefsManager(activity).getStatementEmailSubject());

        //start the app
        activity.startActivity(intent);
    }



    private static Intent getGmailIntent(Activity activity) {
        //set the main intent to ACTION_SEND for looking for applications that share information
        final Intent intent = new Intent(Intent.ACTION_SEND, null);

        //filter out apps that are able to sendShoppingCart plain text
        intent.setType("plain/text");

        //get a list of apps that meet your criteria above
        final List<ResolveInfo> pkgAppsList = activity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY | PackageManager.GET_RESOLVED_FILTER);

        //Cycle through list of apps in list and select the one that matches GMail's package name
        for (ResolveInfo resolveInfo : pkgAppsList) {
            final String packageName = resolveInfo.activityInfo.packageName;
            String className = "";
            if (packageName.equals(GMAIL_PACKAGE_NAME)) {
                className = resolveInfo.activityInfo.name;
                intent.setClassName(packageName, className);
            }
        }
        return intent;
    }


    /**
     * Returns the body of the order email. The following is the template that it follows...
     *
     * prod_num, brand, description, UOM, original_price, level1, level2, level3,: qty PC <—PC optional if pieces selected…also, if PC selected, change UOM format from 3X200ML to just 200ML (remove all after X)
     *
     * salesrep: CURR_SALESMAN_NAME
     *
     * name: CLIENT_NAME
     *
     * company: CUST_ACCT_NUM, CUST_ACCT_NAME, CUST_ACCT_ADDRESS
     *
     * comment1: “comment…”
     *
     * upload: DATE_OF_SALE TIME_OF_SALE
     *
     * duration: DURATION_OF_SALE (always in minutes only...no hours or seconds)
     *
     *
     * @return Body of email.
     */
    private static String getBody() {

        ArrayList<ShoppingCartProduct> shoppingCartProducts = sShoppingCart.getProducts();

        final Set<ShoppingCartProduct> myset = new HashSet<ShoppingCartProduct>(shoppingCartProducts);

        ArrayList<ShoppingCartProduct> sortedList = new ArrayList<>(myset);
        Collections.sort(sortedList);


        String bodyProducts1 = "";
        String bodyProducts2 = "";

        for (ShoppingCartProduct shoppingCartProduct : sortedList) {

            final String price = shoppingCartProduct.getEnteredPrice();

            final Product product = shoppingCartProduct.getProduct();
            final boolean isPiece = shoppingCartProduct.getItemType() == QtySelector.ItemType.PIECE;

            bodyProducts1 = bodyProducts1 + "\n\n" +
                    "~" + product.getNumber() + " : " + shoppingCartProduct.getQuantity() + (isPiece ? " PC" : "") + (TextUtils.isEmpty(price) || price.equalsIgnoreCase("null") ? "" : " [price: " + price + "]");


            bodyProducts2 = bodyProducts2 + "\n\n" +
                    product.getNumber() + ", " +
                    product.getBrand() + ", " +
                    product.getDescription() + ", " +
                    (isPiece ? product.getPieceUom() : product.getCaseUom()) + "," +
                    (isPiece ? product.getPiecePrice() : product.getCasePrice()) + ", " +
                    product.getLevel1Price() + ", " +
                    product.getLevel2Price() + ", " +
                    product.getLevel3Price() + ",: " +
                    shoppingCartProduct.getQuantity() + " " +
                    (isPiece ? "PC" : "") +
                    (TextUtils.isEmpty(price) || price.equalsIgnoreCase("null") ? "" : " [price: " + price + "]");
        }

        final String bodyProducts = bodyProducts1 + "\n\n" + bodyProducts2;

        final Store store = StoreManager.getCurrentStore();

        if(store == null) {
            return "ERROR IN STORE INFORMATION. PLEASE MAKE SURE A STORE IS SELECTED.";
        }

        final String date = Utilities.emptyIfNull(DateFormat.format("yyyy-MM-dd hh:mm aa", new Date()).toString());


        if(!NewStoreDialog.getInstance().isNewCustomer()) {

            final String currentSalesmanName = Utilities.emptyIfNull(SalesmanManager.getCurrentSalesman(sActivity).getName());
            final String contact = Utilities.emptyIfNull(sShoppingCart.getContact());
            final String storeNumber = Utilities.emptyIfNull(store.getNumber());
            final String storeName = Utilities.emptyIfNull(store.getName());
            final String storeAddress = Utilities.emptyIfNull(store.getAddress());
            final String comment = Utilities.emptyIfNull(sShoppingCart.getComment());
            final String duration = Utilities.emptyIfNull(sShoppingCart.getSaleDuration());


            return bodyProducts +
                    "\n\n" +
                    "salesrep: " + currentSalesmanName +
                    "\n\n" +
                    "name:" + contact +
                    "\n\n" +
                    "company: " + storeNumber + ", " + storeName + ", " + storeAddress +
                    "\n\n" +
                    "comment1: " + comment +
                    "\n\n" +
                    "upload: " + date +
                    "\n\n" +
                    "duration: " + duration +
                    "\n\n" +
                    "APP VERSION: " + Utilities.getVersionString(sActivity);
        } else {
            final NewStoreDialog newStoreDialog = NewStoreDialog.getInstance();
            return bodyProducts +
                    "\n\n" +
                    "salesrep: " + Utilities.emptyIfNull(SalesmanManager.getCurrentSalesman(sActivity).getName()) +
                    "\n\n" +
                    "name:" + Utilities.emptyIfNull(sShoppingCart.getContact()) +
                    "\n\n" +

                    "==================== NEW CUSTOMER ====================" +
                    "\n\n" +


                    "new customer contact name: " + Utilities.emptyIfNull(newStoreDialog.getContactName()) +
                    "\n\n" +
                    "new customer company name: " + Utilities.emptyIfNull(newStoreDialog.getCompanyName()) +
                    "\n\n" +
                    "new customer address: " + Utilities.emptyIfNull(newStoreDialog.getAddress()) +
                    "\n\n" +
                    "new customer city: " + Utilities.emptyIfNull(newStoreDialog.getCity()) +
                    "\n\n" +
                    "new customer province: " + Utilities.emptyIfNull(newStoreDialog.getProvince()) +
                    "\n\n" +
                    "new customer country: " + Utilities.emptyIfNull(newStoreDialog.getCountry()) +
                    "\n\n" +
                    "new customer postal code: " + Utilities.emptyIfNull(newStoreDialog.getPostalCode()) +
                    "\n\n" +
                    "new customer telephone: " + Utilities.emptyIfNull(newStoreDialog.getTelephone()) +
                    "\n\n" +
                    "new customer email: " + Utilities.emptyIfNull(newStoreDialog.getEmail()) +
                    "\n\n" +

                    "=============== END NEW CUSTOMER INFO ===============" +
                    "\n\n" +


                    "comment1: " + Utilities.emptyIfNull(sShoppingCart.getComment()) +
                    "\n\n" +
                    "upload: " + Utilities.emptyIfNull(date) +
                    "\n\n" +
                    "duration: " + Utilities.emptyIfNull(sShoppingCart.getSaleDuration()) +
                    "\n\n" +
                    "APP VERSION: " + Utilities.getVersionString(sActivity);
        }
    }

}
