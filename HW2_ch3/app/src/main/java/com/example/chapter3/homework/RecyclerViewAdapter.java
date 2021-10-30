package com.example.chapter3.homework;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.viewHolder> {

    private List<String> mItems;

    public static class viewHolder extends RecyclerView.ViewHolder {
        public final TextView name;

        public viewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
        }

        public TextView getTextView() {
            return name;
        }
    }

    public RecyclerViewAdapter(List<String> dataSet) {
        mItems = dataSet;
    }


    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.p1_item_layout, viewGroup, false);
        return new viewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {
        viewHolder.getTextView().setText(mItems.get(i));
    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
