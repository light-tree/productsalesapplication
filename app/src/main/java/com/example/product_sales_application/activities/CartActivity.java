package com.example.product_sales_application.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.product_sales_application.Data.Cart;
import com.example.product_sales_application.Data.Product;
import com.example.product_sales_application.Data.adapter.CartAdapter;
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
    private TextView cart_total;

    private Button btnDelete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        btnConfirmCart = (Button)findViewById(R.id.btn_confirm_card);
        btnCancel = (Button)findViewById(R.id.btn_cancel);


        ArrayList<Product> productList = new ArrayList<Product>();
        productList.add(new Product(1,"Product 1",  R.drawable.image, 100f,1,"Description of product 2"));
        productList.add(new Product(2,"Product 2",  R.drawable.image, 100f,1,"Description of product 2"));
        productList.add(new Product(3,"Product 3",  R.drawable.image, 100f,1,"Description of product 2"));
        productList.add(new Product(1,"Product 1",  R.drawable.image, 100f,1,"Description of product 2"));
        productList.add(new Product(2,"Product 2",  R.drawable.image, 100f,1,"Description of product 2"));

        Cart cart = new Cart(productList);

        cartAdapter = new CartAdapter(cart);
        cartListView = findViewById(R.id.recycler_view_cart);
//        productListView.setLayoutManager(new LinearLayoutManager(this));
        cartListView.setLayoutManager(new GridLayoutManager(this, 1));
        cartListView.setAdapter(cartAdapter);
        cartListView.setNestedScrollingEnabled(true);




        //
        btnConfirmCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, OrderActivity.class);


                intent.putExtra("cart", cart);
                startActivity(intent);
            }
        });

    }
}