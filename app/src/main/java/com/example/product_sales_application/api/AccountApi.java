package com.example.product_sales_application.api;

import com.example.product_sales_application.models.Account;
import com.example.product_sales_application.models.Product;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AccountApi {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-mm-dd hh:MM:ss")
            .create();

    AccountApi accountApi = new Retrofit.Builder()
            .baseUrl("https://63c500edf3a73b34784bba4a.mockapi.io/sale-product/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AccountApi.class);

    @GET("account")
    Call<List<Account>> checkLogin(@Query("username") String username,
                                   @Query("password") String password);

    @GET("account")
    Call<List<Account>> getAccountByPhone(@Query("username") String phone);


}
