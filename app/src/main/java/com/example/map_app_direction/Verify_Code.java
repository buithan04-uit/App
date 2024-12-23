package com.example.map_app_direction;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.map_app_direction.API.RetrofitClient;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Verify_Code extends AppCompatActivity {
    private static final String TAG = "VerifyCodeActivity";
    EditText code1 ;
    EditText code2 ;
    EditText code3 ;
    EditText code4 ;
    EditText code5 ;
    EditText code6 ;

    EditText[] editTexts = {code1, code2, code3, code4, code5, code6};


    private String email;
    private TextView errorText;
    private Boolean error;

    Animation animation;
    LinearLayout linearLayout , linearLayout1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_code);

        animation = AnimationUtils.loadAnimation(this , R.anim.top_animation);

        linearLayout = findViewById(R.id.linear);
        linearLayout1 = findViewById(R.id.linear1);

        linearLayout.setAnimation(animation);
        linearLayout1.setAnimation(animation);



        Intent intent = getIntent();
        email  = intent.getStringExtra("email");

        errorText = findViewById(R.id.error);

        // Tham chiếu các EditText
         code1 = findViewById(R.id.Code);
         code2 = findViewById(R.id.Code1);
         code3 = findViewById(R.id.Code2);
         code4 = findViewById(R.id.Code3);
         code5 = findViewById(R.id.Code4);
         code6 = findViewById(R.id.Code5);


        // Đặt các EditText vào mảng để dễ quản lý
        EditText[] editTexts = {code1, code2, code3, code4, code5, code6};

        // Thêm TextWatcher để tự động chuyển EditText
        for (int i = 0; i < editTexts.length; i++) {
            final int currentIndex = i;
            editTexts[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // Chuyển sang EditText tiếp theo khi có 1 ký tự
                    if (s.length() == 1 && currentIndex < editTexts.length - 1) {
                        editTexts[currentIndex + 1].requestFocus();
                    }
                    // Quay lại EditText trước nếu xóa ký tự
                    else if (s.length() == 0 && currentIndex > 0) {
                        editTexts[currentIndex - 1].requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_verify), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void BackBtn (View view){
        Intent intent = new Intent(this , ForgotPassword.class);
        startActivity(intent);
        finish();
    }
    public void ResendBtn (View view){
        //Do something
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .resend(email);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try (ResponseBody responseBody = response.body() ) {
                        String responseBodyString = response.body().string();

                        Toast.makeText(Verify_Code.this, responseBodyString, Toast.LENGTH_LONG).show();

                    } catch (IOException e) {
                        Log.e(TAG, "Error reading response", e);
                        Toast.makeText(Verify_Code.this, "Error reading response", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.w(TAG, "Response not successful: " + response.message());
                    Toast.makeText(Verify_Code.this, "Response not successful: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage(), t);
                Toast.makeText(Verify_Code.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void EnterCodeBtn (View view){
        //Do something
        Boolean hasError = false;
        if (code1.getText().toString().trim().isEmpty()) {
            errorText.setText("* Please complete code !");
            hasError = true;
        } else {
            errorText.setText("");
        }
        if (code2.getText().toString().trim().isEmpty()) {
            errorText.setText("* Please complete code !");
            hasError = true;
        } else {
            errorText.setText("");
        }
        if (code3.getText().toString().trim().isEmpty()) {
            errorText.setText("* Please complete code !");
            hasError = true;
        } else {
            errorText.setText("");
        }
        if (code4.getText().toString().trim().isEmpty()) {
            errorText.setText("* Please complete code !");
            hasError = true;
        } else {
            errorText.setText("");
        }
        if (code5.getText().toString().trim().isEmpty() ) {
            errorText.setText("* Please complete code !");
            hasError = true;
        } else {
            errorText.setText("");
        }
        if (code6.getText().toString().trim().isEmpty()) {
            errorText.setText("* Please complete code !");
            hasError = true;
        } else {
            errorText.setText("");
        }
        if (hasError){
            return;
        }

        String code = code1.getText().toString().trim() + code2.getText().toString().trim() + code3.getText().toString().trim()+ code4.getText().toString().trim() + code5.getText().toString().trim() + code6.getText().toString().trim();
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .verifycode(email , code);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try (ResponseBody responseBody = response.body()) {
                        String responseBodyString = response.body().string();
                        // Kiểm tra nội dung của responseBodyString để xác định xem có lỗi hay không
                        error = responseBodyString.contains("Error");
                        if (error){
                            Toast.makeText(Verify_Code.this, "Verify Code is incorrect!", Toast.LENGTH_LONG).show();
                            errorText.setText("Verify Code is incorrect!");
                        }
                        else {
                            Intent intent = new Intent(Verify_Code.this , ResetPassword.class);
                            intent.putExtra("email" , email);
                            Toast.makeText(Verify_Code.this, "Verify Code is correct!", Toast.LENGTH_LONG).show();
                            errorText.setText("");
                            startActivity(intent);
                            finish();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Error reading response", e);
                        Toast.makeText(Verify_Code.this, "Error reading response", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.w(TAG, "Response not successful: " + response.message());
                    Toast.makeText(Verify_Code.this, "Response not successful: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage(), t);
                Toast.makeText(Verify_Code.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}