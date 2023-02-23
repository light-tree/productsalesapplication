package com.example.product_sales_application.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.product_sales_application.R;
import com.example.product_sales_application.adapters.ProductAdapter;
import com.example.product_sales_application.adapters.ProductTypeAdapter;
import com.example.product_sales_application.models.Product;
import com.example.product_sales_application.models.ProductTypeDomain;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    private RecyclerView productTypeView;
    private RecyclerView productRecycler;
    private List<ProductTypeDomain> productTypeDomainList;
    private List<Product> productList;
    private ProductTypeAdapter productTypeAdapter;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        productTypeView = (RecyclerView) findViewById(R.id.product_type_recycler);
        productRecycler = (RecyclerView) findViewById(R.id.product_recycler);

        productTypeDomainList = new ArrayList<>();
        productTypeDomainList.add(new ProductTypeDomain(1, "Product 1", ""));
        productTypeDomainList.add(new ProductTypeDomain(2, "Product 2", ""));
        productTypeDomainList.add(new ProductTypeDomain(3, "Product 3", ""));
        productTypeDomainList.add(new ProductTypeDomain(4, "Product 4", ""));
        productTypeDomainList.add(new ProductTypeDomain(5, "Product 5", ""));

        productList = new ArrayList<>();
        productList.add(new Product("", "Iphone10", 100000000f));
        productList.add(new Product("", "Iphone11", 110000000f));
        productList.add(new Product("", "Iphone12", 120000000f));
        productList.add(new Product("", "Iphone13", 130000000f));

        productTypeAdapter = new ProductTypeAdapter(productTypeDomainList);
        productTypeView.setAdapter(productTypeAdapter);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        productTypeView.setLayoutManager(horizontalLayoutManagaer);


        productAdapter = new ProductAdapter(productList);
        productRecycler.setAdapter(productAdapter);
        productRecycler.setLayoutManager(new GridLayoutManager(this, 2));

    }
}