package com.thebedesseegroup.sales.store;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.thebedesseegroup.sales.R;
import com.thebedesseegroup.sales.orderhistory.SavedOrder;
import com.thebedesseegroup.sales.provider.Contract;
import com.thebedesseegroup.sales.provider.ProviderUtils;
import com.thebedesseegroup.sales.shoppingcart.ShoppingCart;
import com.thebedesseegroup.sales.utilities.Utilities;

import java.util.Date;

/**
 * Dialog to allow salesman to add a new customer. When this form is filled, the data is saved
 * in static instance variables and then used if the salesman then creates an order email using
 * the data provided.
 */
public class NewStoreDialog {

    private static NewStoreDialog instance = new NewStoreDialog();

    private String mContactName;
    private String mCompanyName;
    private String mAddress;
    private String mCity;
    private String mProvince;
    private String mCountry;
    private String mPostalCode;
    private String mTelephone;
    private String mEmail;
    private boolean mIsNewCustomer;

    public static NewStoreDialog getInstance() {
        return instance;
    }

    public void show(final Context context) {

        @SuppressLint("InflateParams")
        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_new_customer, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("New Customer")
                .setCancelable(false)
                .setView(view);

        final AlertDialog alert = builder.create();

        ((EditText)view.findViewById(R.id.edt_telephone)).addTextChangedListener(new PhoneNumberFormattingTextWatcher());


        view.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    mIsNewCustomer = true;
                    mContactName = ((EditText) view.findViewById(R.id.edt_contact_name)).getText().toString();
                    mCompanyName = ((EditText) view.findViewById(R.id.edt_company_name)).getText().toString();
                    mAddress = ((EditText) view.findViewById(R.id.edt_address)).getText().toString();
                    mCity = ((EditText) view.findViewById(R.id.edt_city)).getText().toString();
                    mProvince = ((EditText) view.findViewById(R.id.edt_province)).getText().toString();
                    mCountry = ((EditText) view.findViewById(R.id.edt_country)).getText().toString();
                    mPostalCode = ((EditText) view.findViewById(R.id.edt_postal_cde)).getText().toString();
                    mTelephone = ((EditText) view.findViewById(R.id.edt_telephone)).getText().toString();
                    mEmail = ((EditText) view.findViewById(R.id.edt_email)).getText().toString();

                if(areAllFieldsComplete()) {
                    final Store store = new Store();
                    store.setName(mCompanyName);
                    store.setAddress(mAddress);
                    store.setNumber("0000");

                    StoreManager.setCurrentStore(store);

                    final Date date = new Date();

                    final String savedOrderId = Utilities.getSavedOrderId(context, mCompanyName, new Date());

                    final SavedOrder savedOrder = new SavedOrder(savedOrderId, date, null, false, 0);

                    final ContentValues values = ProviderUtils.savedOrderToContentValues(savedOrder);

                    context.getContentResolver().insert(Contract.SavedOrder.CONTENT_URI, values);

                    ShoppingCart.setCurrentOrderId(context, savedOrderId);

                    alert.dismiss();

                    if (context instanceof Activity) {
                        ((Activity) context).finish();
                    }

                } else {
                    Utilities.shortToast(context, "Please complete all fields to continue.");
                }
            }
        });


        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        alert.show();
    }


    public String getContactName() {
        return mContactName;
    }

    public String getCompanyName() {
        return mCompanyName;
    }

    public String getAddress() {
        return mAddress;
    }

    public String getCity() {
        return mCity;
    }

    public String getProvince() {
        return mProvince;
    }

    public String getCountry() {
        return mCountry;
    }

    public String getPostalCode() {
        return mPostalCode;
    }

    public String getTelephone() {
        return mTelephone;
    }

    public String getEmail() {
        return mEmail;
    }


    public boolean isNewCustomer() {
        return mIsNewCustomer;
    }

    public void setCurrentCustomer() {
        mIsNewCustomer = false;
    }

    public boolean isAnyDataEntered() {
        return mContactName != null || mCompanyName != null || mAddress != null || mCity != null || mProvince != null ||
                mCountry != null || mPostalCode != null || mTelephone != null || mEmail != null;
    }

    public void clearAll() {
        mContactName = null;
        mCompanyName = null;
        mAddress = null;
        mCity = null;
        mProvince = null;
        mCountry = null;
        mPostalCode = null;
        mTelephone = null;
        mEmail = null;
        mIsNewCustomer = false;
    }


    public boolean areAllFieldsComplete() {
        return !TextUtils.isEmpty(mContactName)
            && !TextUtils.isEmpty(mCompanyName)
            && !TextUtils.isEmpty(mAddress)
            && !TextUtils.isEmpty(mCity)
            && !TextUtils.isEmpty(mProvince)
            && !TextUtils.isEmpty(mCountry)
            && !TextUtils.isEmpty(mPostalCode)
            && !TextUtils.isEmpty(mTelephone)
            && !TextUtils.isEmpty(mEmail);
    }

}
