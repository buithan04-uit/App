package com.example.map_app_direction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class Welcome extends AppCompatActivity {
    private boolean loginFlag;
    private boolean firstAccess;

    Animation topAnim , botAnim;
    ImageView imageView;
    TextView warningTxt , buttontxt , textTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadData();
        if (loginFlag){
            Intent intent = new Intent(this , MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if (firstAccess){
            Intent intent = new Intent(this , Welcom1.class);
            startActivity(intent);
            finish();
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);

        imageView = findViewById(R.id.image);
        warningTxt = findViewById(R.id.WarningText);
        buttontxt = findViewById(R.id.button);
        textTxt = findViewById(R.id.logo);

        topAnim = AnimationUtils.loadAnimation(this , R.anim.top_animation);
        botAnim = AnimationUtils.loadAnimation(this , R.anim.bot_animation);

        imageView.setAnimation(topAnim);
        warningTxt.setAnimation(botAnim);
        buttontxt.setAnimation(botAnim);
        textTxt.setAnimation(botAnim);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void BtnNext_Onclick (View view){
        Intent intent = new Intent(this, Welcom1.class);
        startActivity(intent);
        finish();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("EmailUser", MODE_PRIVATE);
        loginFlag = sharedPreferences.getBoolean("login", false);
        firstAccess = sharedPreferences.getBoolean("firstaccess", false);

    }

}