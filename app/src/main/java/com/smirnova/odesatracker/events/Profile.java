package com.smirnova.odesatracker.events;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.auth.User;
import com.smirnova.odesatracker.DataBase;
import com.smirnova.odesatracker.R;
import com.smirnova.odesatracker.start.SignUp;

public class Profile extends Fragment {
    private ChipGroup chipGroupMy;
    private ChipGroup chipGroupAdd;

    private TextView add_interest;

    private EditText name;
    private EditText mail;

    private Button save;

    private ImageView edit_profile;
    private ImageView imageName;
    private ImageView imageMail;
    private ImageView logOut;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View inflate = inflater.inflate(R.layout.fragment_profile, container, false);

        chipGroupAdd = inflate.findViewById(R.id.chipsGroupAdd);
        chipGroupMy = inflate.findViewById(R.id.chipsGroupMy);
        add_interest = inflate.findViewById(R.id.textAddInterests);

        mail = inflate.findViewById(R.id.email);
        name = inflate.findViewById(R.id.name);

        save = inflate.findViewById(R.id.save_changes);

        edit_profile = inflate.findViewById(R.id.edit_profile);
        imageMail = inflate.findViewById(R.id.imageMail);
        imageName = inflate.findViewById(R.id.imageName);
        logOut = inflate.findViewById(R.id.logout);


        name.setText(UserInfo.getName());
        mail.setText(UserInfo.getMail());

        mail.setEnabled(false);
        name.setEnabled(false);

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageMail.setVisibility(View.VISIBLE);
                imageName.setVisibility(View.VISIBLE);
                save.setVisibility(View.VISIBLE);

                mail.setEnabled(true);
                name.setEnabled(true);
                edit_profile.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit_profile_black));
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageMail.setVisibility(View.GONE);
                imageName.setVisibility(View.GONE);
                mail.setEnabled(false);
                name.setEnabled(false);
                save.setVisibility(View.GONE);

                UserInfo.setName(name.getText().toString());
                UserInfo.setMail(mail.getText().toString());
                edit_profile.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit_profile_orange));
            }
        });

        add_interest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = add_interest.getText().toString();
                if (temp.equals("Add interests -")) {
                    chipGroupAdd.setVisibility(View.GONE);
                    add_interest.setText("Add interests +");
                } else {
                    chipGroupAdd.setVisibility(View.VISIBLE);
                    add_interest.setText("Add interests -");
                }
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), SignUp.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        for (String s : DataBase.interestList) {
            TextView child = new TextView(getActivity());

            child.setText(s);
            child.setBackground(getResources().getDrawable(R.drawable.interest_button));

            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (child.getParent() == chipGroupAdd) {
                        chipGroupAdd.removeView(child);
                        chipGroupMy.addView(child);
                    } else {
                        chipGroupMy.removeView(child);
                        chipGroupAdd.addView(child);
                    }


                }
            });
            chipGroupAdd.addView(child);
        }

        return inflate;
    }

}
