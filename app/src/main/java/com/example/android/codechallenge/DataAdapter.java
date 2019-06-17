package com.example.android.codechallenge;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder>{
    private static final String TAG = DataAdapter.class.getSimpleName();

    private static final ArrayList<String> mDummyData = new ArrayList<>();
    private int mNumberItems;

    public DataAdapter(){
        mDummyData.add("message1");
        mDummyData.add("message2");
        mDummyData.add("message3");
        mDummyData.add("message4");
        mDummyData.add("message5");
        mDummyData.add("message6");
        mDummyData.add("message7");
        mDummyData.add("message8");
        mDummyData.add("message9");
        mDummyData.add("message10");
        mDummyData.add("message11");
        mDummyData.add("message12");
        mDummyData.add("message13");
        mDummyData.add("message14");
        mDummyData.add("message15");
        mDummyData.add("message16");
        mNumberItems = mDummyData.size();
    }

    // This gets called when each new ViewHolder is created.
    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        DataViewHolder viewHolder = new DataViewHolder(view);

        // A new DataViewHolder that holds the View for each list item
        return viewHolder;
    }

    // OnBindViewHolder is called by the RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(DataViewHolder dataViewHolder, int position) {
        dataViewHolder.itemTextView.setText(mDummyData.get(position));
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }


    class DataViewHolder extends RecyclerView.ViewHolder{
        TextView itemTextView;

        public DataViewHolder(View itemView) {
            super(itemView);
            itemTextView = itemView.findViewById(R.id.item_text_view);
        }
    }
}
