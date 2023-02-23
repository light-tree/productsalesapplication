package com.example.product_sales_application.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.product_sales_application.Data.Cart;
import com.example.product_sales_application.Data.Order;
import com.example.product_sales_application.Data.Product;
import com.example.product_sales_application.Data.adapter.CartAdapter;
import com.example.product_sales_application.Data.adapter.OrderDetailAdapter;
import com.example.product_sales_application.R;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private OrderDetailAdapter orderDetailAdapter;
    private RecyclerView orderDetailCardView;
    private Button btnConfirmCart;
    private  Button btnBack;
    private TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Cart cart =  (Cart)getIntent().getSerializableExtra("cart");


        Order order = new Order(1,cart);
        OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(order);


        orderDetailCardView = findViewById(R.id.recycler_view_cart);
//        productListView.setLayoutManager(new LinearLayoutManager(this));
        orderDetailCardView.setLayoutManager(new GridLayoutManager(this, 1));
        orderDetailCardView.setAdapter(orderDetailAdapter);
        orderDetailCardView.setNestedScrollingEnabled(true);


        total = (TextView)findViewById(R.id.tv_invoice_total);

        total.setText(String.format( "Tổng tiền: " + "%.2f VND", order.getCart().getTotalPrice()) );

        btnBack = (Button)findViewById(R.id.btn_cancel);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Quay trở lại Activity trước đó
            }
        });
    }
}