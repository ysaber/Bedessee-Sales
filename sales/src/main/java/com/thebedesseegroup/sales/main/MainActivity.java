package com.thebedesseegroup.sales.main;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.thebedesseegroup.sales.R;
import com.thebedesseegroup.sales.customview.NumberPad;
import com.thebedesseegroup.sales.customview.QtySelector;
import com.thebedesseegroup.sales.login.GoogleApiHelper;
import com.thebedesseegroup.sales.login.Login;
import com.thebedesseegroup.sales.mixpanel.MixPanelManager;
import com.thebedesseegroup.sales.store.NewStoreDialog;
import com.thebedesseegroup.sales.orderhistory.OrderHistoryActivity;
import com.thebedesseegroup.sales.orderhistory.SavedOrder;
import com.thebedesseegroup.sales.product.Product;
import com.thebedesseegroup.sales.product.ProductFragment;
import com.thebedesseegroup.sales.product.SpecialProductDialog;
import com.thebedesseegroup.sales.product.brand.BrandFragment;
import com.thebedesseegroup.sales.product.category.CategoryFragment;
import com.thebedesseegroup.sales.product.status.Status;
import com.thebedesseegroup.sales.provider.Contract;
import com.thebedesseegroup.sales.provider.ProviderUtils;
import com.thebedesseegroup.sales.salesman.Salesman;
import com.thebedesseegroup.sales.salesman.SalesmanManager;
import com.thebedesseegroup.sales.salesmanstore.SalesmanStore;
import com.thebedesseegroup.sales.sellsheets.SellSheetsDialog;
import com.thebedesseegroup.sales.sharedprefs.SharedPrefsManager;
import com.thebedesseegroup.sales.shoppingcart.ShoppingCart;
import com.thebedesseegroup.sales.shoppingcart.ShoppingCartDialog;
import com.thebedesseegroup.sales.store.Statement;
import com.thebedesseegroup.sales.store.Store;
import com.thebedesseegroup.sales.store.StoreManager;
import com.thebedesseegroup.sales.store.StoreSelector;
import com.thebedesseegroup.sales.update.UpdateActivity;
import com.thebedesseegroup.sales.utilities.Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Main Activity for application.
 */
public class MainActivity extends FragmentActivity {

    public static MainMenuAdapter sMainMenuAdapter;

    static ArrayList<String> leftMenuItems;

    public Menu mMenu;

    public static AlertDialog mProductDetailsAlertDialog;

    private FragmentManager mFragmentManager;

    private GoogleApiClient mGoogleApiClient;

    private static boolean reportsSpinnerInit = false;
    private static boolean reportsOpen = false;

    private boolean mShowBalanceDialog = false;

    private Salesman mSalesman;


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //  LIFECYCLE METHODS                                                                         //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Crashlytics.start(this);

        setContentView(R.layout.activity_main);

        mGoogleApiClient = GoogleApiHelper.getGoogleApiClient();

        deleteOldSavedOrders();

        checkForSavedOrder();

        final SharedPrefsManager sharedPrefsManager = new SharedPrefsManager(this);


        mFragmentManager = getFragmentManager();

        /**
         * Set the screen dimens as the first thing to do so that it can be accessed later with
         * a null parameter.
         */
        Utilities.getScreenDimensInPx(this);

        /** Instantiate all views in activity */
        final ListView listViewFilters = (ListView) findViewById(R.id.listView_filters);


        final ArrayList<SideMenu> sideMenus = new ArrayList<>();

        sideMenus.add(new SideMenu("#000000", "0", "PRODUCTS", "0", ""));
        sideMenus.add(new SideMenu("#000000", "0", "BRANDS", "0", ""));
        sideMenus.add(new SideMenu("#000000", "0", "CATEGORIES", "0", ""));
        sideMenus.add(new SideMenu("#000000", "0", "", "0", ""));

        final Cursor cursorSideMenu = getContentResolver().query(Contract.SideMenu.CONTENT_URI, null, null, null, Contract.SideMenuColumns.COLUMN_SORT + " ASC");
        while (cursorSideMenu.moveToNext()) {
            sideMenus.add(ProviderUtils.cursorToSideMenu(cursorSideMenu));
        }
        cursorSideMenu.close();


        /** Temporarily set the adapter for the left list view */
        sMainMenuAdapter = new MainMenuAdapter(this, android.R.layout.simple_spinner_dropdown_item, sideMenus);

        listViewFilters.setAdapter(sMainMenuAdapter);
        listViewFilters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });



        /** Set currentSalesman as logged in user */
        final String salesmanName = sharedPrefsManager.getLoggedInUser();//SharedPrefsManager.getSelectedItem().toString();


        if (salesmanName != null) {
            final Cursor cursor = getContentResolver().query(Contract.SalesmanStore.CONTENT_URI, null, Contract.SalesmanStoreColumns.COLUMN_SALESPERSON + " = '" + salesmanName + "'", null, null);
            if (cursor != null && cursor.moveToFirst()) {
                final SalesmanStore salesmanStore = ProviderUtils.cursorToSalesmanStore(cursor);
                mSalesman = salesmanStore.getSalesman();
                SalesmanManager.setCurrentSalesman(this, mSalesman);
            }
        }


        /** Go to landing page fragment: Categories */
        switchFragment(new CategoryFragment(), CategoryFragment.TAG);

        /** Get current salesman. */
        SalesmanManager.getCurrentSalesman(this);

        /** Force show overflow icon */
        Utilities.showOverflowIcon(this);

        findViewById(R.id.btn_sell_sheets).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MixPanelManager.trackButtonClick(MainActivity.this, "Button click: Sell sheets");
                startActivityForResult(new Intent(MainActivity.this, SellSheetsDialog.class), SellSheetsDialog.RESULT_CODE);
            }
        });

        final SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.reports_array, android.R.layout.simple_spinner_dropdown_item);

        final Spinner reportsSpinner = (Spinner) findViewById(R.id.spinner_reports);
        reportsSpinner.setAdapter(spinnerAdapter);
        reportsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(reportsSpinnerInit && !reportsOpen && position > 0) {

                    reportsOpen = true;

                    final Store store = StoreManager.getCurrentStore();

                    if (store != null && store.getStatementUrl() != null) {

                        final String statementUrl = store.getStatementUrl();

                        final String sugarSyncDir = new SharedPrefsManager(MainActivity.this).getSugarSyncDir();

                        final String[] urlParts = statementUrl.split("/");
                        final String fileName = urlParts[urlParts.length - 1];

                        boolean docExists = false;

                        Statement.DocType docType = null;
                        switch (position) {
                            case 1: {
                                docType = Statement.DocType.R336;
                                if (new File(sugarSyncDir + docType.getDir() + "SLD-" + fileName).exists()) {
                                    docExists = true;
                                }
                            }
                            break;

                            case 2: {
                                docType = Statement.DocType.RECEIPT;
                                if (new File(sugarSyncDir + docType.getDir() + fileName).exists()) {
                                    docExists = true;
                                }
                            }
                            break;
                        }
                        if (docType != null && docExists) {
                            Statement.show(MainActivity.this, docType, StoreManager.getCurrentStore().getStatementUrl());
                        } else {
                            Utilities.shortToast(MainActivity.this, "Document not found");
                            reportsOpen = false;
                            reportsSpinner.setSelection(0);
                        }
                    } else {
                        Utilities.shortToast(MainActivity.this, "Document not found");
                        reportsOpen = false;
                        reportsSpinner.setSelection(0);
                    }
                } else {
                    reportsSpinnerInit = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }





    private void deleteOldSavedOrders() {
        final Cursor cursor = getContentResolver().query(Contract.SavedOrder.CONTENT_URI, null, null, null, null);
        final ArrayList<SavedOrder> savedOrders = new ArrayList<>();
        while (cursor.moveToNext()) {
            final SavedOrder savedOrder = ProviderUtils.cursorToSavedOrder(cursor);
            savedOrders.add(savedOrder);
        }

        for (SavedOrder savedOrder : savedOrders) {
            if (Utilities.is7DaysOld(savedOrder.getStartTime().getTime())) {
                getContentResolver().delete(Contract.SavedOrder.CONTENT_URI, Contract.SavedOrderColumns.COLUMN_ID + " = ?", new String[]{savedOrder.getId()});
            }
        }

        cursor.close();
    }


    private void checkForSavedOrder() {
        final String savedOrderId =  ShoppingCart.getCurrentOrderId(MainActivity.this);

        if (!TextUtils.isEmpty(savedOrderId)) {

            final Cursor cursor = getContentResolver().query(Contract.SavedOrder.CONTENT_URI, null, Contract.SavedOrderColumns.COLUMN_ID + " = '" + savedOrderId + "'", null, null);

            int numProducts = 0;

            if (cursor.moveToFirst()) {
                final SavedOrder savedOrder = ProviderUtils.cursorToSavedOrder(cursor);
                numProducts = savedOrder.getNumProducts();
            }
            cursor.close();

            final String storeName = savedOrderId.split("_")[0];

            final ShoppingCart shoppingCart = ShoppingCart.getSavedOrder(MainActivity.this, savedOrderId);

            if (!shoppingCart.getProducts().isEmpty()) {

                /**Load saved order */
                final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setCancelable(false);

                final TextView textView = new TextView(this);
                textView.setText("An in-progress order has been detected at " + storeName + " containing " + numProducts + " products");
                textView.setTextColor(Color.RED);
                textView.setTextSize(20);
                textView.setPadding(16, 16, 16, 16);

                alertDialog.setCustomTitle(textView);
                alertDialog.setMessage("Would you like to resume this order?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int which) {
                                /** Load saved order */

                                MixPanelManager.trackButtonClick(MainActivity.this, "Button click: Resume saved order? YES");

                                final Cursor cursor = getContentResolver().query(Contract.SalesmanStore.CONTENT_URI, null, Contract.SalesmanStoreColumns.COLUMN_CUST_NAME + " = '" + storeName + "'", null, null);

                                if (cursor.moveToFirst()) {
                                    final SalesmanStore salesmanStore = ProviderUtils.cursorToSalesmanStore(cursor);

                                    final Store store = salesmanStore.getStore();

                                    StoreManager.setCurrentStore(store);

                                    Utilities.showOutstandingBalance(MainActivity.this, Float.parseFloat(store.getOutstadingBalanceDue()), store.getLastCollectDate(), store.getStatementUrl());

                                    displayStoreInActionBar();
                                }

                                cursor.close();

                                ShoppingCart.setCurrentShoppingCart(shoppingCart);
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int which) {
                                /** Clear currently saved order */
                                MixPanelManager.trackButtonClick(MainActivity.this, "Button click: Resume saved order? NO");
                                ShoppingCart.setCurrentOrderId(MainActivity.this, null);
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        }
    }



    @Override
    protected void onPostResume() {
        super.onPostResume();
        mSalesman = SalesmanManager.getCurrentSalesman(this);
        displayStoreInActionBar();
    }


    /////////////////////////////////////////////////////////////////////////////////
    //  METHODS FROM SUPERCLASS/INTERFACES                                                        //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onBackPressed() {
        /** Check is back stack > 1 b/c first transaction just loads the landing page. If the
         * back stack is NOT > 1 (i.e. we're at the landing page), the app should never close
         * by the user clicking the back button, as per client's request.*/
        if(mFragmentManager.getBackStackEntryCount() > 1) {
            mFragmentManager.popBackStack();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (resultCode) {

            case UpdateActivity.REQUEST_CODE: {
                final ProductFragment fragment = new ProductFragment();
                switchFragment(fragment, ProductFragment.TAG);
            } break;

            case ShoppingCartDialog.RESULT_CODE_CONTINUED: {
                //Do nothing...
            } break;

            case ShoppingCartDialog.RESULT_CODE_CHECKED_OUT: {
                ShoppingCart.setCurrentOrderId(MainActivity.this, null);
                NewStoreDialog.getInstance().clearAll();
            } break;

            case SellSheetsDialog.RESULT_CODE:
                break;
        }

        switch (requestCode) {

            case OrderHistoryActivity.REQUEST_CODE: {
                if (resultCode == OrderHistoryActivity.RESULT_CODE_LOAD) {
                    mShowBalanceDialog = true;
                }
            }

            case Statement.REQUEST_CODE: {
                ((Spinner) findViewById(R.id.spinner_reports)).setSelection(0);
                reportsOpen = false;
            } break;

            case StoreSelector.REQUEST_CODE: {
                if (resultCode == StoreSelector.RESULT_CODE_JUST_LOOKING) {
                    mShowBalanceDialog = false;
                }
            } break;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        mMenu = menu;

        /** Init actionbar buttons */
        View.OnClickListener actionBarButtonsClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.select_store: {
                        mShowBalanceDialog = true;
                        MixPanelManager.trackButtonClick(MainActivity.this, "Button click: Top menu: Select Store");
                        StoreSelector.open(MainActivity.this);
                    } break;

                    case R.id.shopping_cart: {
                        if(StoreManager.isStoreSelected()) {
                            MixPanelManager.trackButtonClick(MainActivity.this, "Button click: Top menu: Shopping Cart");
                            startActivityForResult(new Intent(MainActivity.this, ShoppingCartDialog.class), ShoppingCartDialog.REQUEST_CODE);
                        } else {
                            MixPanelManager.trackButtonClick(MainActivity.this, "Button click: Top menu: Select Store -NO STORE SELECTED");
                            Toast.makeText(MainActivity.this, "Please select store to continue.", Toast.LENGTH_SHORT).show();
                        }
                    } break;

                    case R.id.upc: {
                        MixPanelManager.trackButtonClick(MainActivity.this, "Button click: Top menu: UPC");
                        new NumberPad(MainActivity.this, new NumberPad.OnNumberSelectedListener() {
                            @Override
                            public void onSelected(final int number) {
                                loadUpcDialog(number);
                            }

                            @Override
                            public void onSelected(QtySelector.ItemType itemType, int qty) {

                            }
                        }, false);
                    } break;
                }
            }
        };

        /** Store select launcher */
        final Button selectStoreButton = (Button) mMenu.findItem(R.id.select_store).getActionView();
        selectStoreButton.setText("Select Store");
        selectStoreButton.setOnClickListener(actionBarButtonsClickListener);

        /** Shopping cart launcher */
        final Button shoppingCartButton = (Button) mMenu.findItem(R.id.shopping_cart).getActionView();
        shoppingCartButton.setText("Shopping Cart");
        shoppingCartButton.setOnClickListener(actionBarButtonsClickListener);

        /** UPC Search launcher */
        final Button upcButton = (Button) mMenu.findItem(R.id.upc).getActionView();
        upcButton.setText("UPC");
        upcButton.setOnClickListener(actionBarButtonsClickListener);

        /** Set version # */
        mMenu.findItem(R.id.version).setTitle("V " + Utilities.getVersionString(MainActivity.this));

        return super.onCreateOptionsMenu(menu);
    }


    /**
     * Loads the upc dialog with the specified upc code (#number)
     *
     * @param number upc code
     */
    private void loadUpcDialog(final int number) {

        final LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(MainActivity.this, Contract.Product.CONTENT_URI, null, Contract.ProductColumns.COLUMN_UPC + " LIKE '%" + number + "%'", null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                final List<Product> products1 = new ArrayList<>();
                while(data.moveToNext()) {

                    Product product = ProviderUtils.cursorToProduct(data);

                    final int length = String.valueOf(number).length();

                    if (length < 7) {
                        final int subStringIndex = product.getUPC().length() - 7;
                        if (product.getUPC().substring(subStringIndex).contains(String.valueOf(number))) {
                            products1.add(product);
                        }
                    } else {
                        if (product.getUPC().contains(String.valueOf(number))) {
                            products1.add(product);
                        }
                    }

                }

                SpecialProductDialog.show(MainActivity.this, "UPC Search", products1);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        };
        getLoaderManager().initLoader(number, null, loaderCallbacks);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final SharedPrefsManager sharedPrefsManager = new SharedPrefsManager(MainActivity.this);

        switch (item.getItemId()) {

            case R.id.daily_update:
                MixPanelManager.trackButtonClick(MainActivity.this, "Button click: Top menu: DAILY UPDATE");
                final Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
                final ArrayList<UpdateActivity.UpdateType> updateTypes = new ArrayList<>(7);
                updateTypes.add(UpdateActivity.UpdateType.APP_INFO);
                updateTypes.add(UpdateActivity.UpdateType.USERS);
                updateTypes.add(UpdateActivity.UpdateType.CUST_SPEC_PRICE_LIST);
                updateTypes.add(UpdateActivity.UpdateType.SIDE_MENU);
                updateTypes.add(UpdateActivity.UpdateType.BRANDS);
                updateTypes.add(UpdateActivity.UpdateType.CATEGORY);
                updateTypes.add(UpdateActivity.UpdateType.SALESMEN_STORES);
                updateTypes.add(UpdateActivity.UpdateType.PRODUCT_INFO);
                intent.putExtra(UpdateActivity.sUpdateTypeKey, updateTypes);
                startActivityForResult(intent, UpdateActivity.REQUEST_CODE);
                return true;

            case R.id.order_history:
                MixPanelManager.trackButtonClick(MainActivity.this, "Button click: Top menu: ORDER HISTORY");
                startActivityForResult(new Intent(MainActivity.this, OrderHistoryActivity.class), OrderHistoryActivity.REQUEST_CODE);
                return true;

            case R.id.sign_out:
                MixPanelManager.trackButtonClick(MainActivity.this, "Button click: Top menu: SIGN OUT");
                if (mGoogleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                }

                sharedPrefsManager.removeLoggedInUser();
                startActivity(new Intent(MainActivity.this, Login.class));
                finish();
                return true;

            case R.id.exit:
                MixPanelManager.trackButtonClick(MainActivity.this, "Button click: Top menu: EXIT APP");
                finish();
                return true;

            case R.id.version:
                MixPanelManager.trackButtonClick(MainActivity.this, "Button click: Top menu: APP VERSION");
                final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setMessage("SugarSync directory:\n\n" + sharedPrefsManager.getSugarSyncDir());
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "CLOSE",
                        new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                return true;

            case R.id.shopping_cart:
                if(StoreManager.isStoreSelected()) {
                    startActivityForResult(new Intent(MainActivity.this, ShoppingCartDialog.class), ShoppingCartDialog.REQUEST_CODE);
                } else {
                    Toast.makeText(MainActivity.this, "Please select store to continue.", Toast.LENGTH_SHORT).show();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //  METHODS                                                                                   //
    ////////////////////////////////////////////////////////////////////////////////////////////////


    private void displayStoreInActionBar() {

        Salesman loggedInUser = null;

        final SharedPrefsManager sharedPrefs = new SharedPrefsManager(this);

        final Cursor userCursor = getContentResolver().query(Contract.User.CONTENT_URI, null, Contract.UserColumns.COLUMN_NAME + " = '" + sharedPrefs.getLoggedInUser() + "'", null, null);
        if (userCursor.moveToFirst()) {
            loggedInUser  = ProviderUtils.CursorToSalesman(userCursor);
        }

        final ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            if (loggedInUser != null) {
                actionBar.setTitle("  Sales Rep: " + loggedInUser.getName());

                final boolean isStoreSelected = StoreManager.isStoreSelected();
                final Store store = StoreManager.getCurrentStore();

                if (isStoreSelected) {
                    actionBar.setSubtitle("   " + store.getName());
                    if (mShowBalanceDialog) {
                        Utilities.showOutstandingBalance(this, Float.parseFloat(store.getOutstadingBalanceDue()), store.getLastCollectDate(), store.getStatementUrl());
                        ((Button)mMenu.findItem(R.id.select_store).getActionView()).setText("Change Store");
                        mShowBalanceDialog = false;
                    }
                } else {
                    actionBar.setSubtitle("   NO STORE SELECTED");
                    if (mMenu != null) {
                        ((Button)mMenu.findItem(R.id.select_store).getActionView()).setText("Select Store");
                    }
                }
            }
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.rect_solid_white));
        }
    }


    /**
     * Swaps fragments in the main content view. Temporarily switches all fragments to
     * FragmentBrandsList.
     * @param position Position of list item.
     */
    private void selectItem(final int position) {
        switch (position) {
            case 0:
                MixPanelManager.trackButtonClick(MainActivity.this, "Button click: Left Menu: Products");
                switchFragment(new ProductFragment(), ProductFragment.TAG);
                break;
            case 1:
                MixPanelManager.trackButtonClick(MainActivity.this, "Button click: Left Menu: Brands");
                switchFragment(BrandFragment.getInstance(), BrandFragment.TAG);
                break;
            case 2:
                MixPanelManager.trackButtonClick(MainActivity.this, "Button click: Left Menu: Categories");
                switchFragment(CategoryFragment.getInstance(), CategoryFragment.TAG);
                break;
            case 3:
                break;
            default:

                final Status status = Status.values()[position - 4];

                final Handler handler = new Handler() {
                    /**
                     * Subclasses must implement this to receive messages.
                     *
                     * @param msg msg
                     */
                    @Override
                    public void handleMessage(Message msg) {
                        ProductFragment productFragment = new ProductFragment();
                        productFragment.setFilter(status);
                        switchFragment(productFragment, ProductFragment.TAG);
                    }
                };

                final LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                        return new CursorLoader(MainActivity.this, Contract.Product.CONTENT_URI, null, Contract.ProductColumns.COLUMN_STATUS_CODE + " = '" + status.name() + "'", null, null);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                        final List<Product> products1 = new ArrayList<>(data.getCount());
                        while(data.moveToNext()) {
                            products1.add(ProviderUtils.cursorToProduct(data));
                        }

//                        if(status.equals(Status.REST) || status.equals(Status.SOAP) || status.equals(Status.LOWQ)) {
                            handler.sendEmptyMessage(1);

//                        } else {
//                            SpecialProductDialog.show(MainActivity.this, status.getDescription(), products1);
//                        }
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> loader) {

                    }
                };

                final LoaderManager loaderManager = getLoaderManager();

                if (loaderManager.getLoader(status.hashCode()) == null) {
                    getLoaderManager().initLoader(status.hashCode(), null, loaderCallbacks);
                } else {
                    getLoaderManager().restartLoader(status.hashCode(), null, loaderCallbacks);
                }

                break;
        }

    }


    /**
     * Helper method to swap fragments into main content frame.
     *
     * @param fragment Fragment to switch to.
     * @param tag Tag of fragment.
     */
    public void switchFragment(final Fragment fragment, final String tag) {
        mFragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }

}