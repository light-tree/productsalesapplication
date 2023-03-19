package com.example.product_sales_application.api;

import com.example.product_sales_application.models.Order;
import com.example.product_sales_application.models.Product;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OrderApi {

    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-mm-dd hh:MM:ss")
            .create();

    OrderApi orderApi = new Retrofit.Builder()
            .baseUrl("https://64131f54b1ea744303239a5d.mockapi.io/sale-product/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OrderApi.class);

    @POST("order")
    Call<Order> createOrder(@Body Order data) ;

    @GET("order")
    Call<Product> getOrder();

    @GET("order")
    Call<List<Order>> getAllOrderHistory();

    @GET("order")
    Call<List<Order>> getAllOrderByPhone(@Query("customerPhone") String phone,
                                         @Query("sortBy") String fieldOrder,
                                         @Query("order") String typeOrder);
}
