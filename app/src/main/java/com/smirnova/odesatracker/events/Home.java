package com.smirnova.odesatracker.events;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smirnova.odesatracker.DataBase;
import com.smirnova.odesatracker.R;

public class Home extends Fragment implements ICallBack, BackFromReadMore {
    private RecyclerView items;

    public static Context context;
    private AdapterRecycleView adapterRecycleView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View inflate = inflater.inflate(R.layout.fragment_home, container, false);
        items = inflate.findViewById(R.id.list_items);

        context = getActivity();
        DataBase.generalView = DataBase.multiView;

        DataBase.createOrReturn().setCallBack(this);
        setList();
        return inflate;
    }

    @Override
    public void setList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        items.setLayoutManager(layoutManager);

        items.setHasFixedSize(false);

        adapterRecycleView = new AdapterRecycleView(DataBase.generalView, this);
        items.setAdapter(adapterRecycleView);
    }

    @Override
    public void goBack() {
        MainActivity.fragmentTransaction = MainActivity.supportFragmentManager.beginTransaction();
        MainActivity.fragmentTransaction.replace(R.id.relativeLayout, new Home()).commit();
    }
}
