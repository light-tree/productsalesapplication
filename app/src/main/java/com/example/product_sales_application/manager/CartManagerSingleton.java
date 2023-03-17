package com.example.product_sales_application.manager;

import android.content.Context;

public class CartManagerSingleton {
    private static CartManager sInstance;

    private static AccountManager accountManagerInstance;

    public static CartManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new CartManager(context);
        }
        return sInstance;
    }

    public static AccountManager getAccountManagerInstance(Context context){
        if (accountManagerInstance == null) {
            accountManagerInstance = new AccountManager(context);
        }
        return accountManagerInstance;
    }
}
