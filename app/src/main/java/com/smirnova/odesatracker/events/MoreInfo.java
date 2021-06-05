package com.smirnova.odesatracker.events;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.smirnova.odesatracker.EventInfo;
import com.smirnova.odesatracker.R;

import java.io.File;
import java.io.IOException;

public class MoreInfo extends Fragment {
    private Button btnRaiting;
    private Button btnTickets;

    private ImageView btnBack;
    private ImageView like;
    private ImageView imageEvent;
    private ImageView calendar;

    private TextView eventName;
    private TextView category;
    private TextView date;
    private TextView time;
    private TextView cost;
    private TextView about;
    private TextView loc;

    private EventInfo eventInfo;
    private String name;


    private BackFromReadMore backFromReadMore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View inflate = inflater.inflate(R.layout.fragment_event_info, container, false);
        btnRaiting = inflate.findViewById(R.id.btnRaiting);
        btnTickets = inflate.findViewById(R.id.buy_tickets);
        btnBack = inflate.findViewById(R.id.btnBack);
        like = inflate.findViewById(R.id.like_or_not);
        imageEvent = inflate.findViewById(R.id.imageEvent2);
        calendar = inflate.findViewById(R.id.calendar_image);
        eventName = inflate.findViewById(R.id.event_name);
        loc = inflate.findViewById(R.id.loc_item);
        category = inflate.findViewById(R.id.category_item);
        date = inflate.findViewById(R.id.date_item);
        time = inflate.findViewById(R.id.time_item);
        cost = inflate.findViewById(R.id.cost_item);
        about = inflate.findViewById(R.id.about_text);


        btnRaiting.setText(eventInfo.getRaiting());
        if (eventInfo.isLike()) {
            like.setImageDrawable(Home.context.getDrawable(R.drawable.ic_saved));
        } else {
            like.setImageDrawable(Home.context.getDrawable(R.drawable.ic_saved_no));
        }

        if(eventInfo.getDate().equals("")) {
            calendar.setVisibility(View.INVISIBLE);
        }

        try {
            File file = File.createTempFile("image", "jpg");
            FirebaseStorage
                    .getInstance()
                    .getReferenceFromUrl("gs://odessatracker-298eb.appspot.com")
                    .child(eventInfo.getEvent().replace(":", "") + ".jpg").getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    imageEvent.setImageBitmap(bitmap);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backFromReadMore.goBack();
            }
        });

        eventName.setText(name);
        category.setText(eventInfo.getCategory());
        date.setText(eventInfo.getDate());
        time.setText(eventInfo.getTime());
        cost.setText(eventInfo.getCost());
        about.setText(eventInfo.getDescription());
        loc.setText(eventInfo.getLocation());

        btnTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Site.class);
                intent.putExtra("Url", eventInfo.getLink());
                startActivity(intent);
            }
        });

        return inflate;
    }

    public void setEventInfo(EventInfo eventInfo, String name) {
        this.eventInfo = eventInfo;
        this.name = name;
    }

    public void setBackFromReadMore(BackFromReadMore backFromReadMore) {
        this.backFromReadMore = backFromReadMore;
    }
}
