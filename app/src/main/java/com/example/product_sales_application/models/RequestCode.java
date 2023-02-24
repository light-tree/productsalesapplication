package com.example.product_sales_application.models;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class RequestCode {
    public static final int SCAN_HOME = 0;
    public static final int SCAN_PRODUCTLIST = 1;

    @IntDef({SCAN_HOME, SCAN_PRODUCTLIST})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Season {
    }
}
