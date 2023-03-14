package com.example.product_sales_application.manager;

import android.content.Context;

public class CartManagerSingleton {
    private static CartManager sInstance;

    public static CartManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new CartManager(context);
        }
        return sInstance;
    }
}
