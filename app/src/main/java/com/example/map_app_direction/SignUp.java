package com.example.map_app_direction;

import static android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.example.map_app_direction.Module.User;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    private EditText firstnameText, lastnameText, emailText, passwordText , confirmpasswordText;
    private TextView errorText , errorText1 , errorText2 ,errorText3 , errorText4;
    private Boolean error, error1;
    private ImageView imageView1 , imageView2;
    private User user;
    private Boolean isPasswordVisible = false;
    private Boolean isPasswordVisible1 = false;
    Animation animation;

    LinearLayout linearLayout1 ,linearLayout2 , linearLayout3 , linearLayout4 ,linearLayout5 ;
    TextView text1 , text2 , text3,text4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        firstnameText = findViewById(R.id.firstname);
        lastnameText = findViewById(R.id.lastname);
        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        confirmpasswordText = findViewById(R.id.confirmpassword);
        imageView1 = findViewById(R.id.image1);
        imageView2 = findViewById(R.id.image2);

        errorText = findViewById(R.id.errortext);
        errorText1 = findViewById(R.id.errortext1);
        errorText2 = findViewById(R.id.errortext2);
        errorText3 = findViewById(R.id.errortext3);
        errorText4 = findViewById(R.id.errortext4);
        errorText.setText("");
        errorText1.setText("");
        errorText2.setText("");
        errorText3.setText("");
        errorText4.setText("");


        animation = AnimationUtils.loadAnimation(this , R.anim.slide_in_left_fade_in);

        linearLayout1 = findViewById(R.id.linear1);
        linearLayout2 = findViewById(R.id.linear2);
        linearLayout3 = findViewById(R.id.linear3);
        linearLayout4 = findViewById(R.id.linear4);
        linearLayout5 = findViewById(R.id.linear5);

        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);

        linearLayout1.setAnimation(animation);
        linearLayout2.setAnimation(animation);
        linearLayout3.setAnimation(animation);
        linearLayout4.setAnimation(animation);
        linearLayout5.setAnimation(animation);

        text1.setAnimation(animation);
        text2.setAnimation(animation);
        text3.setAnimation(animation);
        text4.setAnimation(animation);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_sign_up), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void LoginBtn (View view){
        Intent intent = new Intent(this , Login.class);
        startActivity(intent);
        finish();
    }

    public void BtnHide_Onclick (View view){
        if (isPasswordVisible) {
            // Ẩn mật khẩu
            passwordText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imageView1.setImageResource(R.drawable.baseline_remove_red_eye_24);
        } else {
            // Hiển thị mật khẩu
            passwordText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            imageView1.setImageResource(R.drawable.baseline_visibility_off_24);
        }
        passwordText.setSelection(passwordText.getText().length());
        isPasswordVisible = !isPasswordVisible;
    }
    public void BtnHide_Onclick1 (View view){
        if (isPasswordVisible1) {
            // Ẩn mật khẩu
            confirmpasswordText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imageView2.setImageResource(R.drawable.baseline_remove_red_eye_24);
        } else {
            // Hiển thị mật khẩu
            confirmpasswordText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            imageView2.setImageResource(R.drawable.baseline_visibility_off_24);
        }
        confirmpasswordText.setSelection(confirmpasswordText.getText().length());
        isPasswordVisible1 = !isPasswordVisible1;
    }
    public boolean ValidInfor(String firstName , String lastName , String email , String password , String confirmpassword){
        boolean hasError = false;
        if (firstName.isEmpty()) {
            errorText3.setText("First name is required.");
            hasError = true;
        } else {
            errorText3.setText("");
        }

        if (lastName.isEmpty()) {
            errorText4.setText("Last name is required.");
            hasError = true;
        } else {
            errorText4.setText("");
        }

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

        if (confirmpassword.isEmpty()) {
            errorText2.setText("Confirm password is required.");
            hasError = true;
        } else if (!password.equals(confirmpassword)) {
            errorText2.setText("* Confirm password is not correct.");
            hasError = true;
        } else {
            errorText2.setText("");
        }
        return hasError;
    }

    public void CreateAccountBtn (View view){
        //do sonmething
        Log.d(TAG, "SignUp button clicked");
        String firstName = firstnameText.getText().toString().trim();
        String lastName = lastnameText.getText().toString().trim();
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        String confirmpassword = confirmpasswordText.getText().toString().trim();
        String sdt = "";
        boolean hasError = ValidInfor(firstName , lastName , email , password , confirmpassword);

        if (hasError) {
            return; // Dừng lại nếu có lỗi
        }
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .createUser(firstName , lastName , email , sdt , password);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try (ResponseBody responseBody = response.body()) {
                        String responseBodyString = response.body().string();
                        // Kiểm tra nội dung của responseBodyString để xác định xem có lỗi hay không
                        error = responseBodyString.contains("Error1");
                        error1 = responseBodyString.contains("Error2");
                        Log.d(TAG, "Response: " + responseBodyString);
                        if (error){
                            Toast.makeText(SignUp.this, "Email is used !", Toast.LENGTH_LONG).show();
                            errorText.setText("Email is used !");
                        } else if (error1) {
                            Toast.makeText(SignUp.this, "Email not exist !", Toast.LENGTH_LONG).show();
                            errorText.setText("Email not exist !");
                        } else {
                            Toast.makeText(SignUp.this, "User created successfully !", Toast.LENGTH_LONG).show();
                            errorText.setText("");
                            errorText1.setText("");
                            errorText2.setText("");
                            startActivity(new Intent(SignUp.this , Login.class));
                            finish();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Error reading response", e);
                        Toast.makeText(SignUp.this, "Error reading response", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.w(TAG, "Response not successful: " + response.message());
                    Toast.makeText(SignUp.this, "Response not successful: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage(), t);
                Toast.makeText(SignUp.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}