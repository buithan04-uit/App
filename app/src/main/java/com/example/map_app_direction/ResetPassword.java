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

public class ResetPassword extends AppCompatActivity {
    private static final String TAG = "ResetPasswordActivity";
    private EditText passwordText , confirmpasswordText;
    private TextView errorText , errorText1;
    private Boolean error , error1;
    private String email;
    LinearLayout linearLayout ,linearLayout1;

    Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);

        animation = AnimationUtils.loadAnimation(this , R.anim.top_animation);


        linearLayout = findViewById(R.id.linear);
        linearLayout1 = findViewById(R.id.linear1);

        linearLayout.setAnimation(animation);
        linearLayout1.setAnimation(animation);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        passwordText =  findViewById(R.id.password);
        confirmpasswordText = findViewById(R.id.confirmpassword);

        errorText = findViewById(R.id.error);
        errorText1 = findViewById(R.id.error1);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_reset), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void BackBtn (View view){
        Intent intent = new Intent(this , Verify_Code.class);
        startActivity(intent);
        finish();
    }
    public void ConfirmBtn (View view){
        //Do something
        String password = passwordText.getText().toString().trim();
        String confirmpassword = confirmpasswordText.getText().toString().trim();

        Boolean hasError = false;

        if (password.isEmpty()) {
            errorText.setText("Password is required.");
            hasError = true;
        } else {
            errorText.setText("");
        }

        if (confirmpassword.isEmpty()) {
            errorText1.setText("Confirm password is required.");
            hasError = true;
        }
        else if (!confirmpassword.equals(password)){
            errorText1.setText("Confirm password is not correct!");
            hasError = true;
        }
        else {
            errorText1.setText("");
        }

        if (hasError){
            return;
        }

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .resetpassword( email ,password);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try (ResponseBody responseBody = response.body()) {
                        String responseBodyString = response.body().string();
                        Toast.makeText(ResetPassword.this, responseBodyString, Toast.LENGTH_LONG).show();
                        errorText.setText("");
                        errorText1.setText("");
                        startActivity(new Intent(ResetPassword.this , ResetSuccess.class));
                        finish();
                    } catch (IOException e) {
                        Log.e(TAG, "Error reading response", e);
                        Toast.makeText(ResetPassword.this, "Error reading response", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.w(TAG, "Response not successful: " + response.message());
                    Toast.makeText(ResetPassword.this, "Response not successful: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage(), t);
                Toast.makeText(ResetPassword.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}