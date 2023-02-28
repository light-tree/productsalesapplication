package com.example.product_sales_application.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.se.omapi.Session;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.product_sales_application.R;
import com.example.product_sales_application.adapters.ProductAdapter;
import com.example.product_sales_application.adapters.ProductTypeAdapter;
import com.example.product_sales_application.models.Product;
import com.example.product_sales_application.models.ProductTypeDomain;
import com.example.product_sales_application.models.RequestCode;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class HomeActivity extends AppCompatActivity {

    //    private CoordinatorLayout drawerLayout;
    //    private Toolbar toolbar;
    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result != null) {
//                       if(result.getResultCode() == RequestCode.SCAN_HOME){
//                           if (result.getData()== null) {
//                               Toast.makeText(getBaseContext(), "Canceled", Toast.LENGTH_LONG);
//                           } else {
//                               Intent resultData = result.getData();
////                               resultData.ge
//                               Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
////                               intent.putExtra("ProductId", )
//                               activityResultLauncher.launch(intent);
//                           }
//                       }
                    } else {
                    }
                }
            }
    );
    private RecyclerView productTypeView;
    private List<ProductTypeDomain> productTypeDomainList;
    private ProductTypeAdapter productTypeAdapter;

    private ProductAdapter productAdapter;
    private RecyclerView productListRecyclerView1;
    private RecyclerView productListRecyclerView2;
    private RecyclerView productListRecyclerView3;

    private Button button1;
    private Button button2;
    private Button button3;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    private List<Product> products = new ArrayList<>(Arrays.asList(
            new Product(1, "Product1 A", 100),
            new Product(2, "Product1 B", 200),
            new Product(3, "Product1 C", 100),
            new Product(4, "Product1 D", 120),
            new Product(5, "Product1 E", 150),
            new Product(6, "Product1 F", 250),
            new Product(7, "Product1 G", 110),
            new Product(8, "Product1 H", 90),
            new Product(9, "Product1 I", 90),
            new Product(10, "Product1 J", 120)
    ));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout_home);
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
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    return true;
                }
            }
            return true;
        });

        productTypeView = findViewById(R.id.product_type_recycler);
        productTypeDomainList = new ArrayList<>();
        productTypeDomainList.add(new ProductTypeDomain(1, getResources().getString(R.string.product_type_1), ""));
        productTypeDomainList.add(new ProductTypeDomain(2, getResources().getString(R.string.product_type_2), ""));
        productTypeDomainList.add(new ProductTypeDomain(3, getResources().getString(R.string.product_type_3), ""));
        productTypeDomainList.add(new ProductTypeDomain(4, getResources().getString(R.string.product_type_4), ""));
        productTypeDomainList.add(new ProductTypeDomain(5, getResources().getString(R.string.product_type_5), ""));

        productTypeAdapter = new ProductTypeAdapter(productTypeDomainList);
        productTypeView.setAdapter(productTypeAdapter);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        productTypeView.setLayoutManager(horizontalLayoutManagaer);

        productAdapter = new ProductAdapter(products);
        productListRecyclerView1 = findViewById(R.id.product_list_1);
        ;
        productListRecyclerView1.setAdapter(productAdapter);
        productListRecyclerView1.setNestedScrollingEnabled(false);

        productListRecyclerView2 = findViewById(R.id.product_list_2);
        productListRecyclerView2.setAdapter(productAdapter);
        productListRecyclerView2.setNestedScrollingEnabled(false);

        productListRecyclerView3 = findViewById(R.id.product_list_3);
        productListRecyclerView3.setAdapter(productAdapter);
        productListRecyclerView3.setNestedScrollingEnabled(false);

        button1 = findViewById(R.id.button_1);
        button2 = findViewById(R.id.button_2);
        button3 = findViewById(R.id.button_3);

        View.OnClickListener onClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomeActivity.this, ProductListActivity.class));
                finish();
            }
        };

        button1.setOnClickListener(onClick);
        button2.setOnClickListener(onClick);
        button3.setOnClickListener(onClick);
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
            scannerCode();
        }

        if (id == R.id.cart) {
            startActivity(new Intent(HomeActivity.this, CartActivity.class));
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

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getBaseContext(), "Canceled", Toast.LENGTH_LONG);
            } else {
                Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", result.getContents());
                activityResultLauncher.launch(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}