package com.thebedesseegroup.sales.orderhistory;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.thebedesseegroup.sales.R;
import com.thebedesseegroup.sales.provider.Contract;
import com.thebedesseegroup.sales.provider.ProviderUtils;
import com.thebedesseegroup.sales.utilities.Utilities;

import java.util.ArrayList;

/**
 * Activity that shows a list of the user's saved orders
 */
public class OrderHistoryActivity extends Activity {

    final public static int REQUEST_CODE = 3803;
    final public static int RESULT_CODE_LOAD = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_orderhistory);

        new LoadOrders().execute();

        int[] dimens = Utilities.getScreenDimensInPx(null);

        getWindow().setLayout((int) (dimens[0] * .95), (int) (dimens[1] * .95));
    }




    private class LoadOrders extends AsyncTask<Void, Void, OrderItemAdapter> {


        @Override
        protected OrderItemAdapter doInBackground(Void... params) {

            final Cursor cursor = getContentResolver().query(Contract.SavedOrder.CONTENT_URI, null, null, null, null);

            final ArrayList<SavedOrder> orders = new ArrayList<>();

            while (cursor.moveToNext()) {
                final SavedOrder order = ProviderUtils.cursorToSavedOrder(cursor);
                if (order != null) {
                    if (order.getNumProducts() > 0) {
                        orders.add(order);
                    }
                }
            }

            cursor.close();

            return new OrderItemAdapter(OrderHistoryActivity.this, orders);
        }


        @Override
        protected void onPostExecute(OrderItemAdapter adapter) {
            super.onPostExecute(adapter);

            final ListView listView = (ListView) findViewById(R.id.list);
            listView.setAdapter(adapter);

            findViewById(R.id.progress_bar).setVisibility(View.GONE);
            findViewById(R.id.list).setVisibility(View.VISIBLE);
        }

    }



}
