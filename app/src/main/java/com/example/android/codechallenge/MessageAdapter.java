package com.example.android.codechallenge;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.codechallenge.data.MessageContract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private static final String TAG = MessageAdapter.class.getSimpleName();

    private Cursor mCursor;
    private Context mContext;

    public MessageAdapter(Context context) {
        mContext = context;
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
        // Indices for the _id, description, and priority columns
        int toNameIndex = mCursor.getColumnIndex(MessageContract.MessageEntry.COLUMN_TO_NAME);
        int fromNameIndex = mCursor.getColumnIndex(MessageContract.MessageEntry.COLUMN_From_NAME);
        int timeIndex = mCursor.getColumnIndex(MessageContract.MessageEntry.COLUMN_TIME);
        int areFriendsIndex = mCursor.getColumnIndex(MessageContract.MessageEntry.COLUMN_ARE_FRIENDS);

        mCursor.moveToPosition(position); // get to the right location in the cursor

        // Determine the values of the wanted data
        String toName = mCursor.getString(toNameIndex);
        String fromName = mCursor.getString(fromNameIndex);
        Long time = mCursor.getLong(timeIndex);
        int areFriends = mCursor.getInt(areFriendsIndex);


        messageViewHolder.toNameTextView.setText(toName);
        messageViewHolder.fromNameTextView.setText(fromName);
        if (areFriends > 0) {
            messageViewHolder.areFriendsTextView.setText("Friends");
        } else {
            messageViewHolder.areFriendsTextView.setText("Not Friends");
        }

        Date dateObject = new Date(time);

        SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm:ss MM/dd/yyyy");
        String timeString = timeFormatter.format(dateObject);
        messageViewHolder.timeTextView.setText(timeString);

        messageViewHolder.listIndexTextView.setText(String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    /**
     * When data changes and a re-query occurs, this function swaps the old Cursor
     * with a newly updated Cursor (Cursor c) that is passed in.
     */
    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView toNameTextView;
        TextView fromNameTextView;
        TextView timeTextView;
        TextView areFriendsTextView;
        TextView listIndexTextView;

        public MessageViewHolder(View itemView) {
            super(itemView);
            toNameTextView = itemView.findViewById(R.id.to_name_text_view);
            fromNameTextView = itemView.findViewById(R.id.from_name_text_view);
            timeTextView = itemView.findViewById(R.id.time_text_view);
            areFriendsTextView = itemView.findViewById(R.id.are_friends_text_view);
            listIndexTextView = itemView.findViewById(R.id.list_index_text_view);
        }
    }

    //This method is used to set the data we get from the web.
//
//    public void setMessageData(List<Message> data) {
//        mMessages = data;
//        notifyDataSetChanged();
//    }
//
//
//    //This method is used to add more data we get from the web.
//    public void addMoreMessageData(List<Message> data) {
//        mMessages.addAll(data);
//        notifyDataSetChanged();
//    }

}
