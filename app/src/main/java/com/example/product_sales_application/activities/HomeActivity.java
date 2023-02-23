package com.example.product_sales_application.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuInflater;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.example.product_sales_application.R;
import com.example.product_sales_application.adapters.ProductAdapter;
import com.example.product_sales_application.models.Product;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private CoordinatorLayout drawerLayout;
    private Toolbar toolbar;
    private ProductAdapter productAdapter;
    private RecyclerView productListRecyclerView1;

    private ProductAdapter productAdapter2;
    private RecyclerView productListRecyclerView2;

    private List<Product> products = new ArrayList<>(Arrays.asList(
            new Product("drawable://sample_product.jpg", "Product A", 100),
            new Product("", "Product B", 200),
            new Product("", "Product C", 100),
            new Product("", "Product D", 120),
            new Product("", "Product E", 150),
            new Product("", "Product F", 250),
            new Product("", "Product G", 110),
            new Product("", "Product H", 90),
            new Product("", "Product I", 90),
            new Product("", "Product J", 120)
    ));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout_home);
//        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
//        drawerLayout.addDrawerListener(toggle);
//
//        toggle.syncState();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        navigationView = findViewById(R.id.nav);
//        navigationView.bringToFront();
//        navigationView.setNavigationItemSelectedListener(item -> {
//            switch (item.getItemId()){
//                case R.id.nav_add:{
//                    drawerLayout.close();
//                    startActivityForResult(new Intent(ProductListActivity.this, ProductFormActivity.class), 0);
//                    return true;
//                }
//            }
//            return true;
//        });

        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        productAdapter = new ProductAdapter(products);
        productListRecyclerView1 = findViewById(R.id.product_list_1);
//        productListView.setLayoutManager(new LinearLayoutManager(this));
//        productListRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        productListRecyclerView1.setAdapter(productAdapter);
        productListRecyclerView1.setNestedScrollingEnabled(false);

        productListRecyclerView2 = findViewById(R.id.product_list_2);
        productListRecyclerView2.setAdapter(productAdapter);
        productListRecyclerView2.setNestedScrollingEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_nav, menu);
        return true;
    }
}