package com.example.product_sales_application.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.product_sales_application.R;
import com.example.product_sales_application.api.AccountApi;
import com.example.product_sales_application.manager.AccountManager;
import com.example.product_sales_application.manager.CartManagerSingleton;
import com.example.product_sales_application.models.Account;
import com.example.product_sales_application.models.Product;
import com.example.product_sales_application.models.RequestCode;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Button buttobLogin;
    private Button buttonBack;
    private EditText usernameEdit;
    private EditText passwordEdit;
    private TextView loginWithOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEdit = findViewById(R.id.username_edit_text);
        passwordEdit = findViewById(R.id.password_edit_text);
        loginWithOTP = findViewById(R.id.text_view_login_otp);

        buttobLogin = findViewById(R.id.button_login);
        buttonBack = findViewById(R.id.button_back);

        buttobLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loginWithOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, VerifyPhoneNumberActivity.class);
                startActivityForResult(intent, RequestCode.LOGIN_VERIFYPHONE);
            }
        });
    }

    public void checkLogin (){
        Account account = new Account();
        boolean isError = validateAccount(account);
        if(isError){
            return;
        }

        AccountApi.accountApi.checkLogin(account.getUsername(), account.getPassword()).enqueue(
                new Callback<List<Account>>() {
                    @Override
                    public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                        List<Account> list = response.body();
                        if(list.size() == 1){
                            AccountManager accountManager = CartManagerSingleton.getAccountManagerInstance(LoginActivity.this);
                            accountManager.login();
                            accountManager.saveAccount(list.get(0));
                            finish();
                            return;
                        }
                        Toast.makeText(getApplicationContext(), "Đăng nhập không thành công", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<List<Account>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Đăng nhập không thành công", Toast.LENGTH_LONG).show();
                    }
                }
        );


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RequestCode.LOGIN_VERIFYPHONE){
            if(resultCode == RESULT_OK){
                AccountManager accountManager = CartManagerSingleton.getAccountManagerInstance(this);
                accountManager.login();
                finish();
            } else if(resultCode == RESULT_CANCELED){

            }
        }
    }

    public boolean validateAccount(Account account){
        boolean isError = false;
        String username = usernameEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();
        if(username.contains(" ")){
            usernameEdit.setError("Tên đăng nhập không chứa khoảng trắng");
            isError = true;
        } else if( (username.length() < 8 || username.length() > 40)){
            usernameEdit.setError("Tên đăng nhập phải từ 8 đến 40 kí tự");
            isError = true;
        } else {
            account.setUsername(username);
        }

        if(password.contains(" ")){
            passwordEdit.setError("Mật khẩu không chứa khoảng trắng");
            isError = true;
        } else if( (password.length() < 8 || password.length() > 40)){
            passwordEdit.setError("Mật khẩu phải từ 8 đến 40 kí tự");
            isError = true;
        } else {
            account.setPassword(password);
        }
        return isError;
    }
}