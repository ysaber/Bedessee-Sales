package com.thebedesseegroup.sales.main;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.thebedesseegroup.sales.R;
import com.thebedesseegroup.sales.provider.ProviderUtils;
import com.thebedesseegroup.sales.store.Store;

/**
 * TODO: Document me...
 */
public class StoreSelectorAdapter2 extends CursorAdapter {


    public StoreSelectorAdapter2(Context context) {
        super(context, null, false);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.store_selector_item, parent, false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Store store = ProviderUtils.cursorToSalesmanStore(cursor).getStore();

        ((TextView)view.findViewById(R.id.storename)).setText(store.getName());
        ((TextView)view.findViewById(R.id.storeaddress)).setText(store.getAddress());
    }

    @Override
    public Store getItem(int position) {

        final Cursor cursor = getCursor();
        cursor.moveToPosition(position);

        return ProviderUtils.cursorToSalesmanStore(cursor).getStore();
    }
}