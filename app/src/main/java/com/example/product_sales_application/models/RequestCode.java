package com.example.product_sales_application.models;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class RequestCode {
    public static final int SCAN_HOME = 0;
    public static final int SCAN_PRODUCTLIST = 1;
    public static final int HOME_LOGIN = 2;
    public static final int LOGIN_VERIFYPHONE = 3;

    @IntDef({SCAN_HOME, SCAN_PRODUCTLIST})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Season {
    }
}
