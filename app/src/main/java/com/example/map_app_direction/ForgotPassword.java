package com.example.map_app_direction;

import android.content.Intent;
import android.os.Bundle;
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

public class ForgotPassword extends AppCompatActivity {
    private static final String TAG = "ForgetPasswordActivity";
    private EditText emailText;
    private TextView errorText;
    private Boolean error;
    LinearLayout linearLayout , linearLayout1;

    Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        animation = AnimationUtils.loadAnimation(this , R.anim.top_animation);

        emailText = findViewById(R.id.email);
        errorText = findViewById(R.id.error);
        linearLayout = findViewById(R.id.linear);
        linearLayout1 = findViewById(R.id.linear1);

        linearLayout.setAnimation(animation);
        linearLayout1.setAnimation(animation);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_forgot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void BackBtn (View view){
        Intent intent = new Intent(this , Login.class);
        startActivity(intent);
        finish();
    }
    public void NextBtn (View view) {
        //Do something
        String email = emailText.getText().toString().trim();
        Boolean hasError = false;

        if (email.isEmpty()) {
            errorText.setText("Email is required.");
            hasError = true;
        } else {
            errorText.setText("");
        }
        if (hasError){
            return;
        }

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .forgotpassword(email);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try (ResponseBody responseBody = response.body()) {
                        String responseBodyString = response.body().string();
                        // Kiểm tra nội dung của responseBodyString để xác định xem có lỗi hay không
                        error = responseBodyString.contains("Error");
                        if (error){
                            Toast.makeText(ForgotPassword.this, "Email not Exist !", Toast.LENGTH_LONG).show();
                            errorText.setText("Email not Exist !");
                        }
                        else {
                            Toast.makeText(ForgotPassword.this, "Send code successfully !", Toast.LENGTH_LONG).show();
                            errorText.setText("");
                            Intent intent = new Intent(ForgotPassword.this , Verify_Code.class);
                            intent.putExtra("email" ,emailText.getText().toString().trim());
                            startActivity(intent);
                            finish();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Error reading response", e);
                        Toast.makeText(ForgotPassword.this, "Error reading response", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.w(TAG, "Response not successful: " + response.message());
                    Toast.makeText(ForgotPassword.this, "Response not successful: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage(), t);
                Toast.makeText(ForgotPassword.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
    public void LoginBtn (View view){
        Intent intent = new Intent(this , Login.class);
        startActivity(intent);
        finish();
    }
}