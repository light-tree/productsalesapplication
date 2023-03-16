package com.example.product_sales_application.api;

import com.example.product_sales_application.models.Order;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OrderHistoryApi {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-mm-dd hh:MM:ss")
            .create();

    OrderHistoryApi orderHistoryApi = new Retrofit.Builder()
            .baseUrl("https://64131f54b1ea744303239a5d.mockapi.io/sale-product/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OrderHistoryApi.class);

    @GET("order")
    Call<Order> getAllOrder();

    @GET("order")
    Call<Order> getAllOrderByPhone(@Query("customerPhone")String customerPhone);
}
