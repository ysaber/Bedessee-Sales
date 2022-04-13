package com.thebedesseegroup.sales.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.thebedesseegroup.sales.R;
import com.thebedesseegroup.sales.product.Product;
import com.thebedesseegroup.sales.store.StoreManager;
import com.thebedesseegroup.sales.utilities.Utilities;

import java.io.Serializable;

/**
 * Quantity selector for products.
 */
public class QtySelector extends LinearLayout {

    private int mQty;
    private View parentView;
    private Product mProduct;

    public enum ItemType implements Serializable {
        CASE, PIECE
    }

    public QtySelector(Context context) {
        super(context);
        init(context);
    }

    public QtySelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public QtySelector(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void setProduct(Product product) {
        mProduct = product;
    }

    public void setQtySelectorClickListener(QtySelectorClickListener listener) {
        parentView.findViewById(R.id.btnPlus).setOnClickListener(listener.onPlusButtonClick());
        parentView.findViewById(R.id.btnMinus).setOnClickListener(listener.onMinusButtonClick());
    }

    @SuppressLint("InflateParams")
    private void init(final Context context) {
        addView(parentView = LayoutInflater.from(context).inflate(R.layout.view_qty_selector, null));

        final EditText editText = (EditText) findViewById(R.id.editText);

        editText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(StoreManager.isStoreSelected()) {

                    final NumberPad.OnNumberSelectedListener listener = new NumberPad.OnNumberSelectedListener() {
                        @Override
                        public void onSelected(int number) {
                            editText.setText(String.valueOf(number));
                            mQty = number;
                        }

                        @Override
                        public void onSelected(ItemType itemType, int qty) {
                            Utilities.updateShoppingCart(getContext(), mProduct, qty, null, itemType, null);
                        }
                    };


                    new NumberPad(context, listener, true);
                } else {
                    Toast.makeText(context, "Please select store to continue.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public int getSelectedQty() {
        return mQty;
    }

    public void setQty(int qty) {
        ((EditText) parentView.findViewById(R.id.editText)).setText(String.valueOf(mQty = qty));
    }


    public void incrementQty() {
        ((EditText) parentView.findViewById(R.id.editText)).setText(String.valueOf(++mQty));
    }

    public void decrementQty() {
        if(mQty > 0) {
            ((EditText) parentView.findViewById(R.id.editText)).setText(String.valueOf(--mQty));
        }
    }

    public ItemType getItemType() {
        return ((RadioButton)parentView.findViewById(R.id.radioCase)).isChecked() ? ItemType.CASE : ItemType.PIECE;
    }


    public interface QtySelectorClickListener {
        public OnClickListener onPlusButtonClick();
        public OnClickListener onMinusButtonClick();
    }
}
