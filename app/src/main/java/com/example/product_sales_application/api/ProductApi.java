package com.example.product_sales_application.api;
import retrofit2.Call;
import  retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import com.example.product_sales_application.models.Product;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public interface ProductApi {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-mm-dd hh:MM:ss")
            .create();

    ProductApi productApi = new Retrofit.Builder()
            .baseUrl("https://63c500edf3a73b34784bba4a.mockapi.io/sale-product/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductApi.class);

    @GET("product")
    Call<List<Product>> getAllProduct();

    @GET("product")
    Call<List<Product>> getAllProductByType(@Query("productTypeId") String productTypeId);

    @GET("product/{id}")
    Call<Product> getProductById(@Path("id") int id);

    @GET("product")
    Call<List<Product>> getAllProductByTypeWithPaging(
            @Query("productTypeId") String productTypeId,
            @Query("page") int page,
            @Query("limit") int limit );

//    @GET("product")
//    Call<List<Product>> getAllProductByNameAndTypeWithPaging(
//            @Query("productTypeId") String name,
//            @Query("productTypeId") String productTypeId,
//            @Query("page") int page,
//            @Query("limit") int limit );

    @GET("product")
    Call<List<Product>> getAllProductWithPaging(
            @Query("page") int page,
            @Query("limit") int limit );
}
