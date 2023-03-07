package com.example.product_sales_application.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.product_sales_application.R;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;

public class ProductDetailActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Button buttonBack;
    private Button buttonAdd;
    private TextView tvProductName;
    private TextView tvDescription;
    private TextView tvPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        tvProductName = findViewById(R.id.name);
        tvPrice = findViewById(R.id.price);
        tvDescription = findViewById(R.id.description);


        tvProductName.setText(getIntent().getStringExtra("productName"));
        tvPrice.setText(getIntent().getStringExtra("productPrice"));
        tvDescription.setText(getIntent().getStringExtra("productDescription"));




        drawerLayout = findViewById(R.id.drawer_layout_product_detail);
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
                    startActivity(new Intent(ProductDetailActivity.this, LoginActivity.class));
                    return true;
                }
                case R.id.home:{
                    drawerLayout.close();
                    startActivity(new Intent(ProductDetailActivity.this, HomeActivity.class));
                    finish();
                    return true;
                }
            }
            return true;
        });

        buttonBack = findViewById(R.id.button_back);
        buttonAdd = findViewById(R.id.button_add);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 0:{
                if(resultCode == RESULT_CANCELED){
                    finish();
                }
                if(resultCode == RESULT_OK){
                    finish();
                }
            }
            default:{

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nav, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(ProductDetailActivity.this, ProductListActivity.class);
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

        if(toggle.onOptionsItemSelected(item)){
            return true;
        }

        if (id == R.id.scanner) {
            scannerCode();
        }

        if (id == R.id.cart) {
            startActivity(new Intent(ProductDetailActivity.this, CartActivity.class));
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
}