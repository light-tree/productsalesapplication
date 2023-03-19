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

import android.app.ProgressDialog;
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

import com.example.product_sales_application.R;
import com.example.product_sales_application.adapters.OrderDetailAdapter;
import com.example.product_sales_application.manager.AccountManager;
import com.example.product_sales_application.manager.CartManagerSingleton;
import com.example.product_sales_application.models.Order;
import com.example.product_sales_application.models.OrderDetail;
import com.example.product_sales_application.models.RequestCode;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class HistoryDetailActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private OrderDetailAdapter orderDetailAdapter;
    private RecyclerView orderDetailCardView;
    private Button btnBack;
    private TextView total;
    private List<OrderDetail> orderDetailList;
    private TextView textViewCustomerName,
            textViewCutomerPhone,
            textViewStaffName,
            textViewStaffPhoneNumber,
            textViewCustomerAddr,
            textViewOrderDate,
            textViewRequireDate;
    private Order order;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Đang tải...");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
        initUI();
        order = new Gson().fromJson(getIntent().getStringExtra("order"), Order.class);
        initDataViewOrderDetail(order);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Quay trở lại Activity trước đó
            }
        });

        dialog.hide();
    }

    private void initUI() {
        drawerLayout = findViewById(R.id.drawer_layout_order);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.nav);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:{
                    drawerLayout.close();
                    startActivity(new Intent(HistoryDetailActivity.this, HomeActivity.class));
                    finish();
                    return true;
                }
                case R.id.login: {
                    drawerLayout.close();
                    startActivityForResult(new Intent(HistoryDetailActivity.this, LoginActivity.class), RequestCode.HOME_LOGIN);
                    return true;
                }
                case R.id.order_history: {
                    drawerLayout.close();
                    if (!isLogin()) {
                        showErrorNotLogin();
                        return false;
                    }
                    startActivity(new Intent(HistoryDetailActivity.this, OrderHistoryActivity.class));
                    return true;
                }
            }
            return true;
        });

        orderDetailAdapter = new OrderDetailAdapter(orderDetailList);

        orderDetailCardView = findViewById(R.id.recycler_view_product_order);
        orderDetailCardView.setLayoutManager(new GridLayoutManager(this, 1));
        orderDetailCardView.setAdapter(orderDetailAdapter);
        orderDetailCardView.setNestedScrollingEnabled(true);
        total = findViewById(R.id.tv_total);
        btnBack = (Button) findViewById(R.id.btn_cancel);
        textViewCustomerName = findViewById(R.id.customer_fullname);
        textViewCutomerPhone = findViewById(R.id.customer_phone_number);
        textViewStaffName = findViewById(R.id.staff_name);
        textViewStaffPhoneNumber = findViewById(R.id.staff_phone_number);
        textViewCustomerAddr = findViewById(R.id.customer_address);
        textViewOrderDate = findViewById(R.id.order_date);
        textViewRequireDate = findViewById(R.id.require_date);
    }

    private void initDataViewOrderDetail(Order order) {
        orderDetailList = order.getOrderDetailList();
        orderDetailAdapter.setOrderDetailList(orderDetailList);
        textViewCustomerName.setText("Khách hàng: " + order.getCustomerFullName());
        textViewCutomerPhone.setText("SĐT khách hàng: " + order.getCustomerPhone());
        textViewStaffName.setText("Nhân viên: " + order.getStaff().getFullName());
        textViewStaffPhoneNumber.setText("SĐT nhân viên: " + order.getStaff().getPhone());
        textViewCustomerAddr.setText("Địa chỉ khách hàng: " + order.getCustomerAddress());
        textViewOrderDate.setText("Ngày đặt hàng: " + DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(order.getOrderedDate()));
        textViewRequireDate.setText("Ngày giao hàng: " + DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(order.getRequiredDate()));
        List<Double> totalPrice = new ArrayList<>();
        orderDetailList.stream()
                .forEach(element -> {
                    if (totalPrice.size() == 0) {
                        totalPrice.add(0D);
                    }
                    double tmp = totalPrice.get(0) + element.getQuantity() * element.getProduct().getPrice();
                    totalPrice.set(0, tmp);
                });

        total.setText(String.format("Tổng tiền: " + "%.2f VND", totalPrice.size() != 0 ? totalPrice.get(0) : 0));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nav, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(HistoryDetailActivity.this, ProductListActivity.class);
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
            if (!isLogin()) {
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

        if (requestCode == RequestCode.HOME_LOGIN) {
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
                Intent intent = new Intent(HistoryDetailActivity.this, ProductDetailActivity.class);
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
                Intent intent = new Intent(HistoryDetailActivity.this, LoginActivity.class);
                startActivityForResult(intent, RequestCode.HOME_LOGIN);
            }
        });
        builder.setNegativeButton("Đóng", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}