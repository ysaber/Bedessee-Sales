package com.thebedesseegroup.sales.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * TODO: Document me...
 */
public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bedessee.db";
    private static final int CUR_DATABASE_VERSION = 25;


    public static class Tables {
        public static String PRODUCT = "product";
        public static String BRAND = "brand";
        public static String CATEGORY = "category";
        public static String USER = "user";
        public static String SALESMAN_STORE = "salesmanstore";
        public static String SAVED_ITEM = "saveditem";
        public static String SAVED_ORDER = "savedorder";
        public static String SIDE_MENU = "sidemenu";
        public static String CUST_SPEC_PRICE = "custspecprice";
    }


    public Database(Context context) {
        super(context, DATABASE_NAME, null, CUR_DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + Tables.PRODUCT + " (" +
                Contract.ProductColumns.COLUMN_ID + " integer primary key, " +
                Contract.ProductColumns.COLUMN_NUMBER + " text, " +
                Contract.ProductColumns.COLUMN_BRAND + " text, " +
                Contract.ProductColumns.COLUMN_DESCRIPTION + " text, " +
                Contract.ProductColumns.COLUMN_UOM + " text, " +
                Contract.ProductColumns.COLUMN_PRICE + " text, " +
                Contract.ProductColumns.COLUMN_LEVEL_1_PRICE + " text, " +
                Contract.ProductColumns.COLUMN_LEVEL_2_PRICE + " text, " +
                Contract.ProductColumns.COLUMN_LEVEL_3_PRICE + " text, " +
                Contract.ProductColumns.COLUMN_STATUS_CODE + " text, " +
                Contract.ProductColumns.COLUMN_STATUS_DESCRIPTION + " text, " +
                Contract.ProductColumns.COLUMN_CASES_PER_SKID + " text, " +
                Contract.ProductColumns.COLUMN_CASES_PER_ROW + " text, " +
                Contract.ProductColumns.COLUMN_LAYERS_PER_SKID + " text, " +
                Contract.ProductColumns.COLUMN_IMAGE_PATH + " text, " +
                Contract.ProductColumns.COLUMN_UNIT_PRICE + " text, " +
                Contract.ProductColumns.COLUMN_CASE_UOM + " text, " +
                Contract.ProductColumns.COLUMN_TOTAL_QTY + " text, " +
                Contract.ProductColumns.COLUMN_QTY1 + " text, " +
                Contract.ProductColumns.COLUMN_QTY2 + " text, " +
                Contract.ProductColumns.COLUMN_QTY3 + " text, " +
                Contract.ProductColumns.COLUMN_QTY4 + " text, " +
                Contract.ProductColumns.COLUMN_SHOW_QTY1 + " text, " +
                Contract.ProductColumns.COLUMN_SHOW_QTY2 + " text, " +
                Contract.ProductColumns.COLUMN_SHOW_QTY3 + " text, " +
                Contract.ProductColumns.COLUMN_SHOW_QTY4 + " text, " +
                Contract.ProductColumns.COLUMN_NOTE01 + " text, " +
                Contract.ProductColumns.COLUMN_NOTE02 + " text, " +
                Contract.ProductColumns.COLUMN_NOTE03 + " text, " +
                Contract.ProductColumns.COLUMN_NOTE04 + " text, " +
                Contract.ProductColumns.COLUMN_NOTE05 + " text, " +
                Contract.ProductColumns.COLUMN_POPUPPRICE + " text, " +
                Contract.ProductColumns.COLUMN_POPUPPRICEFLAG + " text, " +
                Contract.ProductColumns.COLUMN_LIKE_TAG + " text, " +
                Contract.ProductColumns.COLUMN_UPC + " text)");

        db.execSQL("CREATE TABLE " + Tables.BRAND + "(" +
                Contract.BrandColumns.COLUMN_ID + " integer primary key, " +
                Contract.BrandColumns.COLUMN_BRAND_NAME + " text, " +
                Contract.BrandColumns.COLUMN_BRAND_NUM_PRODUCTS + " text, " +
                Contract.BrandColumns.COLUMN_LOGO_NAME + " text)");

        db.execSQL("CREATE TABLE " + Tables.SIDE_MENU + "(" +
                Contract.SideMenuColumns.COLUMN_ID + " integer primary key autoincrement, " +
                Contract.SideMenuColumns.COLUMN_CODE + " text, " +
                Contract.SideMenuColumns.COLUMN_TITLE + " text, " +
                Contract.SideMenuColumns.COLUMN_SORT + " text, " +
                Contract.SideMenuColumns.COLUMN_COLOUR + " text, " +
                Contract.SideMenuColumns.COLUMN_COUNT + " text)");

        db.execSQL("CREATE TABLE " + Tables.CATEGORY + "(" +
                Contract.CategoryColumns.COLUMN_ID + " integer primary key, " +
                Contract.CategoryColumns.COLUMN_ACTIVE + " text, " +
                Contract.CategoryColumns.COLUMN_CHAR + " text, " +
                Contract.CategoryColumns.COLUMN_DESCRIPTION + " text)");

        db.execSQL("CREATE TABLE " + Tables.USER + "(" +
                Contract.UserColumns.COLUMN_ID + " integer primary key, " +
                Contract.UserColumns.COLUMN_NAME + " text, " +
                Contract.UserColumns.COLUMN_IS_ADMIN + " text, " +
                Contract.UserColumns.COLUMN_EMAIL + " text)");

        db.execSQL("CREATE TABLE " + Tables.SALESMAN_STORE + "(" +
                Contract.SalesmanStoreColumns.COLUMN_ID + " integer primary key, " +
                Contract.SalesmanStoreColumns.COLUMN_SALESPERSON + " text, " +
                Contract.SalesmanStoreColumns.COLUMN_SALES_EMAIL + " text, " +
                Contract.SalesmanStoreColumns.COLUMN_IS_ADMIN + " text, " +
                Contract.SalesmanStoreColumns.COLUMN_CUST_NAME + " text, " +
                Contract.SalesmanStoreColumns.COLUMN_CUST_NUM + " text, " +
                Contract.SalesmanStoreColumns.COLUMN_CUST_ADDR + " text, " +
                Contract.SalesmanStoreColumns.COLUMN_LAST_COLLECT_DAYS_OLD + " text, " +
                Contract.SalesmanStoreColumns.COLUMN_LAST_COLLECT_DATE + " text, " +
                Contract.SalesmanStoreColumns.COLUMN_LAST_COLLECT_INVOICE + " text, " +
                Contract.SalesmanStoreColumns.COLUMN_LAST_COLLECT_AMOUNT + " text, " +
                Contract.SalesmanStoreColumns.COLUMN_OUTSTANDING_BAL_DUE + " text, " +
                Contract.SalesmanStoreColumns.COLUMN_SHOW_POPUP + " text, " +
                Contract.SalesmanStoreColumns.COLUMN_STATEMENT_URL + " text)");

        db.execSQL("CREATE TABLE " + Tables.SAVED_ITEM + "(" +
                Contract.SavedItemColumns.COLUMN_ID + " integer primary key, " +
                Contract.SavedItemColumns.COLUMN_ORDER_ID + " text, " +
                Contract.SavedItemColumns.COLUMN_PRODUCT_NUMBER + " text, " +
                Contract.SavedItemColumns.COLUMN_PRODUCT_QUANTITY + " text, " +
                Contract.SavedItemColumns.COLUMN_PRODUCT_QUANTITY_TYPE + " text)");

        db.execSQL("CREATE TABLE " + Tables.SAVED_ORDER + "(" +
                Contract.SavedOrderColumns.COLUMN_ID + " text, " +
                Contract.SavedOrderColumns.COLUMN_START_TIME + " text, " +
                Contract.SavedOrderColumns.COLUMN_END_TIME + " text, " +
                Contract.SavedOrderColumns.COLUMN_NUM_PRODUCTS + " integer, " +
                Contract.SavedOrderColumns.COLUMN_IS_CLOSED + " text)");

        db.execSQL("CREATE TABLE " + Tables.CUST_SPEC_PRICE + "(" +
                Contract.CustSpecPriceColumns.COLUMN_ID + " text, " +
                Contract.CustSpecPriceColumns.COLUMN_CUST_NUM + " text, " +
                Contract.CustSpecPriceColumns.COLUMN_PROD_NUM + " text, " +
                Contract.CustSpecPriceColumns.COLUMN_PRICE + " text, " +
                Contract.CustSpecPriceColumns.COLUMN_UNIT_PRICE + " text, " +
                Contract.CustSpecPriceColumns.COLUMN_1 + " text, " +
                Contract.CustSpecPriceColumns.COLUMN_LEVEL1PRICE + " text, " +
                Contract.CustSpecPriceColumns.COLUMN_2 + " text, " +
                Contract.CustSpecPriceColumns.COLUMN_LEVEL2PRICE + " text, " +
                Contract.CustSpecPriceColumns.COLUMN_3 + " text, " +
                Contract.CustSpecPriceColumns.COLUMN_LEVEL3PRICE + " text, " +
                Contract.CustSpecPriceColumns.COLUMN_NOTE_01 + " text, " +
                Contract.CustSpecPriceColumns.COLUMN_NOTE_02 + " text, " +
                Contract.CustSpecPriceColumns.COLUMN_NOTE_03 + " text, " +
                Contract.CustSpecPriceColumns.COLUMN_NOTE_04 + " text, " +
                Contract.CustSpecPriceColumns.COLUMN_NOTE_05 + " text)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tables.PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.BRAND);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.SIDE_MENU);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.USER);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.SALESMAN_STORE);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.SAVED_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.SAVED_ORDER);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.CUST_SPEC_PRICE);
        onCreate(db);
    }
}
