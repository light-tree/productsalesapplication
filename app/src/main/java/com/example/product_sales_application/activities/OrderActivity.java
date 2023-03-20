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
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.product_sales_application.MainActivity;
import com.example.product_sales_application.api.OrderApi;
import com.example.product_sales_application.api.ProductApi;
import com.example.product_sales_application.manager.AccountManager;
import com.example.product_sales_application.manager.CartManager;
import com.example.product_sales_application.manager.CartManagerSingleton;
import com.example.product_sales_application.models.Account;
import com.example.product_sales_application.models.Cart;
import com.example.product_sales_application.models.Order;
import com.example.product_sales_application.adapters.OrderDetailAdapter;
import com.example.product_sales_application.R;
import com.example.product_sales_application.models.OrderDetail;
import com.example.product_sales_application.models.Product;
import com.example.product_sales_application.models.RequestCode;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private OrderDetailAdapter orderDetailAdapter;
    private RecyclerView orderDetailCardView;
    private Button btnConfirmCart;
    private  Button btnBack;
    private TextView total;
    private List<OrderDetail> orderDetailList;
    Cart cart;
    private TextView customerName;
    private TextView customerPhone;
    private TextView customerAddress;
    private EditText requiredDate;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    AccountManager accountManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        customerName = (TextView)findViewById(R.id.ed_customer_fullname);
        customerPhone = (TextView)findViewById(R.id.ed_customer_phone_number);
        customerAddress = (TextView)findViewById(R.id.ed_customer_address);
        accountManager = CartManagerSingleton.getAccountManagerInstance(this);


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
                    startActivity(new Intent(OrderActivity.this, HomeActivity.class));
                    finish();
                    return true;
                }
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
       requiredDate = findViewById(R.id.ed_required_date);
        requiredDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                Calendar minDateCalendar = Calendar.getInstance();
                minDateCalendar.add(Calendar.DAY_OF_MONTH, 0);
                Calendar maxDateCalendar = Calendar.getInstance();
                maxDateCalendar.add(Calendar.DAY_OF_MONTH, 7); // Thêm 7 ngày để có được ngày max date

                DatePickerDialog datePickerDialog = new DatePickerDialog(OrderActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                dayOfMonth += 1;
                                month += 1;
                                String dayOfMonthStr = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                                String monthStr = month < 10 ? "0" + month : String.valueOf(month);
                                String yearStr = String.valueOf(year);

                                String dateStr = (dayOfMonthStr) + "/" + (monthStr) + "/" + yearStr;

                                requiredDate.setText(dateStr);
                                // Thiết lập ngày đã chọn vào EditText
                              //  requiredDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                                isDateValid(requiredDate.getText().toString());

                            }
                        }, year, month, day);

                datePickerDialog.getDatePicker().setMinDate(minDateCalendar.getTimeInMillis());
                datePickerDialog.getDatePicker().setMaxDate(maxDateCalendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });
        requiredDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần xử lý gì ở đây
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String userInput = s.toString();
                isDateValid(userInput);
            }
        });

        customerPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần xử lý gì ở đây
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        @Override
            public void afterTextChanged(Editable s) {
            String userInput = s.toString();
            isPhoneValid(userInput);

            }
        });

        customerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần xử lý gì ở đây
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }
            @Override
            public void afterTextChanged(Editable s) {
                String userInput = s.toString();
                isFullNameValid(userInput);

            }
        });

        customerAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần xử lý gì ở đây
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                String userInput = s.toString();
                isAddressValid(userInput);



            }
        });


        CartManager cartManager = CartManagerSingleton.getInstance(this);
        orderDetailList = new ArrayList<>();
        cart =  new Cart((ArrayList<Product>) cartManager.getCart());
        getProductFromCart(cart);
         preferences = this.getSharedPreferences("cart_prefs", this.MODE_PRIVATE);
         editor = preferences.edit();

        OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(orderDetailList);

        orderDetailCardView = findViewById(R.id.recycler_view_product_order);
        orderDetailCardView.setLayoutManager(new GridLayoutManager(this, 1));
        orderDetailCardView.setAdapter(orderDetailAdapter);
        orderDetailCardView.setNestedScrollingEnabled(true);

        TextView total = findViewById(R.id.tv_invoice_total);
        double totalPrice = cart.getTotalPrice();
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        String formattedMoney = formatter.format(totalPrice)+" VNĐ";
        total.setText("Thành tiền: " + formattedMoney ) ;

        btnBack = (Button)findViewById(R.id.btn_cancel);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Quay trở lại Activity trước đó
            }
        });
        btnConfirmCart = (Button) findViewById(R.id.btn_confirm_card);
        btnConfirmCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValidate(requiredDate.getText().toString(), customerName.getText().toString(), customerPhone.getText().toString(), customerAddress.getText().toString())) {
                   confirmCheckOut();
                }
            }
        });
        customerPhone.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    GetCustomerByPhone();
//                    setContentView();
                    return true;
                }
                return false;
            }
        });
    }
    private void GetCustomerByPhone(){
        if(!isLogin()){
            return;
        }

        String strCustomerPhone = customerPhone.getText().toString().trim();
        if(strCustomerPhone.length() != 10){
            customerPhone.setError("Số điện thoại phải dài 10 kí tự.");
            return;
        }

        ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("Đang tìm kiếm khách hàng");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();

        OrderApi.orderApi.getAllOrderByPhone(strCustomerPhone, "id", "desc").enqueue(
                new Callback<List<Order>>() {
                    @Override
                    public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                        List<Order> list =  response.body();

                        if(list.size() == 0){
                            Toast.makeText(OrderActivity.this, "Số điện thoại không có trong hệ thống.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        customerAddress.setText(list.get(0).getCustomerAddress());
                        customerName.setText(list.get(0).getCustomerFullName());
                        dialog.hide();
                    }

                    @Override
                    public void onFailure(Call<List<Order>> call, Throwable t) {
                        Toast.makeText(OrderActivity.this, "Lỗi xảy ra khi tìm kiếm thông tin.", Toast.LENGTH_LONG).show();
                        dialog.hide();
                        return;
                    }
                }
        );
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
                        Intent intent = new Intent(OrderActivity.this, HomeActivity.class);

                        startActivity(intent);
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

    private void getProductFromCart(Cart cart){

        for (Product p: cart.getProducts()) {
                orderDetailList.add( new OrderDetail(p.getQuantity(), p));
        }
    }
    private void checkOut(Account staffAccount){
        Order order = new Order();
        order.setCustomerAddress(customerAddress.getText().toString());
        order.setCustomerFullName(customerName.getText().toString());
        order.setCustomerPhone(customerPhone.getText().toString());
        Date today = new Date();
        order.setOrderedDate(today);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        order.setRequiredDate(convertStringToDate(requiredDate.getText().toString()));
        order.setStaff(staffAccount);
        order.setOrderDetailList(orderDetailList);
        OrderApi.orderApi.createOrder(order).enqueue(
                new Callback<Order>() {
                    @Override
                    public void onResponse(retrofit2.Call<Order> call, Response<Order> response) {
                        for (OrderDetail orderDetail : orderDetailList) {
                            ProductApi.productApi.getProductById(orderDetail.getProduct().getId()).enqueue(
                                    new Callback<Product>() {
                                        @Override
                                        public void onResponse(retrofit2.Call<Product> call, Response<Product> response) {
                                            Product productOnDatabase = response.body();
                                            productOnDatabase.setQuantity(productOnDatabase.getQuantity() - orderDetail.getProduct().getQuantity());
                                            ProductApi.productApi.updateProduct(productOnDatabase.getId(), productOnDatabase).enqueue( new Callback<Product>() {
                                                @Override
                                                public void onResponse(retrofit2.Call<Product> call, Response<Product> response) {

                                                }

                                                @Override
                                                public void onFailure(Call<Product> call, Throwable t) {

                                                }
                                            });
                                        }
                                        @Override
                                        public void onFailure(Call<Product> call, Throwable t) {

                                        }
                                    }
                            );
                        }

                        editor.clear(); // xóa toàn bộ dữ liệu SharedPreferences
                        editor.apply(); // lưu thay đổi vào SharedPreferences

                    }

                    @Override
                    public void onFailure(Call<Order> call, Throwable t) {

                    }
                }
        );
    }

    private boolean confirmCheckOut() {
        boolean check = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận đơn hàng");
        builder.setMessage("Bạn có muốn thực hiện đặt hàng?");
        builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Account account = accountManager.getAccount();
                checkOut(account);
                Intent intent = new Intent(OrderActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return check;
    }

    private boolean isDateValid(String date) {
        boolean check = true;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(new Date());

        Calendar maxDate = Calendar.getInstance();
        currentDate.setTime(new Date());

        maxDate.add(Calendar.DATE, 7); // Tối đã là 7 ngày kể từ ngày hôm nay


        try {
            Date selectedDate = sdf.parse(date);
            Calendar selectedDateCal = Calendar.getInstance();
            selectedDateCal.setTime(selectedDate);

            if (selectedDateCal.compareTo(currentDate) < 0 || selectedDateCal.compareTo(maxDate) > 0) {
                requiredDate.setError("Vui lòng nhập ngày giao hàng hợp lệ, trong vòng 7 ngày kể từ ngày hôm nay");
                check = false;

            } else {
               check = true;
            }

        } catch (ParseException e) {
            // Ngày đã nhập không đúng định dạng
            check = false;
            requiredDate.setError("Vui lòng nhập ngày giao hàng hợp lệ, trong vòng 7 ngày kể từ ngày hôm nay");
        }
        return check;

    }
    private boolean isPhoneValid(String phone) {
        String regex = "0[0-9]{9,10}";
        boolean check = true;
        if(!phone.matches(regex)){
           customerPhone.setError("Số điện thoại không hợp lệ");
           check = false;
        }
        return check ;
    }
    private boolean isFullNameValid(String fullName){
        boolean check = true;
         if(( fullName.isEmpty()
                 || fullName == null
                 || fullName.length() < 8
                 || fullName.length() > 40)){
             customerName.setError("Tên phải từ 8 đến 40 kí tự ");
             check = false;
         }
         return check;
    }

    private boolean isAddressValid(String address){
        boolean check = true;
        if((address.isEmpty()
                || address == null
                || address.length() < 8
                || address.length() > 128)){
            customerAddress.setError("Địa chỉ phải trên 8 kí tự ");
            check = false;

        }
        return check;
    }

    private boolean checkValidate(String date, String fullName, String phone, String address){
        return ( isFullNameValid(fullName) &&  isPhoneValid(phone) && isDateValid(date)  &&  isAddressValid(address) );
    }

    private Date convertStringToDate(String input){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = null;
        try {
           date = dateFormat.parse(input);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}