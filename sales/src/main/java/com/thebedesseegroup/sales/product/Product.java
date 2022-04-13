package com.thebedesseegroup.sales.product;

import com.google.gson.annotations.SerializedName;
import com.thebedesseegroup.sales.product.category.Category;

import java.io.Serializable;

public class Product implements Serializable, Comparable<Product> {


    @SerializedName("PROD#")
    private String mNumber;

    @SerializedName("BRAND")
    private String mBrand;

    @SerializedName("DESCRIP")
    private String mDescription;

    @SerializedName("UOM")
    private String mPieceUom;

    @SerializedName("UNIT PRICE")
    private String mPiecePrice;

    @SerializedName("CASE SIZE")
    private String mCaseUom;

    @SerializedName("PRICE")
    private String mCasePrice;

    @SerializedName("LEVEL1PRICE")
    private String mLevel1Price;

    @SerializedName("LEVEL2PRICE")
    private String mLevel2Price;

    @SerializedName("LEVEL3PRICE")
    private String mLevel3Price;

    @SerializedName("STATUS CODE")
    private String mStatusCode;

    @SerializedName("STATUS DESCRIPTION")
    private String mStatusDescription;

    @SerializedName("CASES PER SKID")
    private String mCasesPerSkid;

    @SerializedName("CASES PER ROW")
    private String mCasesPerRow;

    @SerializedName("LAYERS PER SKID")
    private String mLayersPerSkid;


    private String mImagePath;

    @SerializedName("TOTAL QTY")
    private String mTotalQty;

    @SerializedName("UPC")
    private String mUPC;

    @SerializedName("QTY1")
    private String mQty1;

    @SerializedName("QTY2")
    private String mQty2;

    @SerializedName("QTY3")
    private String mQty3;

    @SerializedName("QTY4")
    private String mQty4;

    @SerializedName("SHOW_QTY1")
    private String mShowQty1;

    @SerializedName("SHOW_QTY2")
    private String mShowQty2;

    @SerializedName("SHOW_QTY3")
    private String mShowQty3;

    @SerializedName("SHOW_QTY4")
    private String mShowQty4;

    @SerializedName("NOTE01")
    private String mNote01;

    @SerializedName("NOTE02")
    private String mNote02;

    @SerializedName("NOTE03")
    private String mNote03;

    @SerializedName("NOTE04")
    private String mNote04;

    @SerializedName("NOTE05")
    private String mNote05;

    @SerializedName("POP UP PRICE")
    private String mPopUpPrice;

    @SerializedName("POP PRICE FLAG")
    private String mPopUpPriceFlag;

    @SerializedName("LIKE TAG")
    private String mLikeTag;

    public String getTotalQty() {
        return mTotalQty;
    }

    public void setTotalQty(String totalQty) {
        mTotalQty = totalQty;
    }

    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String number) {
        mNumber = number;
    }

    public String getBrand() {
        return mBrand;
    }

    public void setBrand(String brand) {
        mBrand = brand;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getPieceUom() { return mPieceUom; }

    public void setPieceUom(String pieceUom) { mPieceUom = pieceUom; }

    public String getImagePath() { return mImagePath; }

    public void setImagePath(String imagePath) { mImagePath = imagePath; }

    public String getCasePrice() { return mCasePrice; }

    public void setCasePrice(String casePrice) { mCasePrice = casePrice; }

    public String getLevel1Price() { return mLevel1Price; }

    public void setLevel1Price(String level1Price) { mLevel1Price = level1Price; }

    public String getLevel2Price() { return mLevel2Price; }

    public void setLevel2Price(String level2Price) { mLevel2Price = level2Price; }

    public String getLevel3Price() { return mLevel3Price; }

    public void setLevel3Price(String level3Price) { mLevel3Price = level3Price; }

    public String getStatusCode() {
        return mStatusCode;
    }

    public void setStatusCode(String statusCode) {
        mStatusCode = statusCode;
    }

    public String getStatusDescription() {
        return mStatusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        mStatusDescription = statusDescription;
    }

    public String getCasesPerSkid() {
        return mCasesPerSkid;
    }

    public void setCasesPerSkid(String casesPerSkid) {
        mCasesPerSkid = casesPerSkid;
    }

    public String getCasesPerRow() {
        return mCasesPerRow;
    }

    public void setCasesPerRow(String casesPerRow) {
        mCasesPerRow = casesPerRow;
    }

    public String getLayersPerSkid() {
        return mLayersPerSkid;
    }

    public void setLayersPerSkid(String layersPerSkid) {
        mLayersPerSkid = layersPerSkid;
    }

    public Category getCategory() {
        return Category.getCategoryFromChar(mNumber.charAt(0));
    }

    public String getPiecePrice() {
        return mPiecePrice;
    }

    public void setPiecePrice(String piecePrice) {
        mPiecePrice = piecePrice;
    }

    public String getCaseUom() {
        return mCaseUom;
    }

    public void setCaseUom(String caseUom) {
        mCaseUom = caseUom;
    }

    public String getQty1() {
        return mQty1;
    }

    public void setQty1(String qty1) {
        mQty1 = qty1;
    }

    public String getQty2() {
        return mQty2;
    }

    public void setQty2(String qty2) {
        mQty2 = qty2;
    }

    public String getQty3() {
        return mQty3;
    }

    public void setQty3(String qty3) {
        mQty3 = qty3;
    }

    public void setQty4(String qty4) {
        mQty4 = qty4;
    }

    public String getQty4() {
        return mQty4;
    }

    public String getShowQty1() {
        return mShowQty1;
    }

    public void setShowQty1(String showQty1) {
        mShowQty1 = showQty1;
    }

    public String getShowQty2() {
        return mShowQty2;
    }

    public void setShowQty2(String showQty2) {
        mShowQty2 = showQty2;
    }

    public String getShowQty3() {
        return mShowQty3;
    }

    public void setShowQty3(String showQty3) {
        mShowQty3 = showQty3;
    }

    public String getShowQty4() {
        return mShowQty4;
    }

    public void setShowQty4(String showQty4) {
        mShowQty4 = showQty4;
    }

    public String getNote01() {
        return mNote01;
    }

    public void setNote01(String note01) {
        mNote01 = note01;
    }

    public String getNote02() {
        return mNote02;
    }

    public void setNote02(String note02) {
        mNote02 = note02;
    }

    public String getNote03() {
        return mNote03;
    }

    public void setNote03(String note03) {
        mNote03 = note03;
    }

    public String getNote04() {
        return mNote04;
    }

    public void setNote04(String note04) {
        mNote04 = note04;
    }

    public String getNote05() {
        return mNote05;
    }

    public void setNote05(String note05) {
        mNote05 = note05;
    }

    public String getPopUpPrice() {
        return mPopUpPrice;
    }

    public void setPopUpPrice(String popUpPrice) {
        mPopUpPrice = popUpPrice;
    }

    public String getPopUpPriceFlag() {
        return mPopUpPriceFlag;
    }

    public void setPopUpPriceFlag(String popUpPriceFlag) {
        mPopUpPriceFlag = popUpPriceFlag;
    }

    public String getLikeTag() {
        return mLikeTag;
    }

    public void setLikeTag(String likeTag) {
        mLikeTag = likeTag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;

        Product product = (Product) o;

        return mNumber.equals(product.mNumber);

    }

    @Override
    public int hashCode() {
        return mNumber.hashCode();
    }

    public String getUPC() {
        return mUPC;
    }

    public void setUPC(String UPC) {
        mUPC = UPC;
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
    public int compareTo(Product another) {
        return mNumber.compareTo(another.getNumber());
    }
}