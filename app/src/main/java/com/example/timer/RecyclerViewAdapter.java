package com.example.timer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ReclyerViewHolder> {
    private ArrayList<ListItem> mItemList;
    public static class ReclyerViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;

        public ReclyerViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.listText);
        }
    }

    public RecyclerViewAdapter(ArrayList<ListItem> itemList){
        mItemList = itemList;
    }
    @NonNull
    @Override
    public ReclyerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item, viewGroup, false);
        ReclyerViewHolder rvh = new ReclyerViewHolder(v);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ReclyerViewHolder RecyclewVieHolder, int i) {
        ListItem currentItem = mItemList.get(i);
        RecyclewVieHolder.mTextView.setText(currentItem.getName());
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}
