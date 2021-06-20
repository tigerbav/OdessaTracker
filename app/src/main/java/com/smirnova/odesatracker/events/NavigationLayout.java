package com.smirnova.odesatracker.events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.smirnova.odesatracker.DataBase;
import com.smirnova.odesatracker.R;

public class NavigationLayout extends RelativeLayout {
    private CheckBox parks;
    private CheckBox museums;
    private CheckBox attractions;
    private CheckBox selectAll;
    private CheckBox excursions;
    private CheckBox theaters;
    private CheckBox cityEvents;
    private CheckBox cinemas;
    private CheckBox concerts;
    private CheckBox cafes;
    private Button back;

    private Button apply;

    public static int counter = 0;

    private CallExitSlider callExitSlider;
    private CheckBox[] checkBoxes;

    private DataBase dataBase;

    public NavigationLayout(Context context, RelativeLayout parent) {
        super(context);
        initView(context, parent);
    }

    public void initView(final Context context, RelativeLayout parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_drawer_layout, parent, true);

        dataBase = DataBase.createOrReturn();

        parks = view.findViewById(R.id.checkBoxPark);
        museums = view.findViewById(R.id.checkBoxMuseums);
        attractions = view.findViewById(R.id.checkBoxAttractions);
        selectAll = view.findViewById(R.id.checkBoxSelectAll);
        excursions = view.findViewById(R.id.checkBoxExcursions);
        theaters = view.findViewById(R.id.checkBoxTheaters);
        cityEvents = view.findViewById(R.id.checkBoxCityEvents);
        cinemas = view.findViewById(R.id.checkBoxCinemas);
        concerts = view.findViewById(R.id.checkBoxConcerts);
        cafes = view.findViewById(R.id.checkBoxСafes);

        back = view.findViewById(R.id.back_btn_slider);
        apply = view.findViewById(R.id.applyFilters);


        checkBoxes = new CheckBox[]{parks, museums, attractions, excursions,
                theaters, cityEvents, cinemas, concerts, cafes};

        for (CheckBox checkBox : checkBoxes) {
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        counter++;
                    } else {
                        counter--;
                    }
                }
            });
        }
        selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                for (CheckBox checkBox : checkBoxes) {
                    checkBox.setChecked(b);
                }
            }
        });

        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callExitSlider != null) {
                    callExitSlider.close();
                }
            }
        });

        apply.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBase.filterView.clear();
                if (callExitSlider != null) {
                    for (CheckBox checkBox : checkBoxes) {
                        if (checkBox.isChecked()) {
                            dataBase.createFilterList(checkBox.getText().toString());
                        }

                    }
                    callExitSlider.close();
                    callExitSlider.filtersWindow();

                }
            }
        });
    }

    public void setCallExit(CallExitSlider callExit) {
        callExitSlider = callExit;
    }
}