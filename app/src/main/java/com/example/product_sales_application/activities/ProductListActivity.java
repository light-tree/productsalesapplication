package com.example.product_sales_application.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import com.example.product_sales_application.models.Cart;
import com.example.product_sales_application.models.Product;
import com.example.product_sales_application.models.ProductTypeDomain;
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
    private Button viewMoreButton;
    private Parcelable recyclerViewState;

    private TextView query;
    private TextView type;

    int page = 1, limit = 6;

    public static String textQueryStatic = "";
    public static String textTypeStatic = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        viewMoreButton = findViewById(R.id.view_more_button);
        drawerLayout = findViewById(R.id.drawer_layout_product_list);
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
                    startActivity(new Intent(ProductListActivity.this, LoginActivity.class));
                    return true;
                }
                case R.id.home: {
                    drawerLayout.close();
                    startActivity(new Intent(ProductListActivity.this, HomeActivity.class));
                    finish();
                    return true;
                }
                case R.id.order_history: {
                    drawerLayout.close();
                    startActivity((new Intent(ProductListActivity.this, OrderHistoryActivity.class)));
                    return true;
                }
            }
            return true;
        });

        productTypeView = (RecyclerView) findViewById(R.id.product_type_recycler);
        productRecycler = (RecyclerView) findViewById(R.id.product_recycler);

        productTypeDomainList = new ArrayList<>();
        productTypeDomainList.add(new ProductTypeDomain(1, "T???t c???", "https://tietkiemdiennang.net/wp-content/uploads/2020/08/thiet-bi-dien-tu-la-gi-1-2.jpg"));
        productTypeDomainList.add(new ProductTypeDomain(2, getString(R.string.product_type_1), "https://cdn.tgdd.vn/Products/Images/42/247508/iphone-14-pro-tim-thumb-600x600.jpg"));
        productTypeDomainList.add(new ProductTypeDomain(3, getString(R.string.product_type_2), "https://tinhocluna.com/wp-content/uploads/2021/04/pc-gaming.jpg"));
        productTypeDomainList.add(new ProductTypeDomain(4, getString(R.string.product_type_3), "https://diennuocnhatlong.vn/uploads/may-lanh-nguyen-ly-hoat-dong-3.jpg"));
        productTypeDomainList.add(new ProductTypeDomain(5, getString(R.string.product_type_4), "https://dienmaythudo24h.com/wp-content/uploads/2020/12/may-giat-long-ngang-toshiba-inverter-85kg-twbk95g4vws-wbmlmw.jpg"));
        productTypeDomainList.add(new ProductTypeDomain(6, getString(R.string.product_type_5), "https://blog.dktcdn.net/files/kinh-doanh-hang-gia-dung-1.jpg"));

        GetProductsByType(textQueryStatic, textTypeStatic);

        productTypeAdapter = new ProductTypeAdapter(productTypeDomainList);
        productTypeView.setAdapter(productTypeAdapter);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        productTypeView.setLayoutManager(horizontalLayoutManager);

        query = findViewById(R.id.query);
        type = findViewById(R.id.type);

        String textQuery = getIntent().getStringExtra("query");
        if (!TextUtils.isEmpty(textQuery)) {
            textQueryStatic = textQuery;
        }
        if (!TextUtils.isEmpty(textQueryStatic)) query.setText("T??? kh??a: " + textQueryStatic);

        String textType = getIntent().getStringExtra("type");
        if (!TextUtils.isEmpty(textType)) {
            textTypeStatic = textType;
        }
        if (!TextUtils.isEmpty(textType)) type.setText("Lo???i s???n ph???m: " + textTypeStatic);

        viewMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limit += 6;
                GetProductsByType(textQueryStatic, textTypeStatic);
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
                GetProductsByType(query, textQueryStatic);
//                Intent intent = new Intent(ProductListActivity.this, ProductListActivity.class);
//                intent.putExtra("query", query);
//                intent.putExtra("type", ((TextView) findViewById(R.id.type)).getText().toString().substring(15));
//                startActivity(intent);
//                finish();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // X??? l?? khi thay ?????i n???i dung search query
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
            scannerCode();
        }

        if (id == R.id.cart) {
            startActivity(new Intent(ProductListActivity.this, CartActivity.class));
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
                        //L??m g?? ???? khi c?? response tr??? v???
                    } else {
                    }
                }
            }
    );

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getBaseContext(), "Canceled", Toast.LENGTH_LONG);
            } else {
                Intent intent = new Intent(ProductListActivity.this, ProductDetailActivity.class);
                intent.putExtra("productId", result.getContents());
                activityResultLauncher.launch(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void GetProductsByType(String query, String type) {
        textQueryStatic = query;
        textTypeStatic = type;
        if (!TextUtils.isEmpty(textQueryStatic)) this.query.setText("T??? kh??a: " + textQueryStatic);
        type = (TextUtils.isEmpty(type) || type.equals("T???t c???")) ? "" : type;

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
                                productRecycler.setLayoutManager(new GridLayoutManager(ProductListActivity.this, 2));
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
}