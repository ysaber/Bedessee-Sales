package com.thebedesseegroup.sales.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.thebedesseegroup.sales.provider.Contract;
import com.thebedesseegroup.sales.provider.ProviderUtils;
import com.thebedesseegroup.sales.salesman.Salesman;
import com.thebedesseegroup.sales.salesmanstore.SalesmanStore;


public class SharedPrefsManager {

    final private String APP_SHARED_PREFS_NAME = "bedessee_imports_shared_preferences";
    final private String KEY_VIRGIN = "key_virgin";
    final private String KEY_CURRENT_SALESMAN = "key_current_salesman";

    final private String KEY_LOGGED_IN_USER = "logged_in_user";
    final private String KEY_IS_ADMIN = "is_admin";
    final private String KEY_SUGAR_SYNC_DIR = "sugar_sync_dir";
    final private String KEY_CURRENT_ORDER_ID = "current_order_id";

    /** APP_INFO KEYS */
    final private String KEY_ORDER_EMAIL_RECIPS = "order_email_recips";
    final private String KEY_LINK_TO_PROD_IMGS = "link_to_prod_imgs";
    final private String KEY_LINK_TO_BRAND_LOGO_IMGS = "link_to_brand_logo_imgs";
    final private String KEY_LINK_TO_SALE_SHEET_IMGS = "link_to_sale_sheets_imgs";
    final private String KEY_LINK_TO_CUST_STMT_TXT_FILES = "link_to_cust_smtm_txt";
    final private String KEY_LINK_TO_CUST_STMT_SMALL_TXT_FILES = "link_to_cust_smtm_small_txt";
    final private String KEY_LINK_TO_CUST_SALES_STATS_TXT_FILES = "link_to_cust_sls_sts";
    final private String KEY_LINK_TO_CUSTOMER_ACOUNTS_JSON_FILE = "link_to_cust_accnt_json";
    final private String KEY_LINK_TO_PRODUCTS_JSON_FILE = "link_to_products_json";
    final private String KEY_LINK_TO_BRANDS_JSON_FILE = "link_tobrands_json";
    final private String KEY_STATEMENT_EMAIL_SUBJECT = "statement_email_subject";
    private static final String KEY_USE_OLD_MATCH_LOGIC = "use_old_match_logic";
    private static final String KEY_FADE_PERCENTAGE = "face_percent";

    private static final String KEY_TOTAL_COUNT_BRANDS = "count_brands";
    private static final String KEY_TOTAL_COUNT_CATEGORIES = "count_categories";
    private static final String KEY_TOTAL_COUNT_PRODUCTS = "count_products";
    private static final String KEY_TOTAL_COUNT_SIDE_PANEL = "cont_side_panel";
    private static final String KEY_TOTAL_COUNT_SLS = "count_sls";
    private static final String KEY_APK_PATH = "apk_path";
    private static final String KEY_ADMIN_PIN = "admin_pin";


    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private Context mContext;
    private String mFadePercentage;


    public SharedPrefsManager(Context context) {
        mSharedPreferences = context.getSharedPreferences(APP_SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mContext = context;
    }


    public void setCurrentSalesman(Salesman currentSalesman) {
        if(currentSalesman != null) {
            mEditor.putString(KEY_CURRENT_SALESMAN, currentSalesman.getName()).apply();
        }
    }


    public Salesman getCurrentSalesman() {
        final String salesmanName = mSharedPreferences.getString(KEY_CURRENT_SALESMAN, null);

        final Cursor cursor = mContext.getContentResolver().query(Contract.SalesmanStore.CONTENT_URI, null, Contract.SalesmanStoreColumns.COLUMN_SALESPERSON + " = '" + salesmanName + "'", null, null);

        if(cursor.moveToFirst()) {
            final SalesmanStore salesmanStore = ProviderUtils.cursorToSalesmanStore(cursor);
            return salesmanStore.getSalesman();
        }  else {
            return null;
        }
    }


    public String getCurrentOrderId() {
        return mSharedPreferences.getString(KEY_CURRENT_ORDER_ID, "");
    }


    public void setCurrentOrderId(final String orderId) {
        mEditor.putString(KEY_CURRENT_ORDER_ID, orderId).apply();
    }


    public void removeLoggedInUser() {
        mEditor.remove(KEY_LOGGED_IN_USER).apply();
    }


    public void saveLoggedInUser(Salesman user) {
        mEditor.putString(KEY_LOGGED_IN_USER, user.getName()).apply();
    }


    public String getLoggedInUser() {
        return mSharedPreferences.getString(KEY_LOGGED_IN_USER, "");
    }

    public void setIsAdmin(final boolean isAdmin) {
        mEditor.putBoolean(KEY_IS_ADMIN, isAdmin).apply();
    }

    public boolean getIsAdmin() {
        return mSharedPreferences.getBoolean(KEY_IS_ADMIN, false);
    }

    public void setSugarSyncDir(final String dir) {
        mEditor.putString(KEY_SUGAR_SYNC_DIR, dir).apply();
    }

    public String getSugarSyncDir() {
        return mSharedPreferences.getString(KEY_SUGAR_SYNC_DIR, null);
    }

    public void setOrderEmailRecips(final String recips) {
        mEditor.putString(KEY_ORDER_EMAIL_RECIPS, recips).apply();
    }

    public String getOrderEmailRecips() {
        return mSharedPreferences.getString(KEY_ORDER_EMAIL_RECIPS, null);
    }


    public void setLinkToProdImages(final String recips) {
        mEditor.putString(KEY_LINK_TO_PROD_IMGS, recips).apply();
    }

    public String getLinkToProdImages() {
        return mSharedPreferences.getString(KEY_LINK_TO_PROD_IMGS, null);
    }


    public void setLinkToBrandLogoImages(final String recips) {
        mEditor.putString(KEY_LINK_TO_BRAND_LOGO_IMGS, recips).apply();
    }

    public String getLinkToBrandLogoImages() {
        return mSharedPreferences.getString(KEY_LINK_TO_BRAND_LOGO_IMGS, null);
    }


    public void setLinkToSellSheetImages(final String recips) {
        mEditor.putString(KEY_LINK_TO_SALE_SHEET_IMGS, recips).apply();
    }

    public String getLinkToSellSheetImages() {
        return mSharedPreferences.getString(KEY_LINK_TO_SALE_SHEET_IMGS, null);
    }


    public void setLinkToCustomerStatements(final String linkToCustomerStatements) {
        mEditor.putString(KEY_LINK_TO_CUST_STMT_TXT_FILES, linkToCustomerStatements).apply();
    }

    public String getLinkToCustomerStatements() {
        return mSharedPreferences.getString(KEY_LINK_TO_CUST_STMT_TXT_FILES, null);
    }


    public void setLinkToCustomerStatementsSmall(final String linkToCustomerStatementsSmall) {
        mEditor.putString(KEY_LINK_TO_CUST_STMT_SMALL_TXT_FILES, linkToCustomerStatementsSmall).apply();
    }

    public String getLinkToCustomerStatementsSmall() {
        return mSharedPreferences.getString(KEY_LINK_TO_CUST_STMT_SMALL_TXT_FILES, null);
    }


    public void setLinkToCustomerSalesStats(final String linkToCustomerSalesStats) {
        mEditor.putString(KEY_LINK_TO_CUST_SALES_STATS_TXT_FILES, linkToCustomerSalesStats).apply();
    }

    public String getLinkToCustomerSalesStats() {
        return mSharedPreferences.getString(KEY_LINK_TO_CUST_SALES_STATS_TXT_FILES, null);
    }


    public void setLinkToCustomerAccountsJson(final String linkToCustomerAccountsJson) {
        mEditor.putString(KEY_LINK_TO_CUSTOMER_ACOUNTS_JSON_FILE, linkToCustomerAccountsJson).apply();
    }

    public String getLinkToCustomerAccountsJson() {
        return mSharedPreferences.getString(KEY_LINK_TO_CUSTOMER_ACOUNTS_JSON_FILE, null);
    }


    public void setLinkToProductsJson(final String linkToProductsJson) {
        mEditor.putString(KEY_LINK_TO_PRODUCTS_JSON_FILE, linkToProductsJson).apply();
    }

    public String getLinkToProductsJson() {
        return mSharedPreferences.getString(KEY_LINK_TO_PRODUCTS_JSON_FILE, null);
    }


    public void setLinkToBrandsJson(final String linkToBrandsJson) {
        mEditor.putString(KEY_LINK_TO_BRANDS_JSON_FILE, linkToBrandsJson).apply();
    }

    public String getLinkToBrandsJson() {
        return mSharedPreferences.getString(KEY_LINK_TO_BRANDS_JSON_FILE, null);
    }


    public void setStatementEmailSubject(final String statementEmailSubject) {
        mEditor.putString(KEY_STATEMENT_EMAIL_SUBJECT, statementEmailSubject).apply();
    }

    public String getStatementEmailSubject() {
        return mSharedPreferences.getString(KEY_STATEMENT_EMAIL_SUBJECT, "");
    }


    public void setUseNewLikeLogic(String useOldMatchLogin) {
        mEditor.putString(KEY_USE_OLD_MATCH_LOGIC, useOldMatchLogin).apply();
    }


    public String getUseNewLikeLogic() {
        return mSharedPreferences.getString(KEY_USE_OLD_MATCH_LOGIC, "");
    }

    public void setFadePercentage(String fadePercentage) {
        mEditor.putString(KEY_FADE_PERCENTAGE, fadePercentage).apply();
    }

    public String getFadePercentage() {
        return mSharedPreferences.getString(KEY_FADE_PERCENTAGE, "");
    }

    public void setApkPath(String apkPath) {
        mEditor.putString(KEY_APK_PATH, apkPath).apply();
    }

    public String getApkPath() {
        return mSharedPreferences.getString(KEY_APK_PATH, "");
    }

    public void setCountBrands(int count) {
        mEditor.putInt(KEY_TOTAL_COUNT_BRANDS, count).apply();
    }

    public int getCountBrands() {
        return mSharedPreferences.getInt(KEY_TOTAL_COUNT_BRANDS, 0);
    }


    public int getCountCategories() {
        return mSharedPreferences.getInt(KEY_TOTAL_COUNT_CATEGORIES, 0);
    }

    public void setCountCategories(int count) {
        mEditor.putInt(KEY_TOTAL_COUNT_CATEGORIES, count).apply();
    }

    public int getCountProducts() {
        return mSharedPreferences.getInt(KEY_TOTAL_COUNT_PRODUCTS, 0);
    }

    public void setCountProducts(int count) {
        mEditor.putInt(KEY_TOTAL_COUNT_PRODUCTS, count).apply();
    }

    public int getCountSidePanel() {
        return mSharedPreferences.getInt(KEY_TOTAL_COUNT_SIDE_PANEL, 0);
    }

    public void setCountSidePanel(int count) {
        mEditor.putInt(KEY_TOTAL_COUNT_SIDE_PANEL, count).apply();
    }

    public int getCountSls() {
        return mSharedPreferences.getInt(KEY_TOTAL_COUNT_SLS, 0);
    }


    public void setCountSls(int count) {
        mEditor.putInt(KEY_TOTAL_COUNT_SLS, count).apply();
    }

    public int getAdminPin() {
        return mSharedPreferences.getInt(KEY_ADMIN_PIN, 0);
    }


    public void setAdminPin(int adminPin) {
        mEditor.putInt(KEY_ADMIN_PIN, adminPin).apply();
    }

}
