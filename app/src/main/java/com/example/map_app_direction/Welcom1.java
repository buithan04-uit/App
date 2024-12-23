package com.example.map_app_direction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

public class Welcom1 extends AppCompatActivity {
    private Boolean FirstAccess;
    TextView text1 , text2 , text3 , btnSignup , btnLogin;
    Animation animation1 , animation2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcom1);

        animation1 = AnimationUtils.loadAnimation(this ,R.anim.top_animation);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.bot_animation);

        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);


        btnSignup = findViewById(R.id.btnSignup);
        btnLogin = findViewById(R.id.btnlogin);

        text1.setAnimation(animation1);
        text2.setAnimation(animation1);

        btnSignup.setAnimation(animation2);
        btnLogin.setAnimation(animation2);
        text3.setAnimation(animation2);

        FirstAccess = true;
        saveData();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void BtnLogin_Onclick (View view){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
    public void BtnSignup_Onclick (View view){
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
        finish();
    }
    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("EmailUser", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("firstaccess", FirstAccess);
        editor.apply();
    }
}