package com.thebedesseegroup.sales.update.json;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.thebedesseegroup.sales.provider.Contract;
import com.thebedesseegroup.sales.provider.ProviderUtils;
import com.thebedesseegroup.sales.salesman.Salesman;
import com.thebedesseegroup.sales.salesman.SalesmanManager;
import com.thebedesseegroup.sales.sharedprefs.SharedPrefsManager;
import com.thebedesseegroup.sales.utilities.ProtectedLog;
import com.thebedesseegroup.sales.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class UpdateUsers extends BaseJsonUpdate {

    OnUpdateUsersCompleteListener mListener;

    private Context mContext;

    public UpdateUsers(Context context) {
        super(context);
        mContext = context;
    }

    public void setOnUpdateUsersCompleteListener(OnUpdateUsersCompleteListener listener) {
        mListener = listener;
    }

    @Override
    public String getFilename() {
        return "app_login.json";
    }


    protected void onPostExecute(Void v) {

        if (result == null) {
            Utilities.longToast(mContext, "There was an error locating the list of valid users. Have you setup Sugar Sync?");
            final SharedPrefsManager sharedPrefsManager = new SharedPrefsManager(mContext);
            sharedPrefsManager.setSugarSyncDir(null);
            if(mContext instanceof Activity) {
                ((Activity)mContext).finish();
            }
        } else {

            mContext.getContentResolver().delete(Contract.User.CONTENT_URI, null, null);

            try {
                JSONArray jArray = new JSONArray(result);

//                final int length = jArray.length();

//                final ContentValues [] contentValues = new ContentValues[length];

                for (int i = 0; i < jArray.length(); i++) {

                    final JSONObject jsonObject = jArray.getJSONObject(i);

                    final Salesman salesman = new Salesman(jsonObject.getString("name"), jsonObject.getString("email"));
                    salesman.setIsAdmin(jsonObject.getString("admin").equals("YES"));

                    final Salesman currSalesman = new SharedPrefsManager(mContext).getCurrentSalesman();
                    if (currSalesman != null && currSalesman.equals(salesman)) {
                        SalesmanManager.setCurrentSalesman(mContext, salesman);
                    }

                    final ContentValues contentValue = ProviderUtils.UserToContentValues(salesman);

                    final Cursor existsCursor = mContext.getContentResolver().query(Contract.User.CONTENT_URI, null, Contract.UserColumns.COLUMN_EMAIL + " = ?", new String[]{salesman.getEmail()}, null);
                    if (existsCursor.moveToFirst()) {
                        mContext.getContentResolver().update(Contract.User.CONTENT_URI, contentValue, Contract.UserColumns.COLUMN_EMAIL + " = ?", new String[]{salesman.getEmail()});
                    } else {
                        mContext.getContentResolver().insert(Contract.User.CONTENT_URI, contentValue);
                    }

                }


                if (mListener != null) {
                    mListener.onComplete();
                }

            } catch(JSONException e) {
                ProtectedLog.e("JSONException", "Error: " + e.toString(), e);
                if (mListener != null) {
                    mListener.onError();
                }
                ((Activity) mContext).finish();
            }
        }

    }

    public interface OnUpdateUsersCompleteListener {
        void onComplete();
        void onError();
    }


}



