package com.smirnova.odesatracker.events;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.smirnova.odesatracker.R;

public class Home extends AppCompatActivity {
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);

        linearLayout = findViewById(R.id.scrollView);
    }
}
