package com.thebedesseegroup.sales.product;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thebedesseegroup.sales.R;
import com.thebedesseegroup.sales.customview.QtySelector;
import com.thebedesseegroup.sales.main.MainActivity;
import com.thebedesseegroup.sales.mixpanel.MixPanelManager;
import com.thebedesseegroup.sales.utilities.BitmapWorkerTask;
import com.thebedesseegroup.sales.utilities.Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Dialog to display a series of special products in a {@link com.thebedesseegroup.sales.product.HorizontalList}
 */
public class SpecialProductDialog {



    public static void show(final Context context, final String title, final List<Product> products) {

        int [] dimens = Utilities.getScreenDimensInPx(null);

        int width = dimens[0]/3;
        int height = dimens[1]/3;

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final View mainView = LayoutInflater.from(context).inflate(R.layout.dialog_special_product_details, null);

        ((TextView)mainView.findViewById(R.id.title)).setText(title + (title.contains("PRODUCT") ? "" : " PRODUCTS"));
        mainView.findViewById(R.id.imageView_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mProductDetailsAlertDialog.dismiss();
            }
        });

        final HorizontalList horizontalList = (HorizontalList) mainView.findViewById(R.id.horizontal_list);

        final ArrayList<View> listOfProducts = new ArrayList<>(products.size());

        builder.setView(mainView);

        for (final Product product : products) {

            final View v = LayoutInflater.from(context).inflate(R.layout.widget_product_detail, null, false);

            final File file = new File(product.getImagePath());

            final ImageView imageView = (ImageView) v.findViewById(R.id.imageView);

            final BitmapWorkerTask task = new BitmapWorkerTask(context, imageView, new int[]{width, height});
            task.execute(file.getAbsolutePath());

            v.findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utilities.shareImage(context, "file://" + file.getAbsolutePath());
                }
            });

            v.findViewById(R.id.btn_zoom).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LargeView.launch(context, Uri.parse("file://" + file.getAbsolutePath()));
                }
            });


            ((TextView)v.findViewById(R.id.textView_brand)).setText(product.getBrand() + product.getDescription());

            ((TextView)v.findViewById(R.id.textView_price)).setText(product.getCaseUom() + ": $" + product.getCasePrice());

            final String level1price = product.getLevel1Price();
            ((TextView)v.findViewById(R.id.textView_level1price)).setText("Level 1 Price: " + (level1price.equals("0.00") ? "N/A" : "$" + level1price));

            final String level2price = product.getLevel2Price();
            ((TextView)v.findViewById(R.id.textView_level2price)).setText("Level 2 Price: " + (level2price.equals("0.00") ? "N/A" : "$" + level2price));

            final String level3price = product.getLevel3Price();
            ((TextView)v.findViewById(R.id.textView_level3price)).setText("Level 3 Price: " + (level3price.equals("0.00") ? "N/A" : "$" + level3price));

            ((TextView)v.findViewById(R.id.textView_unitprice)).setText(product.getPieceUom() + ": $" + (product.getPiecePrice()));

            ((TextView)v.findViewById(R.id.textView_status)).setText("Status: " + product.getStatusDescription());

            ((TextView)v.findViewById(R.id.textView_casesperskid)).setText("Cases Per Skid: " + product.getCasesPerSkid());
            ((TextView)v.findViewById(R.id.textView_casesperrow)).setText("Cases Per Row: " + product.getCasesPerRow());
            ((TextView)v.findViewById(R.id.textView_layersperskid)).setText("Layers Per Skid: " + product.getLayersPerSkid());
            ((TextView)v.findViewById(R.id.textView_totalqty)).setText("Total Qty: " + product.getTotalQty());

            final QtySelector qtySelector = (QtySelector) v.findViewById(R.id.qty_selector);

            final Button btnAddToCart = (Button) v.findViewById(R.id.btnAddToCart);


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

            final String note01 = product.getNote01();
            final String note02 = product.getNote02();
            final String note03 = product.getNote03();
            final String note04 = product.getNote04();
            final String note05 = product.getNote05();

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

            qtySelector.setQtySelectorClickListener(qtySelectorClickListener);
            qtySelector.setProduct(product);


            btnAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



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


                                if (qtySelector.getSelectedQty() > 0) {
                                    Utilities.updateShoppingCart(context, product, qtySelector.getSelectedQty(), newPrice, qtySelector.getItemType(), new Utilities.OnProductUpdatedListener() {
                                        @Override
                                        public void onUpdated(int qty, QtySelector.ItemType itemType) {
                                            qtySelector.setQty(0);
                                            qtySelector.invalidate();
                                        }

                                    });

                                } else {
                                    Toast.makeText(context, "Please provide quantity before adding a product.", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    } else {

                        if (qtySelector.getSelectedQty() > 0) {
                            Utilities.updateShoppingCart(context, product, qtySelector.getSelectedQty(), null, qtySelector.getItemType(), new Utilities.OnProductUpdatedListener() {
                                @Override
                                public void onUpdated(int qty, QtySelector.ItemType itemType) {
                                    qtySelector.setQty(0);
                                    qtySelector.invalidate();
                                }

                            });

                        } else {
                            Toast.makeText(context, "Please provide quantity before adding a product.", Toast.LENGTH_SHORT).show();
                        }

                    }
                    

                }
            });

            listOfProducts.add(v);
        }

        horizontalList.setListOfViews(listOfProducts);

        if(MainActivity.mProductDetailsAlertDialog != null) {
            if (MainActivity.mProductDetailsAlertDialog.isShowing()) {
                MainActivity.mProductDetailsAlertDialog.dismiss();
            }
        }

        MainActivity.mProductDetailsAlertDialog = builder.create();

        MainActivity.mProductDetailsAlertDialog.setCancelable(true);

        MainActivity.mProductDetailsAlertDialog.show();

        MainActivity.mProductDetailsAlertDialog.getWindow().setLayout((int)(dimens[0]*.95), (int)(dimens[1]*.95));
    }

}
