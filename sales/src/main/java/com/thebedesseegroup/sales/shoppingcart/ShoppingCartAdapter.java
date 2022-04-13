package com.thebedesseegroup.sales.shoppingcart;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.thebedesseegroup.sales.R;
import com.thebedesseegroup.sales.customview.NumberPad;
import com.thebedesseegroup.sales.customview.QtySelector;
import com.thebedesseegroup.sales.product.Product;
import com.thebedesseegroup.sales.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartAdapter extends ArrayAdapter {

    private List<ShoppingCartProduct> mShoppingCartProducts;
    private Context mContext;


    public ShoppingCartAdapter(Context context, int resource, int textViewResourceId, List<ShoppingCartProduct> shoppingCartProducts) {
        super(context, resource, textViewResourceId);
        mContext = context;
        mShoppingCartProducts = shoppingCartProducts;
    }

    @Override
    public ShoppingCartProduct getItem(int position) {
        return mShoppingCartProducts.get(position);
    }

    @Override
    public int getCount() {
        return mShoppingCartProducts.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.shopping_cart_list_item, parent, false);

            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.radioPiece = (RadioButton) convertView.findViewById(R.id.radioPiece);
            viewHolder.radioCase = (RadioButton) convertView.findViewById(R.id.radioCase);
            viewHolder.edtQty = (EditText) convertView.findViewById(R.id.edtQty);
            viewHolder.removeItem = (ImageButton) convertView.findViewById(R.id.btnRemoveItem);
            viewHolder.brand = (TextView) convertView.findViewById(R.id.textView_brand);
            viewHolder.description = (TextView) convertView.findViewById(R.id.textView_description);

            convertView.setTag(viewHolder);
        }

        final ViewHolder holder = (ViewHolder) convertView.getTag();

        final ShoppingCartProduct shoppingCartProduct = getItem(position);

        final Product product = shoppingCartProduct.getProduct();

        holder.radioPiece.setChecked(shoppingCartProduct.getItemType() == QtySelector.ItemType.PIECE);
        holder.radioCase.setChecked(shoppingCartProduct.getItemType() == QtySelector.ItemType.CASE);

        holder.brand.setText(product.getBrand());

        final String price = shoppingCartProduct.getEnteredPrice();
        final boolean hidePrice = TextUtils.isEmpty(price) || price.equalsIgnoreCase("null");
        holder.description.setText(product.getDescription() + " ~ " + product.getCaseUom() + " ~ " + product.getNumber() + (hidePrice ? "" : " price: " + shoppingCartProduct.getEnteredPrice()));

        holder.edtQty.setText(String.valueOf(shoppingCartProduct.getQuantity()));

        holder.removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<ShoppingCartProduct> products = ShoppingCart.getCurrentShoppingCart().getProducts();
                final int index = products.indexOf(shoppingCartProduct);

                Utilities.removeProductFromShoppingCart(mContext, products.get(index).getProduct(), new Utilities.OnProductUpdatedListener() {
                    @Override
                    public void onUpdated(int qty, QtySelector.ItemType itemType) {
                        ShoppingCart.getCurrentShoppingCart().getProducts().remove(shoppingCartProduct);
                        notifyDataSetChanged();
                    }
                });
            }
        });

        holder.edtQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final NumberPad.OnNumberSelectedListener listener = new NumberPad.OnNumberSelectedListener() {
                    @Override
                    public void onSelected(int number) {

                    }

                    @Override
                    public void onSelected(final QtySelector.ItemType itemType, final int qty) {
                        final ArrayList<ShoppingCartProduct> products = ShoppingCart.getCurrentShoppingCart().getProducts();
                        final int index = products.indexOf(shoppingCartProduct);

                        Utilities.updateShoppingCart(mContext, products.get(index).getProduct(), qty, null, itemType, new Utilities.OnProductUpdatedListener() {
                            @Override
                            public void onUpdated(int newQty, QtySelector.ItemType newItemType) {
                                products.get(index).setQuantity(newQty);
                                products.get(index).setItemType(newItemType);
                                notifyDataSetChanged();
                            }
                        });
                    }
                };

                new NumberPad(mContext, listener, true);
            }
        });

        return convertView;
    }

    /**
     * Class for ViewHolder pattern.
     */
    private class ViewHolder {
        public RadioButton radioPiece;
        public RadioButton radioCase;
        public TextView brand;
        public TextView description;
        public EditText edtQty;
        public ImageButton removeItem;
    }
}
