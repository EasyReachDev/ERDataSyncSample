package com.er.sampleapp.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.er.sampleapp.R;

import java.util.List;

public class UpdateStatusAdapter extends RecyclerView.Adapter<UpdateStatusAdapter.MyViewHolder> {

    private List<String> mData;

    // Constructor
    public UpdateStatusAdapter(List<String> data) {
        this.mData = data;
    }

    // 1. Inflates the row layout from XML when needed
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);
        return new MyViewHolder(view);
    }

    // 2. Binds data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String item = mData.get(position);
        holder.textView.setText(item);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    // 3. The ViewHolder holds the references to the views for one row
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textViewName);
        }
    }
}