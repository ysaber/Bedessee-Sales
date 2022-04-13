package com.thebedesseegroup.sales.shoppingcart;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.thebedesseegroup.sales.R;
import com.thebedesseegroup.sales.order.GMailUtils;
import com.thebedesseegroup.sales.store.Statement;
import com.thebedesseegroup.sales.store.Store;
import com.thebedesseegroup.sales.store.StoreManager;
import com.thebedesseegroup.sales.utilities.Utilities;

/**
 * Dialog for shopping cart.
 */
public class ShoppingCartDialog extends Activity implements View.OnClickListener {

    final public static int REQUEST_CODE = 200;
    final public static int RESULT_CODE_CONTINUED = 199;
    final public static int RESULT_CODE_CHECKED_OUT = 198;

    final public static String KEY_SHOPPING_CART = "shopping_cart_key";

    ListView shoppingCartListView;
    
    private ShoppingCart mShoppingCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_shopping_cart);

        final Bundle extras = getIntent().getExtras();

        if(extras != null && extras.containsKey(KEY_SHOPPING_CART)) {
            mShoppingCart = (ShoppingCart) extras.getSerializable(KEY_SHOPPING_CART);
        } else {
            mShoppingCart = ShoppingCart.getCurrentShoppingCart();
        }

        shoppingCartListView = (ListView) findViewById(R.id.listView_shoppingCart);
        ShoppingCartAdapter shoppingCartAdapter = new ShoppingCartAdapter(this, R.layout.shopping_cart_list_item, R.id.textView_brand, mShoppingCart.getProducts());
        shoppingCartListView.setAdapter(shoppingCartAdapter);

        ((EditText) findViewById(R.id.edtComment)).setText(mShoppingCart.getComment());
        ((EditText) findViewById(R.id.edtContact)).setText(mShoppingCart.getContact());

        String storeName = StoreManager.getCurrentStore().getName();
        if (storeName != null) {
            ((TextView) findViewById(R.id.store_name)).setText(storeName);
        }
        
        findViewById(R.id.btnViewStatement).setOnClickListener(this);
        findViewById(R.id.btnCancel).setOnClickListener(this);
        findViewById(R.id.btnSave).setOnClickListener(this);
        findViewById(R.id.btnCheckout).setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();


        int[] dimens = Utilities.getScreenDimensInPx(null);
        getWindow().setLayout((int) (dimens[0] * .95), (int) (dimens[1] * .95));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnViewStatement:
                Store store = StoreManager.getCurrentStore();
                Statement.show(ShoppingCartDialog.this, Statement.DocType.STATEMENT, store.getStatementUrl());
                break;

            case R.id.btnCancel:
                setResult(RESULT_CODE_CONTINUED);
                finish();
                break;

            case R.id.btnSave:
                saveCommentAndContact();
                setResult(RESULT_CODE_CONTINUED);
                finish();
                break;

            case R.id.btnCheckout:

                saveCommentAndContact();
                ShoppingCart shoppingCart = mShoppingCart;
                shoppingCart.stopTimer();
                setResult(RESULT_CODE_CHECKED_OUT);
                GMailUtils.sendShoppingCart(this, shoppingCart);

                mShoppingCart.clearProducts();
                mShoppingCart.clearComment();
                mShoppingCart.clearContact();

                StoreManager.clearCurrentStore();

                finish();
                break;
            default:
                break;
        }
    }


    /**
     * Saves the comment and the contact that the user typed in.
     */
    private void saveCommentAndContact() {
        mShoppingCart.setComment((((EditText) findViewById(R.id.edtComment)).getText().toString()));
        mShoppingCart.setContact((((EditText) findViewById(R.id.edtContact)).getText().toString()));
    }

}