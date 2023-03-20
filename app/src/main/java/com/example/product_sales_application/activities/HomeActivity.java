package com.example.product_sales_application.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.SearchView;

import android.widget.Toast;

import com.example.product_sales_application.R;
import com.example.product_sales_application.adapters.ProductAdapter;
import com.example.product_sales_application.adapters.ProductTypeAdapter;
import com.example.product_sales_application.api.ProductApi;
import com.example.product_sales_application.manager.AccountManager;
import com.example.product_sales_application.manager.CartManager;
import com.example.product_sales_application.manager.CartManagerSingleton;
import com.example.product_sales_application.models.Product;
import com.example.product_sales_application.models.ProductTypeDomain;
import com.example.product_sales_application.models.RequestCode;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeActivity extends AppCompatActivity {


    private RecyclerView productTypeView;
    private List<ProductTypeDomain> productTypeDomainList;
    private ProductTypeAdapter productTypeAdapter;

    private ProductAdapter productAdapter1;
    private ProductAdapter productAdapter2;
    private ProductAdapter productAdapter3;
    private RecyclerView productListRecyclerView1;
    private RecyclerView productListRecyclerView2;
    private RecyclerView productListRecyclerView3;

    private Button button1;
    private Button button2;
    private Button button3;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    private List<Product> products1;
    private List<Product> products2;
    private List<Product> products3;

    AccountManager accountManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        accountManager = CartManagerSingleton.getAccountManagerInstance(this);
        drawerLayout = findViewById(R.id.drawer_layout_home);
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
                    startActivityForResult(new Intent(HomeActivity.this, LoginActivity.class), RequestCode.HOME_LOGIN);
                    return true;
                }
                case R.id.order_history: {
                    drawerLayout.close();
                    if (!isLogin()) {
                        showErrorNotLogin();
                        return false;
                    }
                    startActivity(new Intent(HomeActivity.this, OrderHistoryActivity.class));
                    return true;
                }
            }
            return true;
        });

        getProduct();

        productTypeView = findViewById(R.id.product_type_recycler);
        productTypeDomainList = new ArrayList<>();
        productTypeDomainList.add(new ProductTypeDomain(1, "Tất cả", "https://tietkiemdiennang.net/wp-content/uploads/2020/08/thiet-bi-dien-tu-la-gi-1-2.jpg"));
        productTypeDomainList.add(new ProductTypeDomain(2, getString(R.string.product_type_1), "https://cdn.tgdd.vn/Products/Images/42/247508/iphone-14-pro-tim-thumb-600x600.jpg"));
        productTypeDomainList.add(new ProductTypeDomain(3, getString(R.string.product_type_2), "https://tinhocluna.com/wp-content/uploads/2021/04/pc-gaming.jpg"));
        productTypeDomainList.add(new ProductTypeDomain(4, getString(R.string.product_type_3), "https://diennuocnhatlong.vn/uploads/may-lanh-nguyen-ly-hoat-dong-3.jpg"));
        productTypeDomainList.add(new ProductTypeDomain(5, getString(R.string.product_type_4), "https://dienmaythudo24h.com/wp-content/uploads/2020/12/may-giat-long-ngang-toshiba-inverter-85kg-twbk95g4vws-wbmlmw.jpg"));
        productTypeDomainList.add(new ProductTypeDomain(6, getString(R.string.product_type_5), "https://blog.dktcdn.net/files/kinh-doanh-hang-gia-dung-1.jpg"));

        productTypeAdapter = new ProductTypeAdapter(productTypeDomainList);
        productTypeView.setAdapter(productTypeAdapter);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        productTypeView.setLayoutManager(horizontalLayoutManagaer);

        button1 = findViewById(R.id.button_1);
        button2 = findViewById(R.id.button_2);
        button3 = findViewById(R.id.button_3);

        Intent intent = new Intent(HomeActivity.this, ProductListActivity.class);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("query", "");
                intent.putExtra("type", getString(R.string.product_type_1));
                startActivity(intent);
                finish();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("query", "");
                intent.putExtra("type", getString(R.string.product_type_2));
                startActivity(intent);
                finish();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("query", "");
                intent.putExtra("type", getString(R.string.product_type_3));
                startActivity(intent);
                finish();
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
                Intent intent = new Intent(HomeActivity.this, ProductListActivity.class);
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
        intentIntegrator.setCameraId(0);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RequestCode.HOME_LOGIN){
            if(resultCode == RESULT_OK){
                navigationView.getMenu().findItem(R.id.login).setTitle("Đăng xuất");
            }
        }
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getBaseContext(), "Canceled", Toast.LENGTH_LONG);
            } else {
                Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
                intent.putExtra("productId", result.getContents());
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accountManager.logOut();
        // Handle the event when the app is closing
        Log.d("HomeActivity", "onDestroy: App is closing");

    }

    private void getProduct() {
        ProductApi.productApi.getAllProductByTypeWithPaging(getString(R.string.product_type_1), 1, 6).enqueue(
                new Callback<List<Product>>() {
                    @Override
                    public void onResponse(retrofit2.Call<List<Product>> call, Response<List<Product>> response) {
                        products1 = response.body();
                        buildRecycler(productAdapter1, products1, productListRecyclerView1, R.id.product_list_1);
                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {
                        products1 = new ArrayList<>();
                    }
                }
        );
        ProductApi.productApi.getAllProductByTypeWithPaging(getString(R.string.product_type_2), 1, 6).enqueue(
                new Callback<List<Product>>() {
                    @Override
                    public void onResponse(retrofit2.Call<List<Product>> call, Response<List<Product>> response) {
                        products2 = response.body();
                        buildRecycler(productAdapter2, products2, productListRecyclerView2, R.id.product_list_2);
                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {
                        products2 = new ArrayList<>();
                    }
                }
        );
        ProductApi.productApi.getAllProductByTypeWithPaging(getString(R.string.product_type_3), 1, 6).enqueue(
                new Callback<List<Product>>() {
                    @Override
                    public void onResponse(retrofit2.Call<List<Product>> call, Response<List<Product>> response) {
                        products3 = response.body();
                        buildRecycler(productAdapter3, products3, productListRecyclerView3, R.id.product_list_3);
                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {
                        products3 = new ArrayList<>();
                    }
                }
        );
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        AccountManager accountManager = CartManagerSingleton.getAccountManagerInstance(this);
//        accountManager.logOut();
//    }

    public void buildRecycler(ProductAdapter productAdapter, List<Product> products, RecyclerView recyclerView, int id) {
        productAdapter = new ProductAdapter(products);
        recyclerView = findViewById(id);
        recyclerView.setAdapter(productAdapter);
        recyclerView.setNestedScrollingEnabled(false);
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
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivityForResult(intent, RequestCode.HOME_LOGIN);
            }
        });
        builder.setNegativeButton("Đóng", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}