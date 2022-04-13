package com.thebedesseegroup.sales.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.thebedesseegroup.sales.customview.QtySelector;
import com.thebedesseegroup.sales.main.SideMenu;
import com.thebedesseegroup.sales.orderhistory.SavedItem;
import com.thebedesseegroup.sales.orderhistory.SavedOrder;
import com.thebedesseegroup.sales.product.CustSpecPrice;
import com.thebedesseegroup.sales.product.Product;
import com.thebedesseegroup.sales.product.brand.Brand;
import com.thebedesseegroup.sales.product.category.Category2;
import com.thebedesseegroup.sales.salesman.Salesman;
import com.thebedesseegroup.sales.salesmanstore.SalesmanStore;
import com.thebedesseegroup.sales.shoppingcart.ShoppingCartProduct;
import com.thebedesseegroup.sales.store.Store;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * Provider utilities
 */
public class ProviderUtils {


    public static ContentValues ProductToContentValues(@NonNull final Product product) {

        ContentValues values = new ContentValues();

        values.put(Contract.ProductColumns.COLUMN_ID, product.getNumber().hashCode());
        values.put(Contract.ProductColumns.COLUMN_NUMBER, product.getNumber());
        values.put(Contract.ProductColumns.COLUMN_BRAND, product.getBrand());
        values.put(Contract.ProductColumns.COLUMN_DESCRIPTION, product.getDescription());
        values.put(Contract.ProductColumns.COLUMN_UOM, product.getPieceUom());
        values.put(Contract.ProductColumns.COLUMN_PRICE, product.getCasePrice());
        values.put(Contract.ProductColumns.COLUMN_LEVEL_1_PRICE, product.getLevel1Price());
        values.put(Contract.ProductColumns.COLUMN_LEVEL_2_PRICE, product.getLevel2Price());
        values.put(Contract.ProductColumns.COLUMN_LEVEL_3_PRICE, product.getLevel3Price());
        values.put(Contract.ProductColumns.COLUMN_STATUS_CODE, product.getStatusCode());
        values.put(Contract.ProductColumns.COLUMN_STATUS_DESCRIPTION, product.getStatusDescription());
        values.put(Contract.ProductColumns.COLUMN_CASES_PER_SKID, product.getCasesPerSkid());
        values.put(Contract.ProductColumns.COLUMN_CASES_PER_ROW, product.getCasesPerRow());
        values.put(Contract.ProductColumns.COLUMN_LAYERS_PER_SKID, product.getLayersPerSkid());
        values.put(Contract.ProductColumns.COLUMN_IMAGE_PATH, product.getImagePath());
        values.put(Contract.ProductColumns.COLUMN_UNIT_PRICE, product.getPiecePrice());
        values.put(Contract.ProductColumns.COLUMN_CASE_UOM, product.getCaseUom());
        values.put(Contract.ProductColumns.COLUMN_TOTAL_QTY, product.getTotalQty());
        values.put(Contract.ProductColumns.COLUMN_UPC, product.getUPC());
        values.put(Contract.ProductColumns.COLUMN_QTY1, product.getQty1());
        values.put(Contract.ProductColumns.COLUMN_QTY2, product.getQty2());
        values.put(Contract.ProductColumns.COLUMN_QTY3, product.getQty3());
        values.put(Contract.ProductColumns.COLUMN_QTY4, product.getQty4());
        values.put(Contract.ProductColumns.COLUMN_SHOW_QTY1, product.getShowQty1());
        values.put(Contract.ProductColumns.COLUMN_SHOW_QTY2, product.getShowQty2());
        values.put(Contract.ProductColumns.COLUMN_SHOW_QTY3, product.getShowQty3());
        values.put(Contract.ProductColumns.COLUMN_SHOW_QTY4, product.getShowQty4());
        values.put(Contract.ProductColumns.COLUMN_NOTE01, product.getNote01());
        values.put(Contract.ProductColumns.COLUMN_NOTE02, product.getNote02());
        values.put(Contract.ProductColumns.COLUMN_NOTE03, product.getNote03());
        values.put(Contract.ProductColumns.COLUMN_NOTE04, product.getNote04());
        values.put(Contract.ProductColumns.COLUMN_NOTE05, product.getNote05());
        values.put(Contract.ProductColumns.COLUMN_POPUPPRICE, product.getPopUpPrice());
        values.put(Contract.ProductColumns.COLUMN_POPUPPRICEFLAG, product.getPopUpPriceFlag());
        values.put(Contract.ProductColumns.COLUMN_LIKE_TAG, product.getLikeTag());
        return values;
    }



    public static Product cursorToProduct(@NonNull final Cursor cursor) {
        Product product = new Product();
        product.setNumber(cursor.getString(cursor.getColumnIndex(Contract.ProductColumns.COLUMN_NUMBER)));
        product.setBrand(cursor.getString(cursor.getColumnIndex(Contract.ProductColumns.COLUMN_BRAND)));
        product.setDescription(cursor.getString(cursor.getColumnIndex(Contract.ProductColumns.COLUMN_DESCRIPTION)));
        product.setPieceUom(cursor.getString(cursor.getColumnIndex(Contract.ProductColumns.COLUMN_UOM)));
        product.setCasePrice(cursor.getString(cursor.getColumnIndex(Contract.ProductColumns.COLUMN_PRICE)));
        product.setLevel1Price(cursor.getString(cursor.getColumnIndex(Contract.ProductColumns.COLUMN_LEVEL_1_PRICE)));
        product.setLevel2Price(cursor.getString(cursor.getColumnIndex(Contract.ProductColumns.COLUMN_LEVEL_2_PRICE)));
        product.setLevel3Price(cursor.getString(cursor.getColumnIndex(Contract.ProductColumns.COLUMN_LEVEL_3_PRICE)));
        product.setStatusCode(cursor.getString(cursor.getColumnIndex(Contract.ProductColumns.COLUMN_STATUS_CODE)));
        product.setStatusDescription(cursor.getString(cursor.getColumnIndex(Contract.ProductColumns.COLUMN_STATUS_DESCRIPTION)));
        product.setCasesPerSkid(cursor.getString(cursor.getColumnIndex(Contract.ProductColumns.COLUMN_CASES_PER_SKID)));
        product.setCasesPerRow(cursor.getString(cursor.getColumnIndex(Contract.ProductColumns.COLUMN_CASES_PER_ROW)));
        product.setLayersPerSkid(cursor.getString(cursor.getColumnIndex(Contract.ProductColumns.COLUMN_LAYERS_PER_SKID)));
        product.setImagePath(cursor.getString(cursor.getColumnIndex(Contract.ProductColumns.COLUMN_IMAGE_PATH)));
        product.setPiecePrice(cursor.getString(cursor.getColumnIndex(Contract.ProductColumns.COLUMN_UNIT_PRICE)));
        product.setCaseUom(cursor.getString(cursor.getColumnIndex(Contract.ProductColumns.COLUMN_CASE_UOM)));
        product.setTotalQty(cursor.getString(cursor.getColumnIndex(Contract.ProductColumns.COLUMN_TOTAL_QTY)));
        product.setUPC(cursor.getString(cursor.getColumnIndex(Contract.ProductColumns.COLUMN_UPC)));
        product.setQty1(cursor.getString(cursor.getColumnIndex(Contract.ProductColumns.COLUMN_QTY1)));
        product.setQty2(cursor.getString(cursor.getColumnIndex(Contract.ProductColumns.COLUMN_QTY2)));
        product.setQty3(cursor.getString(cursor.getColumnIndex(Contract.ProductColumns.COLUMN_QTY3)));
        product.setQty4(cursor.getString(cursor.getColumnIndex(Contract.ProductColumns.COLUMN_QTY4)));
        product.setShowQty1(cursor.getString(cursor.getColumnIndex(Contract.ProductColumns.COLUMN_SHOW_QTY1)));
        product.setShowQty2(cursor.getString(cursor.getColumnIndex(Contract.ProductColumns.COLUMN_SHOW_QTY2)));
        product.setShowQty3(cursor.getString(cursor.getColumnIndex(Contract.ProductColumns.COLUMN_SHOW_QTY3)));
        product.setShowQty4(cursor.getString(cursor.getColumnIndex(Contract.ProductColumns.COLUMN_SHOW_QTY4)));
        product.setNote01(cursor.getString(cursor.getColumnIndex(Contract.ProductColumns.COLUMN_NOTE01)));
        product.setNote02(cursor.getString(cursor.getColumnIndex(Contract.ProductColumns.COLUMN_NOTE02)));
        product.setNote03(cursor.getString(cursor.getColumnIndex(Contract.ProductColumns.COLUMN_NOTE03)));
        product.setNote04(cursor.getString(cursor.getColumnIndex(Contract.ProductColumns.COLUMN_NOTE04)));
        product.setNote05(cursor.getString(cursor.getColumnIndex(Contract.ProductColumns.COLUMN_NOTE05)));
        product.setPopUpPrice(cursor.getString(cursor.getColumnIndex(Contract.ProductColumns.COLUMN_POPUPPRICE)));
        product.setPopUpPriceFlag(cursor.getString(cursor.getColumnIndex(Contract.ProductColumns.COLUMN_POPUPPRICEFLAG)));
        product.setLikeTag(cursor.getString(cursor.getColumnIndex(Contract.ProductColumns.COLUMN_LIKE_TAG)));

        return product;
    }



    public static ContentValues BrandToContentValues(@NonNull final Brand brand) {
        final ContentValues values = new ContentValues(4);

        values.put(Contract.BrandColumns.COLUMN_ID, brand.getName().hashCode());
        values.put(Contract.BrandColumns.COLUMN_BRAND_NAME, brand.getName());
        values.put(Contract.BrandColumns.COLUMN_BRAND_NUM_PRODUCTS, brand.getNumProducts());
        values.put(Contract.BrandColumns.COLUMN_LOGO_NAME, brand.getLogoName());

        return values;
    }


    public static Brand cursorToBrand(@NonNull final Cursor cursor) {
        final String name = cursor.getString(cursor.getColumnIndex(Contract.BrandColumns.COLUMN_BRAND_NAME));
        final int numProducts = cursor.getInt(cursor.getColumnIndex(Contract.BrandColumns.COLUMN_BRAND_NUM_PRODUCTS));
        final String logoName = cursor.getString(cursor.getColumnIndex(Contract.BrandColumns.COLUMN_LOGO_NAME));

        return new Brand(name, numProducts, logoName);
    }



    public static ContentValues UserToContentValues(@NonNull final Salesman user) {
        final ContentValues values = new ContentValues(4);

        values.put(Contract.UserColumns.COLUMN_ID, user.getEmail().hashCode());
        values.put(Contract.UserColumns.COLUMN_NAME, user.getName());
        values.put(Contract.UserColumns.COLUMN_EMAIL, user.getEmail());
        values.put(Contract.UserColumns.COLUMN_IS_ADMIN, user.isAdmin() ? "YES" : "NO");

        return values;
    }


    public static Salesman CursorToSalesman(@NonNull final Cursor cursor) {
        final String name = cursor.getString(cursor.getColumnIndex(Contract.UserColumns.COLUMN_NAME));
        final String email = cursor.getString(cursor.getColumnIndex(Contract.UserColumns.COLUMN_EMAIL));
        final Salesman salesman = new Salesman(name, email);
        salesman.setIsAdmin(cursor.getString(cursor.getColumnIndex(Contract.UserColumns.COLUMN_IS_ADMIN)).equals("YES"));
        return salesman;
    }


    /**
     *
     * Returns a SalesmanStore object using data from a cursor.
     *
     * @param cursor Cursor
     * @return SalesmanStore
     *
     */
    public static SalesmanStore cursorToSalesmanStore(@NonNull Cursor cursor) {

        SalesmanStore salesmanStore = new SalesmanStore();

        Salesman salesman = new Salesman(cursor.getString(cursor.getColumnIndex(Contract.SalesmanStoreColumns.COLUMN_SALESPERSON)),
                cursor.getString(cursor.getColumnIndex(Contract.SalesmanStoreColumns.COLUMN_SALES_EMAIL)));
        salesman.setIsAdmin(cursor.getString(cursor.getColumnIndex(Contract.SalesmanStoreColumns.COLUMN_IS_ADMIN)).equals("YES"));

        Store store = new Store();
        store.setName(cursor.getString(cursor.getColumnIndex(Contract.SalesmanStoreColumns.COLUMN_CUST_NAME)));
        store.setNumber(cursor.getString(cursor.getColumnIndex(Contract.SalesmanStoreColumns.COLUMN_CUST_NUM)));
        store.setAddress(cursor.getString(cursor.getColumnIndex(Contract.SalesmanStoreColumns.COLUMN_CUST_ADDR)));
        store.setLastCollectDaysOld(cursor.getString(cursor.getColumnIndex(Contract.SalesmanStoreColumns.COLUMN_LAST_COLLECT_DAYS_OLD)));
        store.setLastCollectDate(cursor.getString(cursor.getColumnIndex(Contract.SalesmanStoreColumns.COLUMN_LAST_COLLECT_DATE)));
        store.setLastCollectInvoice(cursor.getString(cursor.getColumnIndex(Contract.SalesmanStoreColumns.COLUMN_LAST_COLLECT_INVOICE)));
        store.setLastCollectAmount(cursor.getString(cursor.getColumnIndex(Contract.SalesmanStoreColumns.COLUMN_LAST_COLLECT_AMOUNT)));
        store.setOutstadingBalanceDue(cursor.getString(cursor.getColumnIndex(Contract.SalesmanStoreColumns.COLUMN_OUTSTANDING_BAL_DUE)));
        store.setStatementUrl(cursor.getString(cursor.getColumnIndex(Contract.SalesmanStoreColumns.COLUMN_STATEMENT_URL)));
        store.setShowPopup(cursor.getString(cursor.getColumnIndex(Contract.SalesmanStoreColumns.COLUMN_SHOW_POPUP)).equals("Y"));

        salesmanStore.setSalesman(salesman);
        salesmanStore.setStore(store);

        return salesmanStore;
    }


    public static ContentValues salesmanStoreToContentValues(@NonNull Salesman salesman, @NonNull Store store) {
        ContentValues values = new ContentValues();
        values.put(Contract.SalesmanStoreColumns.COLUMN_ID, store.getNumber().hashCode() + salesman.hashCode());
        values.put(Contract.SalesmanStoreColumns.COLUMN_SALESPERSON, salesman.getName());
        values.put(Contract.SalesmanStoreColumns.COLUMN_SALES_EMAIL, salesman.getEmail());
        values.put(Contract.SalesmanStoreColumns.COLUMN_IS_ADMIN, salesman.isAdmin() ? "YES" : "NO");
        values.put(Contract.SalesmanStoreColumns.COLUMN_CUST_NAME, store.getName());
        values.put(Contract.SalesmanStoreColumns.COLUMN_CUST_NUM, store.getNumber());
        values.put(Contract.SalesmanStoreColumns.COLUMN_CUST_ADDR, store.getAddress());
        values.put(Contract.SalesmanStoreColumns.COLUMN_LAST_COLLECT_DAYS_OLD, store.getLastCollectDaysOld());
        values.put(Contract.SalesmanStoreColumns.COLUMN_LAST_COLLECT_DATE, store.getLastCollectDate());
        values.put(Contract.SalesmanStoreColumns.COLUMN_LAST_COLLECT_INVOICE, store.getLastCollectInvoice());
        values.put(Contract.SalesmanStoreColumns.COLUMN_LAST_COLLECT_AMOUNT, store.getLastCollectAmount());
        values.put(Contract.SalesmanStoreColumns.COLUMN_OUTSTANDING_BAL_DUE, store.getOutstadingBalanceDue());
        values.put(Contract.SalesmanStoreColumns.COLUMN_STATEMENT_URL, store.getStatementUrl());
        values.put(Contract.SalesmanStoreColumns.COLUMN_SHOW_POPUP, store.isShowPopup() ? "Y" : "N");

        return values;
    }


    public static SavedItem cursorToSavedItem(@NonNull final Context context, @NonNull final Cursor cursor) {

        final String orderId = cursor.getString(cursor.getColumnIndex(Contract.SavedItemColumns.COLUMN_ORDER_ID));
        final String productNumber = cursor.getString(cursor.getColumnIndex(Contract.SavedItemColumns.COLUMN_PRODUCT_NUMBER));
        final int qty = cursor.getInt(cursor.getColumnIndex(Contract.SavedItemColumns.COLUMN_PRODUCT_QUANTITY));
        final QtySelector.ItemType type = QtySelector.ItemType.valueOf(cursor.getString(cursor.getColumnIndex(Contract.SavedItemColumns.COLUMN_PRODUCT_QUANTITY_TYPE)));

        final Cursor productCursor = context.getContentResolver().query(Contract.Product.CONTENT_URI, null, Contract.ProductColumns.COLUMN_NUMBER + " = '" + productNumber + "'", null, null);

        if (productCursor.moveToFirst()) {
            final Product product = ProviderUtils.cursorToProduct(productCursor);

            final ShoppingCartProduct shoppingCartProduct = new ShoppingCartProduct(product, qty, type);

            return new SavedItem(orderId, shoppingCartProduct);
        } else {
            return null;
        }
    }


    public static ContentValues savedItemToContentValues(@NonNull final SavedItem item) {

        final ContentValues values = new ContentValues();

        final ShoppingCartProduct shProduct = item.getShoppingCartProduct();

        values.put(Contract.SavedItemColumns.COLUMN_ORDER_ID, item.getOrderId());
        values.put(Contract.SavedItemColumns.COLUMN_PRODUCT_NUMBER, shProduct.getProduct().getNumber());
        values.put(Contract.SavedItemColumns.COLUMN_PRODUCT_QUANTITY, shProduct.getQuantity());
        values.put(Contract.SavedItemColumns.COLUMN_PRODUCT_QUANTITY_TYPE, shProduct.getItemType().name());

        return values;
    }

    public static SavedOrder cursorToSavedOrder(@NonNull Cursor cursor) {

        try {
            final DateFormat dateFormat = DateFormat.getDateTimeInstance();

            final String id = cursor.getString(cursor.getColumnIndex(Contract.SavedOrderColumns.COLUMN_ID));
            Date startTime = null;
            Date endTime = null;
            try {
                startTime = dateFormat.parse(cursor.getString(cursor.getColumnIndex(Contract.SavedOrderColumns.COLUMN_START_TIME)));
                endTime = dateFormat.parse(cursor.getString(cursor.getColumnIndex(Contract.SavedOrderColumns.COLUMN_END_TIME)));
            } catch (ParseException ignore) {
            }
            final boolean isClosed = cursor.getString(cursor.getColumnIndex(Contract.SavedOrderColumns.COLUMN_IS_CLOSED)).equals("true");

            final int numProducts = cursor.getInt(cursor.getColumnIndex(Contract.SavedOrderColumns.COLUMN_NUM_PRODUCTS));

            return new SavedOrder(id, startTime, endTime, isClosed, numProducts);
        } catch (CursorIndexOutOfBoundsException ignore) {
            return null;
        }
    }


    public static ContentValues savedOrderToContentValues(@NonNull SavedOrder order) {

        final ContentValues values = new ContentValues();

        final DateFormat dateFormat = DateFormat.getDateTimeInstance();

        values.put(Contract.SavedOrderColumns.COLUMN_ID, order.getId());
        values.put(Contract.SavedOrderColumns.COLUMN_START_TIME, dateFormat.format(order.getStartTime()));

        String endTime = null;
        if(order.getEndTime() != null) {
            endTime = dateFormat.format(order.getEndTime());
        }
        values.put(Contract.SavedOrderColumns.COLUMN_END_TIME, !TextUtils.isEmpty(endTime) ? endTime : "");

        values.put(Contract.SavedOrderColumns.COLUMN_IS_CLOSED, order.isClosed());

        values.put(Contract.SavedOrderColumns.COLUMN_NUM_PRODUCTS, 0);

        return values;
    }

    public static ContentValues categoryToContentValues(@NonNull final Category2 category) {

        final ContentValues values = new ContentValues(4);

        values.put(Contract.CategoryColumns.COLUMN_ID, category.hashCode());
        values.put(Contract.CategoryColumns.COLUMN_ACTIVE, category.isActive());
        values.put(Contract.CategoryColumns.COLUMN_CHAR, category.getChar());
        values.put(Contract.CategoryColumns.COLUMN_DESCRIPTION, category.getDescription());

        return values;
    }


    public static Category2 cursorToCategory(@NonNull final Cursor cursor) {
        final String isActive = cursor.getString(cursor.getColumnIndex(Contract.CategoryColumns.COLUMN_ACTIVE));
        final String aChar = cursor.getString(cursor.getColumnIndex(Contract.CategoryColumns.COLUMN_CHAR));
        final String description = cursor.getString(cursor.getColumnIndex(Contract.CategoryColumns.COLUMN_DESCRIPTION));
        return new Category2(isActive, aChar, description);
    }


    public static ContentValues sideMenuToContentValues(@NonNull final SideMenu sideMenu) {

        final ContentValues values = new ContentValues(6);

        values.put(Contract.SideMenuColumns.COLUMN_ID, sideMenu.hashCode());
        values.put(Contract.SideMenuColumns.COLUMN_CODE, sideMenu.getStatusCode());
        values.put(Contract.SideMenuColumns.COLUMN_TITLE, sideMenu.getMenuTitle());
        values.put(Contract.SideMenuColumns.COLUMN_COLOUR, sideMenu.getColour());
        values.put(Contract.SideMenuColumns.COLUMN_SORT, sideMenu.getSort());
        values.put(Contract.SideMenuColumns.COLUMN_COUNT, sideMenu.getCount());

        return values;
    }


    public static SideMenu cursorToSideMenu(@NonNull final Cursor cursor) {
        final String code = cursor.getString(cursor.getColumnIndex(Contract.SideMenuColumns.COLUMN_CODE));
        final String title = cursor.getString(cursor.getColumnIndex(Contract.SideMenuColumns.COLUMN_TITLE));
        final String sort = cursor.getString(cursor.getColumnIndex(Contract.SideMenuColumns.COLUMN_SORT));
        final String colour = cursor.getString(cursor.getColumnIndex(Contract.SideMenuColumns.COLUMN_COLOUR));
        final String count = cursor.getString(cursor.getColumnIndex(Contract.SideMenuColumns.COLUMN_COUNT));

        return new SideMenu(colour, count, title, sort, code);
    }

    public static ContentValues custSpecPriceToContentValues(@NonNull CustSpecPrice custSpecPrice) {
        final ContentValues values = new ContentValues();

        values.put(Contract.CustSpecPriceColumns.COLUMN_ID, custSpecPrice.getCustNum() + custSpecPrice.getProdNum());
        values.put(Contract.CustSpecPriceColumns.COLUMN_CUST_NUM, custSpecPrice.getCustNum());
        values.put(Contract.CustSpecPriceColumns.COLUMN_PROD_NUM, custSpecPrice.getProdNum());
        values.put(Contract.CustSpecPriceColumns.COLUMN_PRICE, custSpecPrice.getPrice());
        values.put(Contract.CustSpecPriceColumns.COLUMN_UNIT_PRICE, custSpecPrice.getUnitPrice());
        values.put(Contract.CustSpecPriceColumns.COLUMN_1, custSpecPrice.getM1());
        values.put(Contract.CustSpecPriceColumns.COLUMN_LEVEL1PRICE, custSpecPrice.getLevel1Price());
        values.put(Contract.CustSpecPriceColumns.COLUMN_2, custSpecPrice.getM2());
        values.put(Contract.CustSpecPriceColumns.COLUMN_LEVEL2PRICE, custSpecPrice.getLevel2Price());
        values.put(Contract.CustSpecPriceColumns.COLUMN_3, custSpecPrice.getM3());
        values.put(Contract.CustSpecPriceColumns.COLUMN_LEVEL3PRICE, custSpecPrice.getLevel3Price());
        values.put(Contract.CustSpecPriceColumns.COLUMN_NOTE_01, custSpecPrice.getNote01());
        values.put(Contract.CustSpecPriceColumns.COLUMN_NOTE_02, custSpecPrice.getNote02());
        values.put(Contract.CustSpecPriceColumns.COLUMN_NOTE_03, custSpecPrice.getNote03());
        values.put(Contract.CustSpecPriceColumns.COLUMN_NOTE_04, custSpecPrice.getNote04());
        values.put(Contract.CustSpecPriceColumns.COLUMN_NOTE_05, custSpecPrice.getNote05());


        return values;
    }

    public static CustSpecPrice cursorToCustSpecPrice(@NonNull final Cursor cursor) {

        final String custNum = cursor.getString(cursor.getColumnIndex(Contract.CustSpecPriceColumns.COLUMN_CUST_NUM));
        final String prodNum = cursor.getString(cursor.getColumnIndex(Contract.CustSpecPriceColumns.COLUMN_PROD_NUM));
        final String price = cursor.getString(cursor.getColumnIndex(Contract.CustSpecPriceColumns.COLUMN_PRICE));
        final String unitPrice = cursor.getString(cursor.getColumnIndex(Contract.CustSpecPriceColumns.COLUMN_UNIT_PRICE));
        final String m1 = cursor.getString(cursor.getColumnIndex(Contract.CustSpecPriceColumns.COLUMN_1));
        final String level1price = cursor.getString(cursor.getColumnIndex(Contract.CustSpecPriceColumns.COLUMN_LEVEL1PRICE));
        final String m2 = cursor.getString(cursor.getColumnIndex(Contract.CustSpecPriceColumns.COLUMN_2));
        final String level2price = cursor.getString(cursor.getColumnIndex(Contract.CustSpecPriceColumns.COLUMN_LEVEL2PRICE));
        final String m3 = cursor.getString(cursor.getColumnIndex(Contract.CustSpecPriceColumns.COLUMN_3));
        final String level3price = cursor.getString(cursor.getColumnIndex(Contract.CustSpecPriceColumns.COLUMN_LEVEL3PRICE));
        final String note1 = cursor.getString(cursor.getColumnIndex(Contract.CustSpecPriceColumns.COLUMN_NOTE_01));
        final String note2 = cursor.getString(cursor.getColumnIndex(Contract.CustSpecPriceColumns.COLUMN_NOTE_02));
        final String note3 = cursor.getString(cursor.getColumnIndex(Contract.CustSpecPriceColumns.COLUMN_NOTE_03));
        final String note4 = cursor.getString(cursor.getColumnIndex(Contract.CustSpecPriceColumns.COLUMN_NOTE_04));
        final String note5 = cursor.getString(cursor.getColumnIndex(Contract.CustSpecPriceColumns.COLUMN_NOTE_05));

        final CustSpecPrice custSpecPrice = new CustSpecPrice();
        custSpecPrice.setCustNum(custNum);
        custSpecPrice.setProdNum(prodNum);
        custSpecPrice.setPrice(price);
        custSpecPrice.setUnitPrice(unitPrice);
        custSpecPrice.setM1(m1);
        custSpecPrice.setLevel1Price(level1price);
        custSpecPrice.setM2(m2);
        custSpecPrice.setLevel2Price(level2price);
        custSpecPrice.setM3(m3);
        custSpecPrice.setLevel3Price(level3price);
        custSpecPrice.setNote01(note1);
        custSpecPrice.setNote02(note2);
        custSpecPrice.setNote03(note3);
        custSpecPrice.setNote04(note4);
        custSpecPrice.setNote05(note5);

        return custSpecPrice;
    }
}
