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
import android.widget.Toast;

import com.example.product_sales_application.manager.AccountManager;
import com.example.product_sales_application.manager.CartManager;
import com.example.product_sales_application.manager.CartManagerSingleton;
import com.example.product_sales_application.models.Cart;
import com.example.product_sales_application.models.Product;
import com.example.product_sales_application.adapters.CartAdapter;
import com.example.product_sales_application.R;
import com.example.product_sales_application.models.RequestCode;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    CartManager cartManager ;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private CartAdapter cartAdapter;
    private RecyclerView cartListView;
    private  Button btnConfirmCart;
    private Button btnCancel;

    private AccountManager accountManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        btnConfirmCart = (Button)findViewById(R.id.btn_confirm_card);
        btnCancel = (Button)findViewById(R.id.btn_cancel);
        accountManager = CartManagerSingleton.getAccountManagerInstance(this);


        drawerLayout = findViewById(R.id.drawer_layout_cart);
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
                    startActivity(new Intent(CartActivity.this, HomeActivity.class));
                    finish();
                    return true;
                }
                case R.id.login: {
                    if (isLogin()) {
                        accountManager.logOut();
                        navigationView.getMenu().findItem(R.id.login).setTitle("Đăng nhập");
                        drawerLayout.close();
                        Toast.makeText(this, "Đăng xuất thành công.", Toast.LENGTH_LONG);
                        setResult(RESULT_CANCELED);
                        finish();
                        return true;
                    }
                    drawerLayout.close();
                    startActivityForResult(new Intent(CartActivity.this, LoginActivity.class), RequestCode.CART_LOGIN);
                    return true;
                }
                case R.id.order_history: {
                    drawerLayout.close();
                    if(!isLogin()){
                        showErrorNotLogin();
                        return false;
                    }
                    startActivity(new Intent(CartActivity.this, OrderHistoryActivity.class));
                    return true;
                }
            }
            return true;
        });
        cartManager =  CartManagerSingleton.getInstance(this);
        List<Product> productList = cartManager.getCart();


        Cart cart = new Cart((ArrayList<Product>) productList);

        cartAdapter = new CartAdapter(cart);
        cartListView = findViewById(R.id.recycler_view_product_order);
        cartListView.setLayoutManager(new GridLayoutManager(this, 1));
        cartListView.setAdapter(cartAdapter);
        cartListView.setNestedScrollingEnabled(true);

        btnConfirmCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!productList.isEmpty()){
                    Intent intent = new Intent(CartActivity.this, OrderActivity.class);
                    startActivity(intent);
                }
                else{
                    confirmDialog();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                Intent intent = new Intent(CartActivity.this, ProductListActivity.class);
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

        if(requestCode == RequestCode.CART_LOGIN){
            if(resultCode == RESULT_OK){
                navigationView.getMenu().findItem(R.id.login).setTitle("Đăng xuất");
            }
            return;
        }

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getBaseContext(), "Canceled", Toast.LENGTH_LONG);
            } else {
                Intent intent = new Intent(CartActivity.this, ProductDetailActivity.class);
                intent.putExtra("productId", result.getContents());
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Thông báo");
        builder.setMessage("Giỏ hàng trống.");
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
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
                Intent intent = new Intent(CartActivity.this, LoginActivity.class);
                startActivityForResult(intent, RequestCode.HOME_LOGIN);
            }
        });
        builder.setNegativeButton("Đóng", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}