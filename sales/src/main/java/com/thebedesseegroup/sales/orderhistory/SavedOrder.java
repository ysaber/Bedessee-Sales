package com.thebedesseegroup.sales.orderhistory;

import android.support.annotation.NonNull;

import java.util.Date;

/**
 * TODO: Document me...
 */
public class SavedOrder implements Comparable {

    private String mId;

    private Date mStartTime;

    private Date mEndTime;

    private boolean mIsClosed;

    private int mNumProducts;

    public SavedOrder(String id, Date startTime, Date endTime, boolean isClosed, int numProducts) {
        mId = id;
        mStartTime = startTime;
        mEndTime = endTime;
        mIsClosed = isClosed;
        mNumProducts = numProducts;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public Date getStartTime() {
        return mStartTime;
    }

    public void setStartTime(Date startTime) {
        mStartTime = startTime;
    }

    public Date getEndTime() {
        return mEndTime;
    }

    public void setEndTime(Date endTime) {
        mEndTime = endTime;
    }

    public boolean isClosed() {
        return mIsClosed;
    }

    public void setClosed(boolean isClosed) {
        mIsClosed = isClosed;
    }


    public int getNumProducts() {
        return mNumProducts;
    }

    public void setNumProducts(int numProducts) {
        mNumProducts = numProducts;
    }

    /**
     * Compares this object to the specified object to determine their relative
     * order.
     *
     * @param another the object to compare to this instance.
     * @return a negative integer if this instance is less than {@code another};
     * a positive integer if this instance is greater than
     * {@code another}; 0 if this instance has the same order as
     * {@code another}.
     * @throws ClassCastException if {@code another} cannot be converted into something
     *                            comparable to {@code this} instance.
     */
    @Override
    public int compareTo(@NonNull Object another) {

        final SavedOrder f = (SavedOrder)another;

        final Date fSartTime = f.getStartTime();

        if(mStartTime != null && fSartTime != null) {
            return fSartTime.compareTo(mStartTime);
        } else {
            return -1;
        }
    }
}
