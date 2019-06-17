package com.example.android.codechallenge;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{
    private static final String TAG = MessageAdapter.class.getSimpleName();

    private static final ArrayList<String> mDummyData = new ArrayList<>();
    private static List<Message> mMessages = new ArrayList<>();

    public MessageAdapter(){
//        mDummyData.add("message1");
//        mDummyData.add("message2");
//        mDummyData.add("message3");
//        mDummyData.add("message4");
//        mDummyData.add("message5");
//        mDummyData.add("message6");
//        mDummyData.add("message7");
//        mDummyData.add("message8");
//        mDummyData.add("message9");
//        mDummyData.add("message10");
//        mDummyData.add("message11");
//        mDummyData.add("message12");
//        mDummyData.add("message13");
//        mDummyData.add("message14");
//        mDummyData.add("message15");
//        mDummyData.add("message16");
//        mNumberItems = mDummyData.size();
    }

    // This gets called when each new ViewHolder is created.
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        MessageViewHolder viewHolder = new MessageViewHolder(view);

        // A new DataViewHolder that holds the View for each list item
        return viewHolder;
    }

    // OnBindViewHolder is called by the RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(MessageViewHolder messageViewHolder, int position) {
        Message message = mMessages.get(position);

        messageViewHolder.toNameTextView.setText(message.getToName());
        messageViewHolder.fromNameTextView.setText(message.getFromName());
        if(message.getAreFriends()){
            messageViewHolder.areFriendsTextView.setText("Friends");
        }else {
            messageViewHolder.areFriendsTextView.setText("Not Friends");
        }

        Date dateObject = new Date(message.getTime());
        String date = formatDate(dateObject);
        messageViewHolder.dateTextView.setText(date);
        String time = formatTime(dateObject);
        messageViewHolder.timeTextView.setText(time);

    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }


    class MessageViewHolder extends RecyclerView.ViewHolder{
        TextView toNameTextView;
        TextView fromNameTextView;
        TextView timeTextView;
        TextView dateTextView;
        TextView areFriendsTextView;

        public MessageViewHolder(View itemView) {
            super(itemView);
            toNameTextView = itemView.findViewById(R.id.to_name_text_view);
            fromNameTextView = itemView.findViewById(R.id.from_name_text_view);
            timeTextView = itemView.findViewById(R.id.time_text_view);
            dateTextView = itemView.findViewById(R.id.date_text_view);
            areFriendsTextView = itemView.findViewById(R.id.are_friends_text_view);
        }
    }

    //This method is used to set the data we get from the web.

    public void setMessageData(List<Message> data) {
        mMessages = data;
        notifyDataSetChanged();
    }


    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM DD, yyyy");
        String dateToDisplay = dateFormatter.format(dateObject);
        return dateToDisplay;
    }

    private String formatTime(Date dateObject) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm a");
        String timeToDisplay = dateFormatter.format(dateObject);
        return timeToDisplay;
    }
}
