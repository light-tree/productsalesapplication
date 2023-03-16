package com.example.product_sales_application.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.product_sales_application.models.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static final String PREF_NAME = "cart_prefs";
    private static final String KEY_CART = "cart";

    private SharedPreferences mPrefs;
    private Gson mGson;

    public CartManager(Context context) {
        mPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mGson = new Gson();
    }

    public void saveCart(List<Product> cart) {
        String cartJson = mGson.toJson(cart);
        mPrefs.edit().putString(KEY_CART, cartJson).apply();
    }


    public List<Product> getCart() {
        String cartJson = mPrefs.getString(KEY_CART, null);
        if (cartJson != null) {
            Type type = new TypeToken<List<Product>>() {}.getType();
            return mGson.fromJson(cartJson, type);
        }
        return new ArrayList<>();
    }


}
