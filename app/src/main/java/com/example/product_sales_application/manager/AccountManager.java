package com.example.product_sales_application.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.product_sales_application.models.Account;
import com.example.product_sales_application.models.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AccountManager {
    private static final String PREF_NAME = "account_prefs";
    private static final String KEY_ACCOUNT = "account";
    private static final String KEY_LOGIN_STATUS = "login_status";

    private SharedPreferences mPrefs;
    private Gson mGson;

    public AccountManager(Context context) {
        mPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mGson = new Gson();
    }

    public void saveAccount(Account account) {
        account.setPassword("");
        account.setUsername("");
        String accountJson = mGson.toJson(account);
        mPrefs.edit().putString(KEY_ACCOUNT, accountJson).apply();
    }

    public void login() {
        mPrefs.edit().putBoolean(KEY_LOGIN_STATUS, true).apply();
    }

    public void logOut() {
        mPrefs.edit().clear().apply();
    }

    public boolean isLogin() {
        return mPrefs.getBoolean(KEY_LOGIN_STATUS, false);
    }

    public Account getAccount() {
        String accountJson = mPrefs.getString(KEY_ACCOUNT, null);
        if (accountJson != null) {
            Type type = new TypeToken<Account>() {
            }.getType();
            return mGson.fromJson(accountJson, type);
        }
        return null;
    }
}
