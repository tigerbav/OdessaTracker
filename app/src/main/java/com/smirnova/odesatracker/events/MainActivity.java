package com.smirnova.odesatracker.events;


import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.smirnova.odesatracker.DataBase;
import com.smirnova.odesatracker.R;

public class MainActivity extends AppCompatActivity implements CallExitSlider {
    private EditText editSearch;
    private ImageView home, search, saved, filters, profile;
    public static FragmentTransaction fragmentTransaction;
    public static FragmentManager supportFragmentManager;

    private DataBase dataBase;

    private Home homeObject;
    private Profile profileObject;
    private Filters filtersObject;
    private Saved savedObject;
    private Search searchObject;

    private ImageView sliderBtn;
    private NavigationLayout navigationLayout;
    private RelativeLayout left_drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editSearch = findViewById(R.id.editSearch);
        home = findViewById(R.id.home);
        search = findViewById(R.id.search);
        saved = findViewById(R.id.saved);
        filters = findViewById(R.id.filtres);
        profile = findViewById(R.id.profile);

        homeObject = new Home();
        profileObject = new Profile();
        filtersObject = new Filters();
        savedObject = new Saved();
        searchObject = new Search();

        sliderBtn = findViewById(R.id.slider_btn);
        left_drawer = findViewById(R.id.left_drawer);

        dataBase = DataBase.createOrReturn();
        dataBase.createUser();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.relativeLayout, homeObject).commit();

        supportFragmentManager = getSupportFragmentManager();

        home.setOnClickListener(view -> {
            setStartIcons();
            home.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_on));
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.relativeLayout, homeObject).commit();
        });
        search.setOnClickListener(view -> {
            setStartIcons();
            search.setImageDrawable(getResources().getDrawable(R.drawable.ic_search_on));
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.relativeLayout, searchObject).commit();
        });
        saved.setOnClickListener(view -> {
            setStartIcons();
            saved.setImageDrawable(getResources().getDrawable(R.drawable.ic_saved_on));
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.relativeLayout, savedObject).commit();
        });
        filters.setOnClickListener(view -> {

            if (NavigationLayout.counter == 0) {
                left_drawer.setVisibility(View.VISIBLE);
            } else {
                setStartIcons();
                filters.setImageDrawable(getResources().getDrawable(R.drawable.ic_filters_on));
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.relativeLayout, filtersObject).commit();
            }

        });
        profile.setOnClickListener(view -> {
            setStartIcons();
            profile.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_on));
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.relativeLayout, profileObject).commit();
        });
        setupMenu();
        sliderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                left_drawer.setVisibility(View.VISIBLE);
            }
        });

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
    }


    public void setupMenu() {
        navigationLayout = new NavigationLayout(getApplicationContext(), left_drawer);
        navigationLayout.setCallExit(this);
        left_drawer.addView(navigationLayout);
    }

    private void setStartIcons() {
        home.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_off));
        search.setImageDrawable(getResources().getDrawable(R.drawable.ic_search_off));
        saved.setImageDrawable(getResources().getDrawable(R.drawable.ic_saved_off));
        filters.setImageDrawable(getResources().getDrawable(R.drawable.ic_filters_off));
        profile.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_off));
    }

    @Override
    public void onBackPressed() {
        backClick();
    }

    @Override
    public void close() {
        backClick();
    }

    @Override
    public void filtersWindow() {
        if (NavigationLayout.counter == 0) {
            Toast.makeText(this, "Need to choose more then 0 item", Toast.LENGTH_SHORT).show();
        } else {
            setStartIcons();
            filters.setImageDrawable(getResources().getDrawable(R.drawable.ic_filters_on));

            filtersObject = null;
            filtersObject = new Filters();

            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.relativeLayout, filtersObject).commit();
        }

    }

    private void backClick() {
        if (left_drawer.getVisibility() == View.VISIBLE) {
            left_drawer.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }
}