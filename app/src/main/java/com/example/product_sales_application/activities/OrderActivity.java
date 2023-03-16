package com.example.product_sales_application.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.product_sales_application.manager.AccountManager;
import com.example.product_sales_application.manager.CartManagerSingleton;
import com.example.product_sales_application.models.Cart;
import com.example.product_sales_application.models.Order;
import com.example.product_sales_application.adapters.OrderDetailAdapter;
import com.example.product_sales_application.R;
import com.example.product_sales_application.models.RequestCode;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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
            switch (item.getItemId()) {
                case R.id.login: {
                    drawerLayout.close();
                    startActivityForResult(new Intent(OrderActivity.this, LoginActivity.class), RequestCode.HOME_LOGIN);
                    return true;
                }
                case R.id.order_history: {
                    drawerLayout.close();
                    if(!isLogin()){
                        showErrorNotLogin();
                        return false;
                    }
                    startActivity(new Intent(OrderActivity.this, OrderHistoryActivity.class));
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
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(OrderActivity.this, ProductListActivity.class);
                intent.putExtra("query", query);
                startActivity(intent);
                finish();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Xử lý khi thay đổi nội dung search query
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (id == R.id.scanner) {
            if(!isLogin()){
                showErrorNotLogin();
                return false;
            }
            scannerCode();
        }

        if (id == R.id.cart) {
            startActivity(new Intent(this, CartActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void scannerCode() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan a barcode for QRcode");
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setRequestCode(1);
        intentIntegrator.setCameraId(0);
        intentIntegrator.initiateScan();
    }

    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result != null) {
                        //Làm gì đó khi có response trả về
                    } else {
                    }
                }
            }
    );

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RequestCode.HOME_LOGIN){
            if (data.getBooleanExtra("isLogin", false)) {
                SharedPreferences sharedPref = getSharedPreferences("login_status", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.apply();
            }
            return;
        }

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getBaseContext(), "Canceled", Toast.LENGTH_LONG);
            } else {
                Intent intent = new Intent(OrderActivity.this, ProductDetailActivity.class);
                intent.putExtra("productId", result.getContents());
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private boolean isLogin() {
        AccountManager accountManager = CartManagerSingleton.getAccountManagerInstance(this);
        return accountManager.isLogin();
    }

    private void showErrorNotLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cãnh báo");
        builder.setMessage("Bạn cần đăng nhập để thực hiện chức năng này?");
        builder.setPositiveButton("Đăng nhập", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(OrderActivity.this, LoginActivity.class);
                startActivityForResult(intent, RequestCode.HOME_LOGIN);
            }
        });
        builder.setNegativeButton("Đóng", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}