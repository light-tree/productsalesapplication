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

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.product_sales_application.R;
import com.example.product_sales_application.adapters.OrderDetailAdapter;
import com.example.product_sales_application.adapters.ProductAdapter;
import com.example.product_sales_application.adapters.ProductTypeAdapter;
import com.example.product_sales_application.api.ProductApi;
import com.example.product_sales_application.common.LastItemGridLayoutManager;
import com.example.product_sales_application.manager.AccountManager;
import com.example.product_sales_application.manager.CartManagerSingleton;
import com.example.product_sales_application.models.Cart;
import com.example.product_sales_application.models.Product;
import com.example.product_sales_application.models.ProductTypeDomain;
import com.example.product_sales_application.models.RequestCode;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private RecyclerView productTypeView;
    private RecyclerView productRecycler;
    private List<ProductTypeDomain> productTypeDomainList;
    private List<Product> productList;
    private ProductTypeAdapter productTypeAdapter;
    private ProductAdapter productAdapter;
    private Parcelable recyclerViewState;

    private TextView query;
    private TextView type;

    public int page = 1, limit = 6;

    public static String textQueryStatic = "";
    public static String textTypeStatic = "";
    private AccountManager accountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        accountManager = CartManagerSingleton.getAccountManagerInstance(this);
        drawerLayout = findViewById(R.id.drawer_layout_product_list);
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
                case R.id.home:{  
                    drawerLayout.close();
                    startActivity(new Intent(ProductListActivity.this, HomeActivity.class));
                    finish();
                    return true;
                }
                case R.id.login: {
                    if (isLogin()) {
                        accountManager.logOut();
                        navigationView.getMenu().findItem(R.id.login).setTitle("Đăng nhập");
                        drawerLayout.close();
                        Toast.makeText(this, "Đăng xuất thành công.", Toast.LENGTH_LONG);
                        return true;
                    }
                    drawerLayout.close();
                    startActivityForResult(new Intent(this, LoginActivity.class), RequestCode.PRODUCT_LIST_LOGIN);
                    return true;
                }
                case R.id.order_history: {
                    drawerLayout.close();
                    if(!isLogin()){
                        showErrorNotLogin();
                        return false;
                    }
                    startActivity(new Intent(this, OrderHistoryActivity.class));
                    return true;
                }
            }
            return true;
        });

        productTypeView = (RecyclerView) findViewById(R.id.product_type_recycler);
        productRecycler = (RecyclerView) findViewById(R.id.product_recycler);

        productTypeDomainList = new ArrayList<>();
        productTypeDomainList.add(new ProductTypeDomain(1, "Tất cả", "https://tietkiemdiennang.net/wp-content/uploads/2020/08/thiet-bi-dien-tu-la-gi-1-2.jpg"));
        productTypeDomainList.add(new ProductTypeDomain(2, getString(R.string.product_type_1), "https://cdn.tgdd.vn/Products/Images/42/247508/iphone-14-pro-tim-thumb-600x600.jpg"));
        productTypeDomainList.add(new ProductTypeDomain(3, getString(R.string.product_type_2), "https://tinhocluna.com/wp-content/uploads/2021/04/pc-gaming.jpg"));
        productTypeDomainList.add(new ProductTypeDomain(4, getString(R.string.product_type_3), "https://diennuocnhatlong.vn/uploads/may-lanh-nguyen-ly-hoat-dong-3.jpg"));
        productTypeDomainList.add(new ProductTypeDomain(5, getString(R.string.product_type_4), "https://dienmaythudo24h.com/wp-content/uploads/2020/12/may-giat-long-ngang-toshiba-inverter-85kg-twbk95g4vws-wbmlmw.jpg"));
        productTypeDomainList.add(new ProductTypeDomain(6, getString(R.string.product_type_5), "https://blog.dktcdn.net/files/kinh-doanh-hang-gia-dung-1.jpg"));

        query = findViewById(R.id.query);
        type = findViewById(R.id.type);
        String textQuery = getIntent().getStringExtra("query");
        if (!TextUtils.isEmpty(textQuery)) {
            textQueryStatic = textQuery;
        }
        if (!TextUtils.isEmpty(textQueryStatic)) query.setText("Từ khóa: " + textQueryStatic);

        String textType = getIntent().getStringExtra("type");
        if (!TextUtils.isEmpty(textType)) {
            textTypeStatic = textType;
        }
        if (!TextUtils.isEmpty(textType)) type.setText("Loại sản phẩm: " + textTypeStatic);
        GetProductsByType(textQueryStatic, textTypeStatic);
        productTypeAdapter = new ProductTypeAdapter(productTypeDomainList);
        productTypeView.setAdapter(productTypeAdapter);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        productTypeView.setLayoutManager(horizontalLayoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nav, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                GetProductsByType(query, textTypeStatic);
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
        if(resultCode == RESULT_CANCELED){
            finish();
        }

        if(data == null)
            return;

        if(requestCode == RequestCode.PRODUCT_LIST_LOGIN){
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
                Intent intent = new Intent(ProductListActivity.this, ProductDetailActivity.class);
                intent.putExtra("productId", result.getContents());
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void GetProductsByType(String query, String type) {
        textQueryStatic = query;
        if(textTypeStatic != type) limit = 6;
        textTypeStatic = type;
        if (!TextUtils.isEmpty(textQueryStatic)) this.query.setText("Từ khóa: " + textQueryStatic);
        if (!TextUtils.isEmpty(textTypeStatic)) this.type.setText("Loại sản phẩm: " + textTypeStatic);
        type = (TextUtils.isEmpty(type) || type.equals("Tất cả")) ? "" : type;

            ProductApi.productApi.getAllProductByType(type).enqueue(
                    new Callback<List<Product>>() {
                        @Override
                        public void onResponse(retrofit2.Call<List<Product>> call, Response<List<Product>> response) {
                            if(productRecycler.getLayoutManager() != null) {
                                recyclerViewState = productRecycler.getLayoutManager().onSaveInstanceState();
                            }
                            List<Product> tmp = new ArrayList<>();
                            tmp = response.body();
                            if(!TextUtils.isEmpty(textQueryStatic)){
                                tmp = tmp.stream().filter(product -> product.getName().toUpperCase().contains(textQueryStatic.toUpperCase())).collect(Collectors.toList());
                            }
                            tmp = tmp.stream().limit(limit).collect(Collectors.toList());

                            if(productList != null){
                                productList.clear();
                                productList.addAll(tmp);
                            }
                            else{
                                productList = tmp;
                            }

                            if(productAdapter == null){
                                productAdapter = new ProductAdapter(productList);
                                productRecycler.setAdapter(productAdapter);
                                productRecycler.setLayoutManager(new LastItemGridLayoutManager(ProductListActivity.this, 2));
//                                productAdapter.getViewMoreButton().setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        limit += 6;
//                                        GetProductsByType(textQueryStatic, textTypeStatic);
//                                    }
//                                });
                            }
                            else{
                                productAdapter.notifyDataSetChanged();
                            }
                            productRecycler.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                        }

                        @Override
                        public void onFailure(Call<List<Product>> call, Throwable t) {
                            productList = new ArrayList<>();
                        }
                    }
            );
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
                Intent intent = new Intent(ProductListActivity.this, LoginActivity.class);
                startActivityForResult(intent, RequestCode.HOME_LOGIN);
            }
        });
        builder.setNegativeButton("Đóng", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}