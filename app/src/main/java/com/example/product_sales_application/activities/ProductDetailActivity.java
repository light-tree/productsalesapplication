package com.example.product_sales_application.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.display.DeviceProductInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.product_sales_application.R;
import com.example.product_sales_application.api.ProductApi;
import com.example.product_sales_application.manager.AccountManager;
import com.example.product_sales_application.manager.CartManager;
import com.example.product_sales_application.manager.CartManagerSingleton;
import com.example.product_sales_application.models.Cart;
import com.example.product_sales_application.models.OrderDetail;
import com.example.product_sales_application.models.Product;
import com.example.product_sales_application.models.RequestCode;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Button buttonBack;
    private Button buttonAdd;
    private TextView tvProductName;
    private TextView tvDescription;
    private TextView tvPrice;
    private ImageView imageView;
    private OrderDetail orderDetail;
    private Product product;
    private ProgressDialog dialog;

    private Button btnDecrease;
    private Button btnIncrease;
    private Button btnClose;
    private TextView quantity;
    private ImageView imgViewProduct;
    private TextView tvPriceQuantity;
    private TextView tvProductTotalPrice;
    private TextView tvErrorQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(true);

        setContentView(R.layout.activity_product_detail);
        tvProductName = findViewById(R.id.name);
        tvPrice = findViewById(R.id.price);
        tvDescription = findViewById(R.id.description);
        imageView = findViewById(R.id.image);

        int id = Integer.parseInt(getIntent().getStringExtra("productId"));
        getProductById(id);

        drawerLayout = findViewById(R.id.drawer_layout_product_detail);
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
                    startActivityForResult(new Intent(ProductDetailActivity.this, LoginActivity.class), RequestCode.HOME_LOGIN);
                    return true;
                }
                case R.id.order_history: {
                    drawerLayout.close();
                    if (!isLogin()) {
                        showErrorNotLogin();
                        return false;
                    }
                    startActivity(new Intent(ProductDetailActivity.this, OrderHistoryActivity.class));
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
                initPopupChooseQuantity(product);
            }
        });
    }

    private void getProductById(int productId) {
        dialog.setMessage("Đang tìm kiếm đơn hàng.");
        dialog.show();

        ProductApi.productApi.getProductById(productId).enqueue(
                new Callback<Product>() {
                    @Override
                    public void onResponse(retrofit2.Call<Product> call, Response<Product> response) {
                        product = response.body();
                        Picasso.get().load(product.getUrl())
                                .into(imageView);
                        tvProductName.setText(product.getName());
                        tvPrice.setText(String.format("%.0f VND", product.getPrice()));
                        tvDescription.setText(product.getDescription());
                        orderDetail = new OrderDetail();
                        orderDetail.setProduct(product);
                        orderDetail.setQuantity(1);
                        dialog.hide();
                    }

                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {
                        product = null;
                        Toast.makeText(ProductDetailActivity.this, "Không tìm thấy sản phẩm.", Toast.LENGTH_LONG).show();
                        dialog.hide();
                        finish();
                    }
                }
        );
    }

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
                Intent intent = new Intent(ProductDetailActivity.this, ProductDetailActivity.class);
                intent.putExtra("productId", result.getContents());
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
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
                Intent intent = new Intent(ProductDetailActivity.this, LoginActivity.class);
                startActivityForResult(intent, RequestCode.HOME_LOGIN);
            }
        });
        builder.setNegativeButton("Đóng", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void initPopupChooseQuantity(Product p) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.layout_choose_quantity_product, null);

        btnDecrease = dialogLayout.findViewById(R.id.btn_decrease);
        btnIncrease = dialogLayout.findViewById(R.id.btn_increase);
        btnClose = dialogLayout.findViewById(R.id.close_choose_quantity_popup);
        quantity = dialogLayout.findViewById(R.id.tv_quantity);
        imgViewProduct = dialogLayout.findViewById(R.id.image_product_quantity);
        tvPriceQuantity = dialogLayout.findViewById(R.id.tv_product_price_quantity);
        tvProductTotalPrice = dialogLayout.findViewById(R.id.tv_total_price_quantity);
        tvErrorQuantity = dialogLayout.findViewById(R.id.error_quantity);

        Picasso.get().load(p.getUrl())
                .into(imgViewProduct);
        tvPriceQuantity.setText(String.format("Giá: %.2f", p.getPrice()));
        tvProductTotalPrice.setText(String.format("Giá: %.2f", p.getPrice() * orderDetail.getQuantity()));




        builder.setView(dialogLayout);

        builder.setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CartManager cartManager = CartManagerSingleton.getInstance(ProductDetailActivity.this);
                List<Product> productList = cartManager.getCart();
                Product result = productList.stream()
                        .filter(p -> p.getId() == product.getId())
                        .findFirst()
                        .orElse(null);
                if (result != null) {
                    result.setQuantity(result.getQuantity() + orderDetail.getQuantity());

                } else {
                    product.setQuantity(orderDetail.getQuantity());
                    productList.add(product);
                }
                cartManager.saveCart(productList);
//                startActivity((new Intent(ProductDetailActivity.this, HomeActivity.class)));
                finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
        layoutParams.gravity = Gravity.BOTTOM;

        alertDialog.getWindow().setAttributes(layoutParams);
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        alertDialog.show();

        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateQuantity(true, alertDialog);
            }
        });

        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateQuantity(false, alertDialog);
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void updateQuantity(boolean isIncrease, AlertDialog alertDialog) {
        dialog.setMessage("Kiểm tra số lượng");
        dialog.show();
        alertDialog.hide();

        ProductApi.productApi.getProductById(product.getId()).enqueue(
                new Callback<Product>() {
                    @Override
                    public void onResponse(retrofit2.Call<Product> call, Response<Product> response) {
                        product = response.body();
                        if (isIncrease) {
                            if (orderDetail.getQuantity() < product.getQuantity()) {
                                orderDetail.setQuantity(orderDetail.getQuantity() + 1);
                                tvErrorQuantity.setText("");
                            } else {
                                tvErrorQuantity.setText("Số lượng đã tối đa.");
                            }
                        } else {
                            orderDetail.setQuantity(orderDetail.getQuantity() > 1 ? orderDetail.getQuantity() - 1 : 1);
                            tvErrorQuantity.setText("");
                        }

                        quantity.setText(String.format("%d", orderDetail.getQuantity()));
                        tvProductTotalPrice.setText(String.format("Giá: %.2f", product.getPrice() * orderDetail.getQuantity()));
                        dialog.hide();
                        alertDialog.show();

                    }

                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {
                        product = null;
                        Toast.makeText(ProductDetailActivity.this, "Không tìm thấy sản phẩm.", Toast.LENGTH_LONG).show();
                        dialog.hide();
                        finish();
                    }
                }
        );
    }
}