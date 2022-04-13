package com.thebedesseegroup.sales.update.json;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.google.gson.Gson;
import com.thebedesseegroup.sales.main.SideMenu;
import com.thebedesseegroup.sales.provider.Contract;
import com.thebedesseegroup.sales.provider.ProviderUtils;
import com.thebedesseegroup.sales.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * AsyncTask used to update all Salesman and Store data.x
 */
public class UpdateSideMenu extends BaseJsonUpdate {

    private Context mContext;
    private Gson mGson;

    public UpdateSideMenu(Context context) {
        super(context);
        mContext = context;
        mGson = new Gson();
    }

    @Override
    public String getFilename() {
        return "product_status_side_menu.json";
    }


    @Override
    protected void onPostExecute(Void v) {
        if(result != null) {

            final ContentResolver contentResolver = mContext.getContentResolver();

            contentResolver.delete(Contract.SideMenu.CONTENT_URI, null, null);


            try {

                mContext.getContentResolver().delete(Contract.SideMenu.CONTENT_URI, null, null);

                final JSONArray jArray = new JSONArray(result);

                final int length = jArray.length();

                final ContentValues [] contentValues = new ContentValues[length];

                for (int i = 0; i < length; i++) {

                    final JSONObject jsonObject = jArray.getJSONObject(i);

                    final SideMenu sideMenu = mGson.fromJson(jsonObject.toString(), SideMenu.class);

                    contentValues[i] = ProviderUtils.sideMenuToContentValues(sideMenu);
                }

                contentResolver.bulkInsert(Contract.SideMenu.CONTENT_URI, contentValues);

                mListener.onComplete();

            } catch (JSONException e) {
                Utilities.longToast(mContext, "SIDE MENU JSON FILE ERROR");
                ((Activity) mContext).finish();
            }
        }

    }

}
