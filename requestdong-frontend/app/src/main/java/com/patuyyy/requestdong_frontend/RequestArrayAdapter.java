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
import com.patuyyy.requestdong_frontend.model.Request;

import java.util.List;

public class RequestArrayAdapter extends ArrayAdapter<Request> {
    // invoke the suitable constructor of the ArrayAdapter class
    public RequestArrayAdapter(@NonNull Context context, List<Request> list) {
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View currentItemView = convertView;

        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.request_view, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        Request currentRequest = getItem(position);

        // then according to the position of the view assign the desired TextView 1 for the same
        TextView requestText = currentItemView.findViewById(R.id.requested_thing);
        TextView amount = currentItemView.findViewById(R.id.amount);
        TextView deadline = currentItemView.findViewById(R.id.deadline);
        requestText.setText(currentRequest.getRequested_thing().toString());
        amount.setText("Amount :" + currentRequest.getAmount());
        deadline.setText(currentRequest.getDeadline().toString());

        return currentItemView;
    }
}
