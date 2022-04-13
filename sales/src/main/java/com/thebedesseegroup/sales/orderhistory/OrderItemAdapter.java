package com.thebedesseegroup.sales.orderhistory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.thebedesseegroup.sales.R;
import com.thebedesseegroup.sales.provider.Contract;
import com.thebedesseegroup.sales.provider.ProviderUtils;
import com.thebedesseegroup.sales.salesmanstore.SalesmanStore;
import com.thebedesseegroup.sales.shoppingcart.ShoppingCart;
import com.thebedesseegroup.sales.store.Store;
import com.thebedesseegroup.sales.store.StoreManager;
import com.thebedesseegroup.sales.store.SwitchStoreDialog;
import com.thebedesseegroup.sales.utilities.Utilities;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;

/**
 * TODO: Document me...
 */
public class OrderItemAdapter extends BaseAdapter {

    final private Context mContext;
    private ArrayList<SavedOrder> mSavedOrders;

    public OrderItemAdapter(Context context, ArrayList<SavedOrder> savedOrders) {
        mContext = context;
        mSavedOrders = savedOrders;
        Collections.sort(mSavedOrders, null);
    }

    @Override
    public int getCount() {
        return mSavedOrders.size();
    }



    @Override
    public SavedOrder getItem(int position) {
        return mSavedOrders.get(position);
    }



    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final SavedOrder order = getItem(position);

        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_line_item, parent, false);

        if(order.getStartTime() != null) {
            ((TextView) convertView.findViewById(R.id.date)).setText(DateFormat.getDateTimeInstance().format(order.getStartTime()));
        }

        final String storeName = order.getId().split("_")[0];

        ((TextView)convertView.findViewById(R.id.customer)).setText(storeName + "  :  " + order.getNumProducts() + " PRODUCTS");
        convertView.findViewById(R.id.btn_load_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog alertDialog = SwitchStoreDialog.build(mContext,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                final ShoppingCart shoppingCart = ShoppingCart.getSavedOrder(mContext, order.getId());
                                ShoppingCart.setCurrentShoppingCart(shoppingCart);

                                final Cursor cursor = mContext.getContentResolver().query(Contract.SalesmanStore.CONTENT_URI, null, Contract.SalesmanStoreColumns.COLUMN_CUST_NAME + " = '" + storeName + "'", null, null);

                                if (cursor.moveToFirst()) {
                                    final SalesmanStore salesmanStore = ProviderUtils.cursorToSalesmanStore(cursor);

                                    final Store store = salesmanStore.getStore();

                                    StoreManager.setCurrentStore(store);

                                    Utilities.showOutstandingBalance((Activity)mContext, Float.parseFloat(store.getOutstadingBalanceDue()), store.getLastCollectDate(), store.getStatementUrl());
                                }

                                ((Activity)mContext).setResult(OrderHistoryActivity.RESULT_CODE_LOAD);
                                ((Activity)mContext).finish();
                            }
                        });
                alertDialog.show();

            }
        });

        return convertView;
    }


    private class ViewHolder {
        public TextView date;
        public TextView customer;
        public TextView status;
        public Button load;
    }
}
