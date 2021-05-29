package com.smirnova.odesatracker.start;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.smirnova.odesatracker.Constants;
import com.smirnova.odesatracker.R;
import com.smirnova.odesatracker.start.about.AboutMain;

public class Logo extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    private TextView welcomeText;
    private ImageView logo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_logo);

        logo = findViewById(R.id.logo);
        welcomeText = findViewById(R.id.welcomeText);


        welcomeText.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animation_alpha_translate));
        logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animation_alpha));

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        int currentApiVersion = Build.VERSION.SDK_INT;
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(visibility -> {
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            decorView.setSystemUiVisibility(flags);
                        }
                    });
        }


        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            if(FirebaseAuth.getInstance().getCurrentUser() != null ){
//                Intent intent = new Intent(Logo.this, MainActivity.class);
//
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//            }
            //else{
            Intent intent;
            if (sharedPreferences.getBoolean(Constants.FIRST_LOAD, false)) {
                intent = new Intent(Logo.this, Login.class);
            } else {
                intent = new Intent(Logo.this, AboutMain.class);
            }

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//удаляем с памяти активити,
            startActivity(intent);
            //}

        }).start();
    }
}
