package com.example.product_sales_application.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.product_sales_application.models.Cart;
import com.example.product_sales_application.models.Product;
import com.example.product_sales_application.adapters.CartAdapter;
import com.example.product_sales_application.R;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private CartAdapter cartAdapter;
    private RecyclerView cartListView;
    private  Button btnConfirmCart;
    private Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        btnConfirmCart = (Button)findViewById(R.id.btn_confirm_card);
        btnCancel = (Button)findViewById(R.id.btn_cancel);

        drawerLayout = findViewById(R.id.drawer_layout_cart);
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
                    startActivity(new Intent(CartActivity.this, LoginActivity.class));
                    return true;
                }
                case R.id.home:{
                    drawerLayout.close();
                    startActivity(new Intent(CartActivity.this, HomeActivity.class));
                    finish();
                    return true;
                }
            }
            return true;
        });

        ArrayList<Product> productList = new ArrayList<Product>();
        productList.add(new Product(1,"Product1 1",  R.drawable.image, 100f,1,"Description of product 2"));
        productList.add(new Product(2,"Product1 2",  R.drawable.image, 100f,1,"Description of product 2"));
        productList.add(new Product(3,"Product1 3",  R.drawable.image, 100f,1,"Description of product 2"));
        productList.add(new Product(1,"Product1 1",  R.drawable.image, 100f,1,"Description of product 2"));
        productList.add(new Product(2,"Product1 2",  R.drawable.image, 100f,1,"Description of product 2"));

        Cart cart = new Cart(productList);

        cartAdapter = new CartAdapter(cart);
        cartListView = findViewById(R.id.recycler_view_cart);
        cartListView.setLayoutManager(new GridLayoutManager(this, 1));
        cartListView.setAdapter(cartAdapter);
        cartListView.setNestedScrollingEnabled(true);

        btnConfirmCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, OrderActivity.class);


                intent.putExtra("cart", cart);
                startActivity(intent);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

        }
        return super.onOptionsItemSelected(item);
    }
}