package com.thebedesseegroup.sales.admin;

import com.google.gson.annotations.SerializedName;

/**
 * TODO: Document me...
 */
public class AppInfo {

    @SerializedName("SHORT NAME                        ")
    private String mShortName;

    @SerializedName("APP DESCRIPTION                   ")
    private String mAppDescription;

    @SerializedName("APP NAME                          ")
    private String mAppName;

    @SerializedName("LINK TO PRODUCT IMAGES            ")
    private String mProductImagesLink;

    @SerializedName("LINK TO BRAND LOGO IMAGES         ")
    private String mBrandLogoImagesLink;

    @SerializedName("LINK TO SALES SHEET IMAGES        ")
    private String mSalesSheetImagesLink;

    @SerializedName("LINK TO CUST STATEMENT  TXT FILES ")
    private String mCustStatementTxtFilesLink;

    @SerializedName("LINK TO CUST STMT SMALL TXT FILES ")
    private String mCustStmtSmallTxtFilesLink;

    @SerializedName("LINK TO CUST SALES SATS TXT FILES ")
    private String mCustSalesStatsTxtFilesLink;

    @SerializedName("LINK TO CUSTOMER ACCOUNTS    JSON ")
    private String mCustomerAccountsJsonLink;

    @SerializedName("LINK TO PRODUCT INFO         JSON ")
    private String mProductInfoJsonLink;

    @SerializedName("LINK TO BRANDS INFO          JSON ")
    private String mBrandsInfoJsonLink;

    @SerializedName("ORDER RECEIVED EMAIL ADDRESS      ")
    private String mOrderReceivedEmailAddress;

    @SerializedName("APP CURRENT VERSION               ")
    private String mAppCurrentVersion;

    @SerializedName("VERSION NOTES                     ")
    private String mVersionNotesFile;

    @SerializedName("THIS FILE CREATED                 ")
    private String mThisFileCreated;

    @SerializedName("STATEMENT OF ACCT BODY LINE MSG   ")
    private String mStatementEmailSubject;

    @SerializedName("USE PRODUCT LIKE MATCH LOGIC      ")
    private String mUseProductLikeLogic;

    @SerializedName("FADE PERCENT FOR PRODUCT POP UP   ")
    private String mFadePercentage;

    @SerializedName("IMAGE WORKING FOLDER              ")
    private String mImageWorkingFolder;

    @SerializedName("TOTAL COUNT SLS JSON FILE         ")
    private int mTotalCountSlsJsonFile;

    @SerializedName("TOTAL COUNT CAT JSON FILE         ")
    private int mTotalCountCategoriesJsonFile;

    @SerializedName("TOTAL COUNT PROD JSON FILE        ")
    private int mTotalCountProductsJsonFile;

    @SerializedName("TOTAL COUNT BRAND JSON FILE       ")
    private int mTotalCountBrandsJsonFile;

    @SerializedName("TOTAL COUNT SIDE PANEL JSON FILE  ")
    private int mTotalCountSidePanelJsonFile;

    @SerializedName("PRV                               ")
    private int mAdminCode;

    @SerializedName("APP APK FOLDER AND FILE NAME      ")
    private String mAppApkFilePath;




    public String getAppCurrentVersion() {
        return mAppCurrentVersion;
    }

    public void setAppCurrentVersion(String appCurrentVersion) {
        mAppCurrentVersion = appCurrentVersion;
    }

    public String getAppDescription() {
        return mAppDescription;
    }

    public void setAppDescription(String appDescription) {
        mAppDescription = appDescription;
    }

    public String getAppName() {
        return mAppName;
    }

    public void setAppName(String appName) {
        mAppName = appName;
    }

    public String getBrandLogoImagesLink() {
        return mBrandLogoImagesLink;
    }

    public void setBrandLogoImagesLink(String brandLogoImagesLink) {
        mBrandLogoImagesLink = brandLogoImagesLink;
    }

    public String getBrandsInfoJsonLink() {
        return mBrandsInfoJsonLink;
    }

    public void setBrandsInfoJsonLink(String brandsInfoJsonLink) {
        mBrandsInfoJsonLink = brandsInfoJsonLink;
    }

    public String getCustomerAccountsJsonLink() {
        return mCustomerAccountsJsonLink;
    }

    public void setCustomerAccountsJsonLink(String customerAccountsJsonLink) {
        mCustomerAccountsJsonLink = customerAccountsJsonLink;
    }

    public String getCustSalesStatsTxtFilesLink() {
        return mCustSalesStatsTxtFilesLink;
    }

    public void setCustSalesStatsTxtFilesLink(String custSalesStatsTxtFilesLink) {
        mCustSalesStatsTxtFilesLink = custSalesStatsTxtFilesLink;
    }

    public String getCustStatementTxtFilesLink() {
        return mCustStatementTxtFilesLink;
    }

    public void setCustStatementTxtFilesLink(String custStatementTxtFilesLink) {
        mCustStatementTxtFilesLink = custStatementTxtFilesLink;
    }

    public String getCustStmtSmallTxtFilesLink() {
        return mCustStmtSmallTxtFilesLink;
    }

    public void setCustStmtSmallTxtFilesLink(String custStmtSmallTxtFilesLink) {
        mCustStmtSmallTxtFilesLink = custStmtSmallTxtFilesLink;
    }

    public String getOrderReceivedEmailAddress() {
        return mOrderReceivedEmailAddress;
    }

    public void setOrderReceivedEmailAddress(String orderReceivedEmailAddress) {
        mOrderReceivedEmailAddress = orderReceivedEmailAddress;
    }

    public String getProductImagesLink() {
        return mProductImagesLink;
    }

    public void setProductImagesLink(String productImagesLink) {
        mProductImagesLink = productImagesLink;
    }

    public String getProductInfoJsonLink() {
        return mProductInfoJsonLink;
    }

    public void setProductInfoJsonLink(String productInfoJsonLink) {
        mProductInfoJsonLink = productInfoJsonLink;
    }

    public String getSalesSheetImagesLink() {
        return mSalesSheetImagesLink;
    }

    public void setSalesSheetImagesLink(String salesSheetImagesLink) {
        mSalesSheetImagesLink = salesSheetImagesLink;
    }

    public String getShortName() {
        return mShortName;
    }

    public void setShortName(String shortName) {
        mShortName = shortName;
    }

    public String getThisFileCreated() {
        return mThisFileCreated;
    }

    public void setThisFileCreated(String thisFileCreated) {
        mThisFileCreated = thisFileCreated;
    }

    public String getVersionNotesFile() {
        return mVersionNotesFile;
    }

    public void setVersionNotesFile(String versionNotesFile) {
        mVersionNotesFile = versionNotesFile;
    }

    public String getUseProductLikeLogic() {
        return mUseProductLikeLogic;
    }

    public void setUseProductLikeLogic(String useProductLikeLogic) {
        mUseProductLikeLogic = useProductLikeLogic;
    }

    public String getStatementEmailSubject() {
        return mStatementEmailSubject;
    }

    public void setStatementEmailSubject(String statementEmailSubject) {
        mStatementEmailSubject = statementEmailSubject;
    }

    public String getFadePercentage() {
        return mFadePercentage;
    }

    public void setFadePercentage(String fadePercentage) {
        mFadePercentage = fadePercentage;
    }


    public int getAdminPin() {
        return mAdminCode;
    }

    public void setAdminCode(int adminCode) {
        mAdminCode = adminCode;
    }

    public String getAppApkFilePath() {
        return mAppApkFilePath;
    }

    public void setAppApkFilePath(String appApkFilePath) {
        mAppApkFilePath = appApkFilePath;
    }

    public String getImageWorkingFolder() {
        return mImageWorkingFolder;
    }

    public void setImageWorkingFolder(String imageWorkingFolder) {
        mImageWorkingFolder = imageWorkingFolder;
    }

    public int getTotalCountBrandsJsonFile() {
        return mTotalCountBrandsJsonFile;
    }

    public void setTotalCountBrandsJsonFile(int totalCountBrandsJsonFile) {
        mTotalCountBrandsJsonFile = totalCountBrandsJsonFile;
    }

    public int getTotalCountCategoriesJsonFile() {
        return mTotalCountCategoriesJsonFile;
    }

    public void setTotalCountCategoriesJsonFile(int totalCountCategoriesJsonFile) {
        mTotalCountCategoriesJsonFile = totalCountCategoriesJsonFile;
    }

    public int getTotalCountProductsJsonFile() {
        return mTotalCountProductsJsonFile;
    }

    public void setTotalCountProductsJsonFile(int totalCountProductsJsonFile) {
        mTotalCountProductsJsonFile = totalCountProductsJsonFile;
    }

    public int getTotalCountSidePanelJsonFile() {
        return mTotalCountSidePanelJsonFile;
    }

    public void setTotalCountSidePanelJsonFile(int totalCountSidePanelJsonFile) {
        mTotalCountSidePanelJsonFile = totalCountSidePanelJsonFile;
    }

    public int getTotalCountSlsJsonFile() {
        return mTotalCountSlsJsonFile;
    }

    public void setTotalCountSlsJsonFile(int totalCountSlsJsonFile) {
        mTotalCountSlsJsonFile = totalCountSlsJsonFile;
    }
}
