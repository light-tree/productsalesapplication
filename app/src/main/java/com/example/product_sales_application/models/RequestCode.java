package com.example.product_sales_application.models;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class RequestCode {
    public static final int SCAN_HOME = 0;
    public static final int SCAN_PRODUCTLIST = 1;
    public static final int HOME_LOGIN = 2;
    public static final int LOGIN_VERIFYPHONE = 3;
    public static final int CART_LOGIN = 4;
    public static final int HISTORY_DETAIL = 5;
    public static final int ORDER_LOGIN = 6;
    public static final int ORDER_HISTORY_LOGIN = 6;
    public static final int PRODUCT_DETAIL_LOGIN = 7;
    public static final int PRODUCT_LIST_LOGIN = 8;
    public static final int HISTORY_HISTORY_DETAIL = 9;

    @IntDef({SCAN_HOME, SCAN_PRODUCTLIST})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Season {
    }
}
