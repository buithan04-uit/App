package com.example.map_app_direction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.map_app_direction.API.RetrofitClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity {
    private static final String TAG = "ChangePasswordActivity";
    private EditText oldpasswordText ,passwordText , confirmpasswordText;
    private TextView errorText , errorText1,errorText2;
    private Boolean error;
    private String emailGlobal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);

        loadData();

        oldpasswordText = findViewById(R.id.Oldpassword);
        passwordText = findViewById(R.id.Newpassword);
        confirmpasswordText = findViewById(R.id.confirmpassword);

        errorText = findViewById(R.id.error);
        errorText1 = findViewById(R.id.error1);
        errorText2 = findViewById(R.id.error2);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainchange), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void BackBtn (View view){
        Intent intent = new Intent(this , MyAccount.class);
        startActivity(intent);
    }
    public void ChangeBtn (View view){
        //Do something
        String oldpassword = oldpasswordText.getText().toString().trim();
        String newpassword = passwordText.getText().toString().trim();
        String confirmpassword = confirmpasswordText.getText().toString().trim();

        Boolean hasError = false;

        if (oldpassword.isEmpty()) {
            errorText.setText("Old Password is required.");
            hasError = true;
        } else {
            errorText.setText("");
        }

        if (newpassword.isEmpty()) {
            errorText.setText("New Password is required.");
            hasError = true;
        } else {
            errorText.setText("");
        }

        if (confirmpassword.isEmpty()) {
            errorText1.setText("Confirm password is required.");
            hasError = true;
        }
        else if (!confirmpassword.equals(newpassword)){
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
                .changepassword(emailGlobal,oldpassword , newpassword);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try (ResponseBody responseBody = response.body()) {
                        String responseBodyString = response.body().string();
                        Toast.makeText(ChangePassword.this, responseBodyString, Toast.LENGTH_LONG).show();
                        error = responseBodyString.contains("Error");
                        Log.d(TAG, "Response: " + responseBodyString);
                        if (error){
                            Toast.makeText(ChangePassword.this, "Old password is not correct !", Toast.LENGTH_LONG).show();
                            errorText.setText("Old password is not correct !");
                        } else {
                            Toast.makeText(ChangePassword.this, "Change Password successfully !", Toast.LENGTH_LONG).show();
                            errorText.setText("");
                            errorText1.setText("");
                            errorText2.setText("");
                            Intent intent = new Intent(ChangePassword.this , MyAccount.class);
                            startActivity(intent);
                            finish();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Error reading response", e);
                        Toast.makeText(ChangePassword.this, "Error reading response", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.w(TAG, "Response not successful: " + response.message());
                    Toast.makeText(ChangePassword.this, "Response not successful: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage(), t);
                Toast.makeText(ChangePassword.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("EmailUser", MODE_PRIVATE);
        Gson gson = new Gson();
        String EmailJson = sharedPreferences.getString("email", null);
        Type EmailType = new TypeToken<String>() {}.getType(); // Sửa lại TypeToken để phù hợp với kiểu dữ liệu String
        emailGlobal = gson.fromJson(EmailJson, EmailType);
    }
}