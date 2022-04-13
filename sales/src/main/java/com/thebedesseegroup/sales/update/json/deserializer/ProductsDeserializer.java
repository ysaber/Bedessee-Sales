package com.thebedesseegroup.sales.update.json.deserializer;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.thebedesseegroup.sales.product.Product;
import com.thebedesseegroup.sales.sharedprefs.SharedPrefsManager;

import java.lang.reflect.Type;


public class ProductsDeserializer implements JsonDeserializer<Product> {

    private Context mContext;

    public ProductsDeserializer(Context context) {
        mContext = context;
    }

    @Override
    public Product deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        final SharedPrefsManager sharedPrefs = new SharedPrefsManager(mContext);
        final String mSugarSyncDir = sharedPrefs.getSugarSyncDir();

        final Product product = new Gson().fromJson(json.toString(), Product.class);
        product.setImagePath(mSugarSyncDir + "/prod/" + product.getNumber() + ".jpg");

        return product;
    }

}