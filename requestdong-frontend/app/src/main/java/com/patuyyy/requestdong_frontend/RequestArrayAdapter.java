package com.patuyyy.requestdong_frontend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.patuyyy.requestdong_frontend.model.Event;

import java.util.List;

public class RequestArrayAdapter extends ArrayAdapter<Event> {
    // invoke the suitable constructor of the ArrayAdapter class
    public RequestArrayAdapter(@NonNull Context context, List<Event> list) {
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View currentItemView = convertView;

        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.event_view, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        Event currentEvent = getItem(position);

        // then according to the position of the view assign the desired TextView 1 for the same
        TextView eventText = currentItemView.findViewById(R.id.eventname);
        TextView eventTimeText = currentItemView.findViewById(R.id.eventdate);
        eventText.setText(currentEvent.getName());
        eventTimeText.setText(currentEvent.getTime().toString());

        return currentItemView;
    }
}
