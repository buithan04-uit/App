package com.example.map_app_direction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class Profile extends AppCompatActivity {
    private boolean loginFlag;
    private String emailGlobal;
    private TextView Nametxt , Gmailtxt;
    private String firstName , lastName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        loadData();

        Gmailtxt = findViewById(R.id.Emailtxt);
        Gmailtxt.setText(emailGlobal);
        Nametxt = findViewById(R.id.Name);
        Nametxt.setText(firstName + " " + lastName);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void BtnBack_Onclick(View view){
        Intent intent = new Intent(this , MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void BtnMyaccount_Onclick(View view){
        Intent intent = new Intent(this , MyAccount.class);
        startActivity(intent);
    }
    public void BtnLogout_Onclick(View view) {
        loginFlag = false;
        saveData();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("EmailUser", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("login", loginFlag);
        editor.apply();
    }
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("EmailUser", MODE_PRIVATE);
        Gson gson = new Gson();
        String EmailJson = sharedPreferences.getString("email", null);
        String FirstNameJson = sharedPreferences.getString("firstname", null);
        String LastNameJson = sharedPreferences.getString("lastname", null);
        Type EmailType = new TypeToken<String>() {}.getType();
        Type FirstNameType = new TypeToken<String>() {}.getType();
        Type LastNameType = new TypeToken<String>() {}.getType();// Sửa lại TypeToken để phù hợp với kiểu dữ liệu String
        emailGlobal = gson.fromJson(EmailJson, EmailType);
        firstName = gson.fromJson(FirstNameJson, FirstNameType);
        lastName = gson.fromJson(LastNameJson, LastNameType);
    }
}