package com.thebedesseegroup.sales.store;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.thebedesseegroup.sales.R;
import com.thebedesseegroup.sales.main.StoreSelectorAdapter2;
import com.thebedesseegroup.sales.orderhistory.SavedOrder;
import com.thebedesseegroup.sales.provider.Contract;
import com.thebedesseegroup.sales.provider.ProviderUtils;
import com.thebedesseegroup.sales.salesman.Salesman;
import com.thebedesseegroup.sales.salesman.SalesmanManager;
import com.thebedesseegroup.sales.salesmanstore.SalesmanStore;
import com.thebedesseegroup.sales.shoppingcart.ShoppingCart;
import com.thebedesseegroup.sales.utilities.Utilities;

import java.util.Date;

/**
 * Store selector activity
 */
public class StoreSelector extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    final public static int REQUEST_CODE = 2993;
    final public static int RESULT_CODE_JUST_LOOKING = 2911;

    private StoreSelectorAdapter2 mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_selector);

        final ListView listView = (ListView) findViewById(R.id.list);

        mAdapter = new StoreSelectorAdapter2(this);

        getLoaderManager().initLoader(9312, null, this);

        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new OnStoreSelectedListener());
    }


    @Override
    protected void onResume() {
        super.onResume();

        int[] dimens = Utilities.getScreenDimensInPx(null);
        getWindow().setLayout((int) (dimens[0] * .5), (int) (dimens[1] * .95));
    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        final Salesman salesman = SalesmanManager.getCurrentSalesman(this);

        String salesmanName = "";

        if (salesman == null) {

            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("WARNING! - NO STORES FOUND");
            alertDialog.setMessage("To continue, please perform the following 2 steps:\n\n   1) Run DAILY UPDATE\n\n   2) Sign out, and sign in again");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

        }  else {
            salesmanName = salesman.getName();
        }

        return new CursorLoader(this, Contract.SalesmanStore.CONTENT_URI, null, Contract.SalesmanStoreColumns.COLUMN_SALESPERSON + " = '" + salesmanName + "'", null, Contract.SalesmanStoreColumns.COLUMN_CUST_NAME + " ASC");
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }



    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    public static void open(final Activity activity) {
        activity.startActivityForResult(new Intent(activity, StoreSelector.class), REQUEST_CODE);
    }


    private void showNewStoreDialog(final String storeName, final Store store) {

        final AlertDialog alertDialog = SwitchStoreDialog.build(this,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        ShoppingCart.getCurrentShoppingCart().clear();

                        final Date date = new Date();

                        final String savedOrderId = Utilities.getSavedOrderId(StoreSelector.this, storeName, date);

                        final SavedOrder savedOrder = new SavedOrder(savedOrderId, date, null, false, 0);

                        final ContentValues values = ProviderUtils.savedOrderToContentValues(savedOrder);

                        getContentResolver().insert(Contract.SavedOrder.CONTENT_URI, values);

                        ShoppingCart.setCurrentOrderId(StoreSelector.this, savedOrderId);

                        StoreManager.setCurrentStore(store);

                        dialog.dismiss();

                        finish();
                    }
                });
        alertDialog.show();

    }




    private class OnStoreSelectedListener implements AdapterView.OnItemClickListener {


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final TextView storeNameTxt = (TextView)view.findViewById(R.id.storename);

            if(storeNameTxt.getText().toString().contains("0000") && !NewStoreDialog.getInstance().isAnyDataEntered()) {
                NewStoreDialog.getInstance().setCurrentCustomer();
                NewStoreDialog.getInstance().show(StoreSelector.this);

            } else {
                final String storeName = storeNameTxt.getText().toString();

                final Cursor cursor = getContentResolver().query(Contract.SalesmanStore.CONTENT_URI, null, Contract.SalesmanStoreColumns.COLUMN_CUST_NAME + " = '" + storeName + "'", null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    final SalesmanStore salesmanStore = ProviderUtils.cursorToSalesmanStore(cursor);

                    final Store store = salesmanStore.getStore();

                    if (store != null) {

                        if (!store.isShowPopup()) {
                            StoreManager.setCurrentStore(store);
                            final Intent intent = new Intent();
                            intent.putExtra("justLooking", true);
                            setResult(RESULT_CODE_JUST_LOOKING, intent);
                            finish();
                        }
                        /** Check if store has changed, otherwise ignore */
                        else if (!store.equals(StoreManager.getCurrentStore())) {
                            showNewStoreDialog(storeName, store);
                        }
                    }
                    cursor.close();
                }
            }
            ShoppingCart.getCurrentShoppingCart().startTimer();
        }

    }
}