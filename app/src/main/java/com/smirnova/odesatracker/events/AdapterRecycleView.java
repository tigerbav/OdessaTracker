package com.smirnova.odesatracker.events;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.smirnova.odesatracker.DataBase;
import com.smirnova.odesatracker.EventInfo;
import com.smirnova.odesatracker.R;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AdapterRecycleView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int TEXT = 0;
    private final int ITEMS = 1;

    private BackFromReadMore backFromReadMore;

    private List<Object> objectListItems;

    public AdapterRecycleView(List<Object> objectListItems, BackFromReadMore backFromReadMore) {
        this.objectListItems = objectListItems;
        this.backFromReadMore = backFromReadMore;
    }

    @Override
    public int getItemViewType(int position) {
        if (objectListItems.get(position) instanceof NameEventScreen) {
            return TEXT;
        } else if (objectListItems.get(position) instanceof EventInfo) {
            return ITEMS;
        }
        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View modelView;
        RecyclerView.ViewHolder viewHolder = null;

        if (viewType == TEXT) {
            modelView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_name, parent, false);
            viewHolder = new SimpleText(modelView);
        } else if (viewType == ITEMS) {
            modelView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
            viewHolder = new Event(modelView);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TEXT) {
            SimpleText simpleText = (SimpleText) holder;
            NameEventScreen nameEventScreen = (NameEventScreen) objectListItems.get(position);
            simpleText.name.setText(nameEventScreen.getEvent());
        } else if (holder.getItemViewType() == ITEMS) {
            Event event = (Event) holder;
            EventInfo eventInfo = (EventInfo) DataBase.generalView.get(position);

            event.eventName.setText(eventInfo.getEvent());
            event.category.setText(eventInfo.getCategory());
            event.rate.setText(eventInfo.getRaiting());
            event.date.setText(eventInfo.getDate());

            if (eventInfo.isLike()) {
                event.saved.setImageDrawable(Home.context.getDrawable(R.drawable.ic_saved));
            } else {
                event.saved.setImageDrawable(Home.context.getDrawable(R.drawable.ic_saved_no));
            }
            if (eventInfo.getDate().equals("")) {
                event.dateImage.setVisibility(View.GONE);
            }

            event.saved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!eventInfo.isLike()) {
                        event.saved.setImageDrawable(Home.context.getDrawable(R.drawable.ic_saved));
                        DataBase.savedView.add(eventInfo);
                    } else {
                        event.saved.setImageDrawable(Home.context.getDrawable(R.drawable.ic_saved_no));
                        DataBase.savedView.remove(eventInfo);
                    }
                    eventInfo.setLike(!eventInfo.isLike());
                }
            });

            event.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MoreInfo moreInfo = new MoreInfo();
                    moreInfo.setBackFromReadMore(backFromReadMore);
                    moreInfo.setEventInfo(eventInfo, eventInfo.getEvent());
                    MainActivity.fragmentTransaction = MainActivity.supportFragmentManager.beginTransaction();
                    MainActivity.fragmentTransaction.replace(R.id.relativeLayout, moreInfo).commit();
                }
            });

            try {
                File file = File.createTempFile("image", "jpg");
                FirebaseStorage
                        .getInstance()
                        .getReferenceFromUrl("gs://odessatracker-298eb.appspot.com")
                        .child(eventInfo.getEvent().replace(":", "") + ".jpg").getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        event.imageEvent.setImageBitmap(bitmap);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public int getItemCount() {
        return objectListItems.size();
    }

    public class SimpleText extends RecyclerView.ViewHolder {
        TextView name;

        public SimpleText(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameEventTotal);
        }
    }

    public class Event extends RecyclerView.ViewHolder {
        TextView more;
        TextView eventName;
        TextView category;
        TextView date;
        ImageView imageEvent;
        ImageView saved;
        ImageView dateImage;
        Button rate;

        public Event(@NonNull View itemView) {
            super(itemView);
            more = itemView.findViewById(R.id.read_more);
            eventName = itemView.findViewById(R.id.nameEvent);
            category = itemView.findViewById(R.id.category);
            date = itemView.findViewById(R.id.dateText);

            imageEvent = itemView.findViewById(R.id.imageEvent);
            dateImage = itemView.findViewById(R.id.date_image);
            saved = itemView.findViewById(R.id.saved_like);

            rate = itemView.findViewById(R.id.rating);

        }
    }
}