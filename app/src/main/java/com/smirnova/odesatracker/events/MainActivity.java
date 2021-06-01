package com.smirnova.odesatracker.events;


import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.smirnova.odesatracker.DataBase;
import com.smirnova.odesatracker.R;

public class MainActivity extends AppCompatActivity {
    private EditText editSearch;
    private ImageView home, search, saved, reviews, profile;
    private FragmentTransaction fragmentTransaction;

    private DataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editSearch = findViewById(R.id.editSearch);
        home = findViewById(R.id.home);
        search = findViewById(R.id.search);
        saved = findViewById(R.id.saved);
        reviews = findViewById(R.id.reviews);
        profile = findViewById(R.id.profile);

        home.setOnClickListener(view -> {
            setStartIcons();
            home.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_on));
        });
        search.setOnClickListener(view -> {
            setStartIcons();
            search.setImageDrawable(getResources().getDrawable(R.drawable.ic_search_on));
        });
        saved.setOnClickListener(view -> {
            setStartIcons();
            saved.setImageDrawable(getResources().getDrawable(R.drawable.ic_saved_on));
        });
        reviews.setOnClickListener(view -> {
            setStartIcons();
            reviews.setImageDrawable(getResources().getDrawable(R.drawable.ic_reviews_on));
        });
        profile.setOnClickListener(view -> {
            setStartIcons();
            profile.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_on));
        });

        dataBase = DataBase.createOrReturn();
    }

    private void setStartIcons() {
        home.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_off));
        search.setImageDrawable(getResources().getDrawable(R.drawable.ic_search_off));
        saved.setImageDrawable(getResources().getDrawable(R.drawable.ic_saved_off));
        reviews.setImageDrawable(getResources().getDrawable(R.drawable.ic_reviews_off));
        profile.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_off));
    }
}