package com.smirnova.odesatracker.start.about;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.smirnova.odesatracker.Constants;
import com.smirnova.odesatracker.start.Login;
import com.smirnova.odesatracker.R;
import com.smirnova.odesatracker.start.SignUp;

public class AboutMain extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Button startBtn;
    private ImageView leftBtn;
    private ImageView rightBtn;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_about);

        viewPager = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tab_layout);
        startBtn = findViewById(R.id.getStartedBtn);
        leftBtn = findViewById(R.id.btnLeft);
        rightBtn = findViewById(R.id.btnRight);

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


        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putBoolean(Constants.FIRST_LOAD, true);
                editor.apply();
                Intent intent = new Intent(AboutMain.this, SignUp.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });



        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.alpha));
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < 3; i++) {
            if(i == 0)
                tabLayout.getTabAt(i).setIcon(R.drawable.ic_ellipse_taken);
            else
                tabLayout.getTabAt(i).setIcon(R.drawable.ic_ellipse_untake);
        }


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < 3; i++) {
                    if(position == i)
                        tabLayout.getTabAt(i).setIcon(R.drawable.ic_ellipse_taken);
                    else
                        tabLayout.getTabAt(i).setIcon(R.drawable.ic_ellipse_untake);
                }

                if(position == 2) {
                    startBtn.setVisibility(View.VISIBLE);
                    rightBtn.setVisibility(View.GONE);
                } else if(position == 0) {
                    leftBtn.setVisibility(View.GONE);
                }
                else {
                    rightBtn.setVisibility(View.VISIBLE);
                    leftBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
