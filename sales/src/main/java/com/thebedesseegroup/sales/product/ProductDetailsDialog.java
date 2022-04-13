package com.thebedesseegroup.sales.product;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thebedesseegroup.sales.R;
import com.thebedesseegroup.sales.customview.QtySelector;
import com.thebedesseegroup.sales.main.MainActivity;
import com.thebedesseegroup.sales.mixpanel.MixPanelManager;
import com.thebedesseegroup.sales.provider.Contract;
import com.thebedesseegroup.sales.provider.ProviderUtils;
import com.thebedesseegroup.sales.sharedprefs.SharedPrefsManager;
import com.thebedesseegroup.sales.store.StoreManager;
import com.thebedesseegroup.sales.utilities.BitmapWorkerTask;
import com.thebedesseegroup.sales.utilities.Utilities;

import java.io.File;
import java.util.Arrays;

/**
 * Dialog showing product details and similar products.
 */
public class ProductDetailsDialog {

    public static void show(final Context context, final Product product) {

        int [] dimens = Utilities.getScreenDimensInPx(null);

        MixPanelManager.trackProductView(context, product.getBrand() + " " + product.getDescription());

        final String currCustNum = StoreManager.getCurrentStore().getNumber();
        final String productNum = product.getNumber();

        CustSpecPrice custSpecPrice = null;

        final Cursor custSpecPriceCursor = context.getContentResolver().query(Contract.CustSpecPrice.CONTENT_URI, null, Contract.CustSpecPriceColumns.COLUMN_CUST_NUM + " = ? AND " + Contract.CustSpecPriceColumns.COLUMN_PROD_NUM + " = ?", new String[]{currCustNum, productNum}, null);
        if (custSpecPriceCursor.moveToFirst()) {
            custSpecPrice = ProviderUtils.cursorToCustSpecPrice(custSpecPriceCursor);

        }
        custSpecPriceCursor.close();

        int width = dimens[0]/3;
        int height = dimens[1]/3;

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final View v = LayoutInflater.from(context).inflate(R.layout.dialog_product_details, null, false);

        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        final float imageDimValue = Float.parseFloat(new SharedPrefsManager(context).getFadePercentage());

        v.findViewById(R.id.background_dimmer).setAlpha(imageDimValue / 100f);

        if (custSpecPrice != null) {
            v.findViewById(R.id.textView_special_available).setVisibility(View.VISIBLE);
        }


        builder.setView(v);

        final File f = new File(product.getImagePath());

        final ImageView imageView = (ImageView) v.findViewById(R.id.imageView);

        final BitmapWorkerTask task = new BitmapWorkerTask(context, imageView, new int[]{width, height});
        task.execute(f.getAbsolutePath());

        v.findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MixPanelManager.trackButtonClick(context, "Button Click: Image Share");
                Utilities.shareImage(context, "file://" + f.getAbsolutePath());
            }
        });

        v.findViewById(R.id.btn_zoom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MixPanelManager.trackButtonClick(context, "Button Click: Image Zoo ");
                LargeView.launch(context, Uri.parse("file://" + f.getAbsolutePath()));
            }
        });

        ((TextView)v.findViewById(R.id.textView_brand)).setText(product.getBrand() + " " + product.getDescription());
        ((TextView)v.findViewById(R.id.textView_uom)).setText("Unit: " + product.getPieceUom());

        final String casePrice = (custSpecPrice == null || TextUtils.isEmpty(custSpecPrice.getPrice())) ? product.getCasePrice() : custSpecPrice.getPrice();
        ((TextView)v.findViewById(R.id.textView_price)).setText(product.getCaseUom() + ": $" + casePrice);

        if(sharedPrefs.getBoolean("pref_show_level1price", true)) {
//            final String level1price = product.getLevel1Price();
            final String level1price = (custSpecPrice == null || TextUtils.isEmpty(custSpecPrice.getLevel1Price())) ? product.getLevel1Price() : custSpecPrice.getLevel1Price();
            ((TextView) v.findViewById(R.id.textView_level1price)).setText("Level 2 Price: " + (level1price.equals("0.00") ? "N/A" : "$" + level1price));
        } else {
            v.findViewById(R.id.textView_level1price).setVisibility(View.GONE);
        }

        if(sharedPrefs.getBoolean("pref_show_level2price", true)) {
//            final String level2price = product.getLevel2Price();
            final String level2price = (custSpecPrice == null || TextUtils.isEmpty(custSpecPrice.getLevel2Price())) ? product.getLevel2Price() : custSpecPrice.getLevel2Price();
            ((TextView)v.findViewById(R.id.textView_level2price)).setText("Level 3 Price: " + (level2price.equals("0.00") ? "N/A" : "$" + level2price));
        } else {
            v.findViewById(R.id.textView_level2price).setVisibility(View.GONE);
        }

        if(sharedPrefs.getBoolean("pref_show_level3price", true)) {
//            final String level3price = product.getLevel3Price();
            final String level3price = (custSpecPrice == null || TextUtils.isEmpty(custSpecPrice.getLevel3Price())) ? product.getLevel3Price() : custSpecPrice.getLevel3Price();
            ((TextView)v.findViewById(R.id.textView_level3price)).setText("Level 4 Price: " + (level3price.equals("0.00") ? "N/A" : "$" + level3price));
        } else {
            v.findViewById(R.id.textView_level3price).setVisibility(View.GONE);
        }
        final String unitPrice = (custSpecPrice == null || TextUtils.isEmpty(custSpecPrice.getUnitPrice())) ? product.getPiecePrice() : custSpecPrice.getUnitPrice();
        ((TextView)v.findViewById(R.id.textView_unitprice)).setText(product.getPieceUom() + ": $" + unitPrice);

        ((TextView)v.findViewById(R.id.textView_status)).setText("Status: " + product.getStatusDescription());

        ((TextView)v.findViewById(R.id.textView_casesperskid)).setText("Cases Per Skid: " + product.getCasesPerSkid());
        ((TextView)v.findViewById(R.id.textView_casesperrow)).setText("Cases Per Row: " + product.getCasesPerRow());
        ((TextView)v.findViewById(R.id.textView_layersperskid)).setText("Layers Per Skid: " + product.getLayersPerSkid());
        ((TextView)v.findViewById(R.id.textView_totalqty)).setText("Total Qty: " + product.getTotalQty());

        final String showQty1 = product.getShowQty1();
        if (TextUtils.isEmpty(showQty1)) {
            v.findViewById(R.id.textView_qty1).setVisibility(View.GONE);
        } else {
            ((TextView) v.findViewById(R.id.textView_qty1)).setText(product.getShowQty1() + ": " + product.getQty1());
        }

        final String showQy2 = product.getShowQty2();
        if (TextUtils.isEmpty(showQy2)) {
            v.findViewById(R.id.textView_qty2).setVisibility(View.GONE);
        } else {
            ((TextView) v.findViewById(R.id.textView_qty2)).setText(product.getShowQty2() + ": " + product.getQty2());
        }

        final String showQty3 = product.getShowQty3();
        if (TextUtils.isEmpty(showQty3)) {
            v.findViewById(R.id.textView_qty3).setVisibility(View.GONE);
        } else {
            ((TextView) v.findViewById(R.id.textView_qty3)).setText(product.getShowQty3() + ": " + product.getQty3());
        }

        final String showQty4 = product.getShowQty4();
        if (TextUtils.isEmpty(showQty4)) {
            v.findViewById(R.id.textView_qty4).setVisibility(View.GONE);
        } else {
            ((TextView) v.findViewById(R.id.textView_qty4)).setText(product.getShowQty4() + ": " + product.getQty4());
        }

        final String note01 = (custSpecPrice == null || TextUtils.isEmpty(custSpecPrice.getNote01())) ? product.getNote01() : custSpecPrice.getNote01();
        final String note02 = (custSpecPrice == null || TextUtils.isEmpty(custSpecPrice.getNote02())) ? product.getNote02() : custSpecPrice.getNote02();
        final String note03 = (custSpecPrice == null || TextUtils.isEmpty(custSpecPrice.getNote03())) ? product.getNote03() : custSpecPrice.getNote03();
        final String note04 = (custSpecPrice == null || TextUtils.isEmpty(custSpecPrice.getNote04())) ? product.getNote04() : custSpecPrice.getNote04();
        final String note05 = (custSpecPrice == null || TextUtils.isEmpty(custSpecPrice.getNote05())) ? product.getNote05() : custSpecPrice.getNote05();

        if (TextUtils.isEmpty(note01)) {
            v.findViewById(R.id.textView_note01).setVisibility(View.GONE);
        } else {
            ((TextView) v.findViewById(R.id.textView_note01)).setText(note01);
        }

        if (TextUtils.isEmpty(note02)) {
            v.findViewById(R.id.textView_note02).setVisibility(View.GONE);
        } else {
            ((TextView) v.findViewById(R.id.textView_note02)).setText(note02);
        }

        if (TextUtils.isEmpty(note03)) {
            v.findViewById(R.id.textView_note03).setVisibility(View.GONE);
        } else {
            ((TextView) v.findViewById(R.id.textView_note03)).setText(note03);
        }

        if (TextUtils.isEmpty(note04)) {
            v.findViewById(R.id.textView_note04).setVisibility(View.GONE);
        } else {
            ((TextView) v.findViewById(R.id.textView_note04)).setText(note04);
        }

        if (TextUtils.isEmpty(note05)) {
            v.findViewById(R.id.textView_note05).setVisibility(View.GONE);
        } else {
            ((TextView) v.findViewById(R.id.textView_note05)).setText(note05);
        }


        final boolean isNewMatchLogic = new SharedPrefsManager(context).getUseNewLikeLogic().equals("YES");
        ((TextView) v.findViewById(R.id.textView_match_type)).setText("Match code: " + (isNewMatchLogic ? product.getLikeTag() + " (LIKE MATCH ON)" : "(LIKE MATCH OFF"));


        final GridView gridViewSimilarProducts = (GridView) v.findViewById(R.id.horizontalScrollView_similarProducts);

        final String [] searchStrings = product.getDescription().split(" ");

        final ProductAdapter productAdapter = new ProductAdapter(context);

        gridViewSimilarProducts.setAdapter(productAdapter);

        final LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {

                String searchString = "";

                final String likeTag = product.getLikeTag();

                if (new SharedPrefsManager(context).getUseNewLikeLogic().equals("YES") && !TextUtils.isEmpty(likeTag)) {
//                    ((TextView)v.findViewById(R.id.like_label)).setText("SIMILAR PRODUCTS\n(" + product.getLikeTag() + ") [LIKE MATCH ON]");
                    searchString = searchString + Contract.ProductColumns.COLUMN_LIKE_TAG + " = '" + product.getLikeTag() + "'";
                } else {
//                    ((TextView)v.findViewById(R.id.like_label)).setText("SIMILAR PRODUCTS\n[LIKE MATCH OFF]");
                    for (int i = 0; i < searchStrings.length; i++) {
                        searchString = searchString + Contract.ProductColumns.COLUMN_DESCRIPTION + " like '%" + searchStrings[i] + "%'" + (i == searchStrings.length - 1 ? "" : " OR ");
                    }
                }
                return new CursorLoader(context, Contract.Product.CONTENT_URI, null, searchString, null, null);

            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                productAdapter.changeCursor(cursor);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                productAdapter.changeCursor(null);
            }
        };

        ((Activity)context).getLoaderManager().initLoader(Arrays.hashCode(searchStrings), null, loaderCallbacks);

        final QtySelector qtySelector = (QtySelector) v.findViewById(R.id.qty_selector);

        final Button btnAddToCart = (Button) v.findViewById(R.id.btnAddToCart);

        QtySelector.QtySelectorClickListener qtySelectorClickListener = new QtySelector.QtySelectorClickListener() {
            @Override
            public View.OnClickListener onPlusButtonClick() {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        qtySelector.incrementQty();
                    }
                };
            }

            @Override
            public View.OnClickListener onMinusButtonClick() {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        qtySelector.decrementQty();
                    }
                };
            }
        };

        qtySelector.setProduct(product);
        qtySelector.setQtySelectorClickListener(qtySelectorClickListener);


        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (qtySelector.getSelectedQty() > 0) {

                    MixPanelManager.trackButtonClick(context, "Button Click: Add to cart");

                    if (product.getPopUpPriceFlag().equalsIgnoreCase("Y")) {

                        AlertDialog.Builder qtyBuilder = new AlertDialog.Builder(context);
                        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_product_qty, null, false);

                        ((EditText) view.findViewById(R.id.edt_price)).setText(product.getPopUpPrice());

                        qtyBuilder.setView(view);

                        final AlertDialog dialog = qtyBuilder.create();

                        dialog.show();

                        view.findViewById(R.id.btn_done).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.hide();

                                String newPrice = ((EditText) view.findViewById(R.id.edt_price)).getText().toString();

                                Utilities.updateShoppingCart(context, product, qtySelector.getSelectedQty(), newPrice, qtySelector.getItemType(), new Utilities.OnProductUpdatedListener() {
                                    @Override
                                    public void onUpdated(int qty, QtySelector.ItemType itemType) {
                                        qtySelector.setQty(0);
                                    }
                                });

                            }
                        });

                    } else {

                        Utilities.updateShoppingCart(context, product, qtySelector.getSelectedQty(), null, qtySelector.getItemType(), new Utilities.OnProductUpdatedListener() {
                            @Override
                            public void onUpdated(int qty, QtySelector.ItemType itemType) {
                                qtySelector.setQty(0);
                            }
                        });
                    }

                } else {
                    MixPanelManager.trackButtonClick(context, "Button Click: Add to cart: ZERO QTY");
                    Toast.makeText(context, "Please provide quantity before adding a product.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(MainActivity.mProductDetailsAlertDialog != null) {
            if (MainActivity.mProductDetailsAlertDialog.isShowing()) {
                MainActivity.mProductDetailsAlertDialog.dismiss();
            }
        }

        MainActivity.mProductDetailsAlertDialog = builder.create();

        v.findViewById(R.id.imageView_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MixPanelManager.trackButtonClick(context, "Button Click: Close popup");
                MainActivity.mProductDetailsAlertDialog.hide();
            }
        });

        MainActivity.mProductDetailsAlertDialog.show();

        MainActivity.mProductDetailsAlertDialog.getWindow().setLayout((int) (dimens[0] * .95), (int) (dimens[1] * .95));

    }
}
