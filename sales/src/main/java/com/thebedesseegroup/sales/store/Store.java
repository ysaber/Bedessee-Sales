package com.thebedesseegroup.sales.store;

import android.text.TextUtils;

/**
 * Created by yusufsaber on 2014-04-25.
 */
public class Store {

    private String mName;
    private String mNumber;
    private String mAddress;
    private String mLastCollectDaysOld;
    private String mLastCollectDate;
    private String mLastCollectInvoice;
    private String mLastCollectAmount;
    private String mOutstadingBalanceDue;
    private String mStatementUrl;
    private boolean mShowPopup;


    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String number) {
        mNumber = number;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getLastCollectDaysOld() {
        return mLastCollectDaysOld;
    }

    public void setLastCollectDaysOld(String lastCollectDaysOld) {
        mLastCollectDaysOld = lastCollectDaysOld;
    }

    public String getLastCollectDate() {
        return mLastCollectDate;
    }

    public void setLastCollectDate(String lastCollectDate) {
        mLastCollectDate = lastCollectDate;
    }

    public String getLastCollectInvoice() {
        return mLastCollectInvoice;
    }

    public void setLastCollectInvoice(String lastCollectInvoice) {
        mLastCollectInvoice = lastCollectInvoice;
    }

    public String getLastCollectAmount() {
        return mLastCollectAmount;
    }

    public void setLastCollectAmount(String lastCollectAmount) {
        mLastCollectAmount = lastCollectAmount;
    }

    public String getOutstadingBalanceDue() {
        return TextUtils.isEmpty(mOutstadingBalanceDue) ? "0" : mOutstadingBalanceDue;
    }

    public void setOutstadingBalanceDue(String outstadingBalanceDue) {
        mOutstadingBalanceDue = outstadingBalanceDue;
    }

    public String getStatementUrl() {
        return mStatementUrl;
    }

    public void setStatementUrl(String statementUrl) {
        mStatementUrl = statementUrl;
    }

    public boolean isShowPopup() {
        return mShowPopup;
    }

    public void setShowPopup(boolean showPopup) {
        mShowPopup = showPopup;
    }

    @Override
    public String toString() {
        return "Store{" +
                "mName='" + mName + '\'' +
                ", mNumber='" + mNumber + '\'' +
                ", mAddress='" + mAddress + '\'' +
                ", mLastCollectDaysOld='" + mLastCollectDaysOld + '\'' +
                ", mLastCollectDate='" + mLastCollectDate + '\'' +
                ", mLastCollectInvoice='" + mLastCollectInvoice + '\'' +
                ", mLastCollectAmount='" + mLastCollectAmount + '\'' +
                ", mOutstadingBalanceDue='" + mOutstadingBalanceDue + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Store)) return false;

        Store store = (Store) o;

        return !(mNumber != null ? !mNumber.equals(store.mNumber) : store.mNumber != null);

    }

    @Override
    public int hashCode() {
        return mNumber != null ? mNumber.hashCode() : 0;
    }
}
