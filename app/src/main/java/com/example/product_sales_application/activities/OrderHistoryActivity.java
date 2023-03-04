package com.example.product_sales_application.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.product_sales_application.R;
import com.example.product_sales_application.adapters.OrderHistoryAdapter;
import com.example.product_sales_application.models.Cart;
import com.example.product_sales_application.models.Order;
import com.example.product_sales_application.models.OrderDetail;
import com.example.product_sales_application.models.Product;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView listOrderHistory;
    private List<Order> listOrderHistoryData;
    private OrderHistoryAdapter orderHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        listOrderHistory = findViewById(R.id.list_order_history);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        listOrderHistory.setLayoutManager(linearLayoutManager);

        listOrderHistoryData = new ArrayList<Order>() {{
            add(new Order("0123456789", "Anh A", 1, new Cart(new ArrayList<Product>() {{
                add(new Product(1, "Iphone10", 100f));
                add(new Product(2, "Iphone11", 110f));
                add(new Product(3, "Iphone12", 120f));
                add(new Product(4, "Iphone13", 130f));
            }}
            )));
            add(new Order("0987654321", "Anh B", 2, new Cart(new ArrayList<Product>() {{
                add(new Product(1, "Iphone10", 100f));
                add(new Product(2, "Iphone11", 110f));
                add(new Product(3, "Iphone12", 120f));
                add(new Product(4, "Iphone13", 130f));
            }}
            )));
        }};
        orderHistoryAdapter = new OrderHistoryAdapter(listOrderHistoryData);
        listOrderHistory.setAdapter(orderHistoryAdapter);
    }
}