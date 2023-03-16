package com.example.product_sales_application.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.product_sales_application.MainActivity;
import com.example.product_sales_application.R;
import com.example.product_sales_application.api.AccountApi;
import com.example.product_sales_application.manager.AccountManager;
import com.example.product_sales_application.manager.CartManager;
import com.example.product_sales_application.manager.CartManagerSingleton;
import com.example.product_sales_application.models.Account;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyPhoneNumberActivity extends AppCompatActivity {

    // variable for FirebaseAuth class
    private FirebaseAuth mAuth;

    // string for storing our verification ID
    private String verificationId;

    private EditText edtPhoneNumber;
    private Button btnVerifyPhone;
    private EditText edtOTP;
    private Button btnSendOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_number);

        mAuth = FirebaseAuth.getInstance();

        initUI();

        // setting onclick listener for generate OTP button.
        btnVerifyPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtPhoneNumber.getText().toString())) {
                    edtPhoneNumber.setError("Please enter a valid phone number.");
                } else {
                    String phone = edtPhoneNumber.getText().toString();
                    if (phone.charAt(0) == '0') {
                        phone = "+84" + phone.substring(1);
                        checkAccountUsingPhone(phone);

                    } else {
                        edtPhoneNumber.setError("Sai định dạng số điện thoại.");
                    }

                }
            }
        });

        // initializing on click listener
        // for verify otp button
        btnSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validating if the OTP text field is empty or not.
                if (TextUtils.isEmpty(edtOTP.getText().toString())) {
                    // if the OTP text field is empty display
                    // a message to user to enter OTP
                    edtOTP.setError("Please enter OTP");
                } else {
                    // if OTP field is not empty calling
                    // method to verify the OTP.
                    verifyCode(edtOTP.getText().toString());
                }
            }
        });
    }

    private void initUI() {
        edtPhoneNumber = findViewById(R.id.edit_text_phone_number);
        btnVerifyPhone = findViewById(R.id.btn_verify_phone_number);
        edtOTP = findViewById(R.id.edit_text_otp);
        btnSendOTP = findViewById(R.id.btn_enter_otp);
    }

    private void checkAccountUsingPhone(String phone) {
        AccountApi.accountApi.getAccountByPhone(phone).enqueue(
                new Callback<List<Account>>() {
                    @Override
                    public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                        List<Account> list = response.body();
                        if (list.size() == 0) {
                            edtPhoneNumber.setError("Số điện thoại không tồn tại trong hệ thống");
                            return;
                        }
                        AccountManager accountManager = CartManagerSingleton.getAccountManagerInstance(VerifyPhoneNumberActivity.this);
                        accountManager.saveAccount(list.get(0));
                        sendVerificationCode(phone);
                    }

                    @Override
                    public void onFailure(Call<List<Account>> call, Throwable t) {
                        edtOTP.setError("");
                        Toast.makeText(VerifyPhoneNumberActivity.this, "Đăng nhập không thành công.", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(VerifyPhoneNumberActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationId = s;
                            }

                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                                final String code = phoneAuthCredential.getSmsCode();
                                if (code != null) {
                                    edtOTP.setText(code);

                                    verifyCode(code);
                                }
                            }

                            @Override
                            public void onVerificationFailed(FirebaseException e) {
                                // displaying error message with firebase exception.
                                Toast.makeText(VerifyPhoneNumberActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                AccountManager accountManager = CartManagerSingleton.getAccountManagerInstance(VerifyPhoneNumberActivity.this);
                                accountManager.logOut();
                            }
                        })           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }
}