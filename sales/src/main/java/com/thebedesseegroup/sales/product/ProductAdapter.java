package com.thebedesseegroup.sales.product;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thebedesseegroup.sales.R;
import com.thebedesseegroup.sales.customview.QtySelector;
import com.thebedesseegroup.sales.main.SideMenu;
import com.thebedesseegroup.sales.mixpanel.MixPanelManager;
import com.thebedesseegroup.sales.provider.Contract;
import com.thebedesseegroup.sales.provider.ProviderUtils;
import com.thebedesseegroup.sales.store.StoreManager;
import com.thebedesseegroup.sales.utilities.BitmapWorkerTask;
import com.thebedesseegroup.sales.utilities.Utilities;

import java.util.ArrayList;


public class ProductAdapter extends CursorAdapter implements Filterable {

    private Context mContext;
    private ArrayList<SideMenu> sideMenus = new ArrayList<>();

    public boolean isBusy = false;

    int [] dimens = Utilities.getScreenDimensInPx(null);

    int width = dimens[0]/12;
    int height = dimens[1]/6;

    public ProductAdapter(final Context context) {
        super(context, null, false);
        mContext = context;

        final Cursor cursorSideMenu = mContext.getContentResolver().query(Contract.SideMenu.CONTENT_URI, null, null, null, Contract.SideMenuColumns.COLUMN_SORT + " ASC");
        while (cursorSideMenu.moveToNext()) {
            sideMenus.add(ProviderUtils.cursorToSideMenu(cursorSideMenu));
        }
        cursorSideMenu.close();


    }



    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        FilterQueryProvider filter = getFilterQueryProvider();
        if (filter != null) {
            return filter.runQuery(constraint);
        }

        return mContext.getContentResolver().query(Contract.Product.CONTENT_URI, null, constraint.toString(), null, Contract.ProductColumns.COLUMN_BRAND + " ASC");
    }

    /**
     * Change the underlying cursor to a new cursor. If there is an existing cursor it will be
     * closed.
     *
     * @param cursor The new cursor to be used
     */
    @Override
    public void changeCursor(Cursor cursor) {
        swapCursor(cursor);
    }

    @Override
    public Product getItem(int position) {
        final Cursor cursor = getCursor();
        cursor.moveToPosition(position);
        return ProviderUtils.cursorToProduct(cursor);
    }


    /**
     * Makes a new view to hold the data pointed to by cursor.
     *
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view;
        ViewHolder viewHolder = new ViewHolder();

        view = LayoutInflater.from(context).inflate(R.layout.product_grid_item, parent, false);

//        if (view == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(mIsList ? R.layout.product_list_item : R.layout.product_grid_item, null);

        viewHolder.mItemBody = (RelativeLayout) view.findViewById(R.id.item_body);
        viewHolder.mImageView = (ImageView) view.findViewById(R.id.imageView);
        viewHolder.mTextViewBrand = (TextView) view.findViewById(R.id.textView_brand);
        viewHolder.mTextViewDescription = (TextView) view.findViewById(R.id.textView_description);
        viewHolder.mTextUom = (TextView) view.findViewById(R.id.textView_uom);
        viewHolder.mTextPrice = (TextView) view.findViewById(R.id.textView_price);
        viewHolder.mTextUomUnit = (TextView) view.findViewById(R.id.textView_unit_uom);
        viewHolder.mTextPriceUnit = (TextView) view.findViewById(R.id.textView_unit_price);
        viewHolder.mBtnAddToCart = (Button) view.findViewById(R.id.btnAddToCart);
        viewHolder.mQtySelector = (QtySelector) view.findViewById(R.id.list_item_qty_selector);

        view.setTag(viewHolder);
//        }

        return view;
    }

    /**
     * Bind an existing view to the data pointed to by cursor
     *
     * @param view    Existing view, returned earlier by newView
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        final Product product = ProviderUtils.cursorToProduct(cursor);
        final ViewHolder holder = (ViewHolder) view.getTag();

        final String filePath = product.getImagePath();


        if(!isBusy) {

            View.OnClickListener quickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (StoreManager.isStoreSelected()) {
                        MixPanelManager.trackButtonClick(context, "Button Click: view product");
                        ProductDetailsDialog.show(mContext, product);
                    } else {
                        Toast.makeText(mContext, "Please select store to continue.", Toast.LENGTH_SHORT).show();
                    }
                }
            };
            holder.mImageView.setOnClickListener(quickListener);
            holder.mTextViewBrand.setOnClickListener(quickListener);
            holder.mTextViewDescription.setOnClickListener(quickListener);

            BitmapWorkerTask task = new BitmapWorkerTask(mContext, holder.mImageView, new int[]{width, height});
            task.execute(filePath);
            holder.mImageView.setLongClickable(true);
            holder.mImageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Utilities.shareImage(context, "file://" + filePath);
                    return true;
                }
            });
        } else {
            holder.mImageView.setImageBitmap(null);
        }

        String statusCode = product.getStatusCode();

        for(SideMenu menuSideMenu : sideMenus) {
            if(menuSideMenu.getStatusCode().equals(statusCode)) {
                holder.mTextViewBrand.setTextColor(Color.parseColor(menuSideMenu.getColour()));
                holder.mTextViewDescription.setTextColor(Color.parseColor(menuSideMenu.getColour()));
            }
        }

//        if(statusCode != null) {
//            if (statusCode.equals(Status.LOWQ.name())) {
//                holder.mTextViewBrand.setTextColor(Color.BLUE);
//                holder.mTextViewDescription.setTextColor(Color.BLUE);
//            } else if (statusCode.equals(Status.LOW.name())) {
//                holder.mTextViewBrand.setTextColor(Color.RED);
//                holder.mTextViewDescription.setTextColor(Color.RED);
//            } else if (statusCode.equals(Status.NEW.name())) {
//                holder.mTextViewBrand.setTextColor(Color.parseColor("#336600"));
//                holder.mTextViewDescription.setTextColor(Color.parseColor("#336600"));
//            } else if (statusCode.equals(Status.SOAP.name())) {
//                holder.mTextViewBrand.setTextColor(Color.parseColor("#CC6600"));
//                holder.mTextViewDescription.setTextColor(Color.parseColor("#CC6600"));
//            } else if (statusCode.equals(Status.REST.name())) {
//                holder.mTextViewBrand.setTextColor(Color.MAGENTA);
//                holder.mTextViewDescription.setTextColor(Color.MAGENTA );
//            } else if (statusCode.equals(Status.SOON.name())) {
//                holder.mTextViewBrand.setTextColor(Color.parseColor("#401616"));
//                holder.mTextViewDescription.setTextColor(Color.parseColor("#401616") );
//            } else if (statusCode.equals(Status.SPEC.name())) {
//                holder.mTextViewBrand.setTextColor(Color.parseColor("#660066"));
//                holder.mTextViewDescription.setTextColor(Color.parseColor("#660066") );
//            } else if (statusCode.equals(Status.LAST.name())) {
//                holder.mTextViewBrand.setTextColor(Color.parseColor("#CCCC00"));
//                holder.mTextViewDescription.setTextColor(Color.parseColor("#CCCC00") );
//            }
//
//        }

        holder.mTextViewBrand.setText(product.getBrand());
        holder.mTextViewDescription.setText(product.getDescription());

        holder.mTextUom.setText(product.getCaseUom());
        holder.mTextPrice.setText("$" + product.getCasePrice());
        holder.mTextUomUnit.setText(product.getPieceUom());
        holder.mTextPriceUnit.setText("$" + product.getPiecePrice());

        if(!isBusy) {

            holder.mBtnAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(StoreManager.isStoreSelected()) {


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


                                    if (holder.mQtySelector.getSelectedQty() > 0) {
                                        Utilities.updateShoppingCart(mContext, product, holder.mQtySelector.getSelectedQty(), newPrice, holder.mQtySelector.getItemType(), new Utilities.OnProductUpdatedListener() {
                                            @Override
                                            public void onUpdated(int qty, QtySelector.ItemType itemType) {
                                                holder.mQtySelector.setQty(0);
                                                holder.mQtySelector.invalidate();
                                            }

                                        });

                                    } else {
                                        Toast.makeText(mContext, "Please provide quantity before adding a product.", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        } else {

                            if (holder.mQtySelector.getSelectedQty() > 0) {
                                Utilities.updateShoppingCart(mContext, product, holder.mQtySelector.getSelectedQty(), null, holder.mQtySelector.getItemType(), new Utilities.OnProductUpdatedListener() {
                                    @Override
                                    public void onUpdated(int qty, QtySelector.ItemType itemType) {
                                        holder.mQtySelector.setQty(0);
                                        holder.mQtySelector.invalidate();
                                    }

                                });

                            } else {
                                Toast.makeText(mContext, "Please provide quantity before adding a product.", Toast.LENGTH_SHORT).show();
                            }

                        }

                    } else {
                        Toast.makeText(mContext, "Please select store to continue.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.mItemBody.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (StoreManager.isStoreSelected()) {
                        ProductDetailsDialog.show(mContext, product);
                    } else {
                        Toast.makeText(mContext, "Please select store to continue.", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            QtySelector.QtySelectorClickListener qtySelectorClickListener = new QtySelector.QtySelectorClickListener() {
                @Override
                public View.OnClickListener onPlusButtonClick() {
                    return new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (StoreManager.isStoreSelected()) {
                                holder.mQtySelector.incrementQty();
//                                mTempQty.set(position, holder.mQtySelector.getSelectedQty());
                            } else {
                                Toast.makeText(mContext, "Please select store to continue.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    };

                }

                @Override
                public View.OnClickListener onMinusButtonClick() {
                    return new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (StoreManager.isStoreSelected()) {
                                holder.mQtySelector.decrementQty();
//                                mTempQty.set(position, holder.mQtySelector.getSelectedQty());
                            } else {
                                Toast.makeText(mContext, "Please select store to continue.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    };
                }
            };

            holder.mQtySelector.setQtySelectorClickListener(qtySelectorClickListener);

            holder.mQtySelector.setQty(0);
//            holder.mQtySelector.setQty(mTempQty.get(position));
            holder.mQtySelector.setProduct(product);
        }


    }





    static class ViewHolder {
        RelativeLayout mItemBody;
        ImageView mImageView;
        TextView mTextViewBrand;
        TextView mTextViewDescription;
        TextView mTextUom;
        TextView mTextPrice;
        TextView mTextUomUnit;
        TextView mTextPriceUnit;
        Button mBtnAddToCart;
        QtySelector mQtySelector;
    }

}