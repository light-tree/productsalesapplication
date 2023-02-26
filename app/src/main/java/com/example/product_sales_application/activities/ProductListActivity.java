package com.example.product_sales_application.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.product_sales_application.R;
import com.example.product_sales_application.adapters.ProductAdapter;
import com.example.product_sales_application.adapters.ProductTypeAdapter;
import com.example.product_sales_application.models.Product;
import com.example.product_sales_application.models.ProductTypeDomain;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
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

        drawerLayout = findViewById(R.id.drawer_layout_product_list);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.nav);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.login:{
                    drawerLayout.close();
                    startActivity(new Intent(ProductListActivity.this, LoginActivity.class));
                    return true;
                }
                case R.id.home:{
                    drawerLayout.close();
                    startActivity(new Intent(ProductListActivity.this, HomeActivity.class));
                    finish();
                    return true;
                }
            }
            return true;
        });

        productTypeView = (RecyclerView) findViewById(R.id.product_type_recycler);
        productRecycler = (RecyclerView) findViewById(R.id.product_recycler);

        productTypeDomainList = new ArrayList<>();
        productTypeDomainList.add(new ProductTypeDomain(1, "Product1 1", ""));
        productTypeDomainList.add(new ProductTypeDomain(2, "Product1 2", ""));
        productTypeDomainList.add(new ProductTypeDomain(3, "Product1 3", ""));
        productTypeDomainList.add(new ProductTypeDomain(4, "Product1 4", ""));
        productTypeDomainList.add(new ProductTypeDomain(5, "Product1 5", ""));

        productList = new ArrayList<>();
        productList.add(new Product(1, "Iphone10", 100f));
        productList.add(new Product(2, "Iphone11", 110f));
        productList.add(new Product(3, "Iphone12", 120f));
        productList.add(new Product(4, "Iphone13", 130f));

        productTypeAdapter = new ProductTypeAdapter(productTypeDomainList);
        productTypeView.setAdapter(productTypeAdapter);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        productTypeView.setLayoutManager(horizontalLayoutManagaer);


        productAdapter = new ProductAdapter(productList);
        productRecycler.setAdapter(productAdapter);
        productRecycler.setLayoutManager(new GridLayoutManager(this, 2));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nav, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(toggle.onOptionsItemSelected(item)){
            return true;
        }

        if (id == R.id.scanner) {

        }

        if (id == R.id.cart) {
            startActivity(new Intent(ProductListActivity.this, CartActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}