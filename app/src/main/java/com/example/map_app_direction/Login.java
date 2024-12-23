package com.example.map_app_direction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText emailText , passwordText;
    private TextView errorText , errorText1;
    private Boolean error , error1;
    private String message;
    private String emailGlobal;
    private String firstName , lastName;
    private boolean loginFlag;
    private ImageView imageView ;
    private Boolean isPasswordVisible = false;
    Animation animation;
    TextView text1 , text2 , text3 ,text4;

    LinearLayout linear1 , linear2 , linear3 ,  btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        animation = AnimationUtils.loadAnimation(this , R.anim.slide_in_left_fade_in);

        imageView = findViewById(R.id.image);

        linear1 = findViewById(R.id.linear1);
        linear2 = findViewById(R.id.linear2);
        linear3 = findViewById(R.id.linear3);
        btnLogin = findViewById(R.id.linear4);

        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text5);
        text4 = findViewById(R.id.text6);

        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);

        linear1.setAnimation(animation);
        linear2.setAnimation(animation);
        linear3.setAnimation(animation);
        btnLogin.setAnimation(animation);
        text1.setAnimation(animation);
        text2.setAnimation(animation);
        text3.setAnimation(animation);
        text4.setAnimation(animation);





        errorText = findViewById(R.id.error);
        errorText1 = findViewById(R.id.error1);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void SignUpBtn (View vIew){
        Intent intent = new Intent(this , SignUp.class);
        startActivity(intent);
        finish();
    }
    public boolean ValidInfor(String email , String password){
        boolean hasError = false;

        if (email.isEmpty()) {
            errorText.setText("Email is required.");
            hasError = true;
        } else {
            errorText.setText("");
        }

        if (password.isEmpty()) {
            errorText1.setText("Password is required.");
            hasError = true;
        } else {
            errorText1.setText("");
        }
        return hasError;
    }
    public void SignInBtn (View view){
        // Do something
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        boolean hasError = ValidInfor(email , password);

        if (hasError) {
            return; // Dừng lại nếu có lỗi
        }
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .login(email,password);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try (ResponseBody responseBody = response.body()) {
                        String responseBodyString = response.body().string();
                        // Kiểm tra nội dung của responseBodyString để xác định xem có lỗi hay không
                        error = responseBodyString.contains("Error");
                        error1 = responseBodyString.contains("Err1");
                        if (error){
                            Toast.makeText(Login.this, "Email not found !", Toast.LENGTH_LONG).show();
                            errorText.setText("Email not found !");
                        }
                        else if(error1){
                            Toast.makeText(Login.this, "Password is not correct !", Toast.LENGTH_LONG).show();
                            errorText1.setText("Password is not correct !");
                        }
                        else {
                            emailGlobal = email;
                            String Temp = responseBodyString.substring(1, responseBodyString.length() - 1);
                            String[] parts = Temp.split(",");
                            firstName = parts[0];
                            lastName = parts[1];
                            loginFlag = true;
                            saveData();
                            Toast.makeText(Login.this, "Login Successfully !", Toast.LENGTH_LONG).show();
                            errorText.setText("");
                            errorText1.setText("");
                            startActivity(new Intent(Login.this , MainActivity.class));
                            finish();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Error reading response", e);
                        Toast.makeText(Login.this, "Error reading response", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.w(TAG, "Response not successful: " + response.message());
                    Toast.makeText(Login.this, "Response not successful: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage(), t);
                Toast.makeText(Login.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
    public void BtnHide_Onclick (View view){
        if (isPasswordVisible) {
            // Ẩn mật khẩu
            passwordText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imageView.setImageResource(R.drawable.baseline_remove_red_eye_24);
        } else {
            // Hiển thị mật khẩu
            passwordText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            imageView.setImageResource(R.drawable.baseline_visibility_off_24);
        }
        passwordText.setSelection(passwordText.getText().length());
        isPasswordVisible = !isPasswordVisible;
    }
    public void ForgetBtn (View view){
        //do sonmething

        Intent intent = new Intent(this , ForgotPassword.class);
        startActivity(intent);
        finish();
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("EmailUser", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String EmailJson = gson.toJson(emailGlobal);
        String FirstNameJson = gson.toJson(firstName);
        String LastnameJson = gson.toJson(lastName);

        editor.putString("email", EmailJson);
        editor.putString("firstname", FirstNameJson);
        editor.putString("lastname", LastnameJson);
        editor.putBoolean("login", loginFlag);
        editor.apply();
    }


}