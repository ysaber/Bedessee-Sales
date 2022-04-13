package com.thebedesseegroup.sales.product.brand;

import com.google.gson.annotations.SerializedName;

/**
 * Brand model.
 */
public class Brand {

    @SerializedName("BRAND")
    private String mName;

    @SerializedName("# of PRODUCTS FOR BRAND")
    private int mNumProducts;

    @SerializedName("FILENAME")
    private String mLogoName;

    public Brand(String name, int numProducts, String logoName) {
        mName = name;
        mNumProducts = numProducts;
        mLogoName = logoName;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getNumProducts() {
        return mNumProducts;
    }

    public void setNumProducts(int numProducts) {
        mNumProducts = numProducts;
    }

    public String getLogoName() {
        return mLogoName;
    }

    public void setLogoName(String logoName) {
        mLogoName = logoName;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "mLogoName='" + mLogoName + '\'' +
                ", mNumProducts=" + mNumProducts +
                ", mName='" + mName + '\'' +
                '}';
    }
}
