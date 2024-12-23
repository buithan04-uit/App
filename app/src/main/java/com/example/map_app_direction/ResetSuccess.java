package com.example.map_app_direction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ResetSuccess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_success);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_reset_success), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void BackBtn (View view){
        Intent intent = new Intent(this , Login.class);
        startActivity(intent);
    }

    public void BackLoginBtn (View view){
        Intent intent = new Intent(this , Login.class);
        startActivity(intent);
    }
}