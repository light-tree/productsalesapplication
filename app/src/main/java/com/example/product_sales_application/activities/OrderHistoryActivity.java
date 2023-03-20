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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.product_sales_application.R;
import com.example.product_sales_application.adapters.OrderHistoryAdapter;
import com.example.product_sales_application.api.OrderApi;
import com.example.product_sales_application.manager.AccountManager;
import com.example.product_sales_application.manager.CartManagerSingleton;
import com.example.product_sales_application.models.Order;
import com.example.product_sales_application.models.RequestCode;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private RecyclerView listOrderHistory;
    private List<Order> listOrderHistoryData;
    private OrderHistoryAdapter orderHistoryAdapter;
    private EditText edtSearchCusPhone;
    private Button buttonSearchInvoice;
    private String strSearchvalue = "";
    private AccountManager accountManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        accountManager = CartManagerSingleton.getAccountManagerInstance(this);

        edtSearchCusPhone = findViewById(R.id.search_cust_phone);
        buttonSearchInvoice = findViewById(R.id.button_search_invoice_by_phone);
        drawerLayout = findViewById(R.id.drawer_layout_order);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.nav);
        if (isLogin()) {
            navigationView.getMenu().findItem(R.id.login).setTitle("Đăng xuất");
        } else {
            navigationView.getMenu().findItem(R.id.login).setTitle("Đăng nhập");
        }
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.login: {
                    if (isLogin()) {
                        accountManager.logOut();
                        navigationView.getMenu().findItem(R.id.login).setTitle("Đăng nhập");
                        drawerLayout.close();
                        Toast.makeText(this, "Đăng xuất thành công.", Toast.LENGTH_LONG);
                        return true;
                    }
                    drawerLayout.close();
                    startActivityForResult(new Intent(OrderHistoryActivity.this, LoginActivity.class), RequestCode.HOME_LOGIN);
                    return true;
                }
                case R.id.home:{
                    drawerLayout.close();
                    startActivity(new Intent(OrderHistoryActivity.this, HomeActivity.class));
                    finish();
                    return true;
                }
            }
            return true;
        });

        listOrderHistory = findViewById(R.id.list_order_history);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        listOrderHistory.setLayoutManager(linearLayoutManager);

        orderHistoryAdapter = new OrderHistoryAdapter(listOrderHistoryData);
        listOrderHistory.setAdapter(orderHistoryAdapter);

        buttonSearchInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strSearchvalue = edtSearchCusPhone.getText().toString().trim();
                getOrderWithPhone();
            }
        });

        getAllOrderDetail();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nav, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(OrderHistoryActivity.this, ProductListActivity.class);
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
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getBaseContext(), "Canceled", Toast.LENGTH_LONG);
            } else {
                Intent intent = new Intent(OrderHistoryActivity.this, ProductDetailActivity.class);
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
                Intent intent = new Intent(OrderHistoryActivity.this, LoginActivity.class);
                startActivityForResult(intent, RequestCode.HOME_LOGIN);
            }
        });
        builder.setNegativeButton("Đóng", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getAllOrderDetail() {
        ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("Đang tìm kiếm đơn hàng.");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
        OrderApi.orderApi.getAllOrderHistory().enqueue(
                new Callback<List<Order>>() {
                    @Override
                    public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                        listOrderHistoryData = response.body();
                        orderHistoryAdapter.setOrderList(listOrderHistoryData);
                        dialog.hide();
                    }

                    @Override
                    public void onFailure(Call<List<Order>> call, Throwable t) {
                        Toast.makeText(OrderHistoryActivity.this, "Lỗi không thể lấy dữ liệu", Toast.LENGTH_LONG);
                        dialog.hide();
                    }
                }
        );
    }

    private void getOrderWithPhone() {
        ProgressDialog dialog= new ProgressDialog(this);
        dialog.setMessage("Đang tìm kiếm đơn hàng.");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();

        OrderApi.orderApi.getAllOrderByPhone(strSearchvalue, "id", "desc").enqueue(
                new Callback<List<Order>>() {
                    @Override
                    public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                        if(response.body().size() == listOrderHistoryData.size()){
                            Toast.makeText(OrderHistoryActivity.this, "Tất cả đã hiển thị.", Toast.LENGTH_SHORT).show();
                            dialog.hide();
                            return;
                        }
                        listOrderHistoryData = response.body();
                        orderHistoryAdapter.setOrderList(listOrderHistoryData);
                        dialog.hide();
                    }

                    @Override
                    public void onFailure(Call<List<Order>> call, Throwable t) {
                        Toast.makeText(OrderHistoryActivity.this, "Lỗi không thể lấy dữ liệu", Toast.LENGTH_LONG);
                        dialog.hide();
                    }
                }
        );
    }
}