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
import com.example.product_sales_application.models.Order;
import com.example.product_sales_application.adapters.OrderDetailAdapter;
import com.example.product_sales_application.R;
import com.google.android.material.navigation.NavigationView;

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

        drawerLayout = findViewById(R.id.drawer_layout_order);
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
                    startActivity(new Intent(OrderActivity.this, LoginActivity.class));
                    return true;
                }
                case R.id.home:{
                    drawerLayout.close();
                    startActivity(new Intent(OrderActivity.this, HomeActivity.class));
                    finish();
                    return true;
                }
            }
            return true;
        });

        Cart cart =  (Cart)getIntent().getSerializableExtra("cart");
        Order order = new Order(1,cart);
        OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(order);

        orderDetailCardView = findViewById(R.id.recycler_view_cart);
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