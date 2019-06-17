package com.example.android.codechallenge;

public class Message {

    private String mToName;
    private String mFromName;
    private long mTimeInMilliseconds;
    private boolean mAreFriends;

    public Message(String toName, String fromName, long timeInMilliseconds, boolean areFriends){
        mToName = toName;
        mFromName = fromName;
        mTimeInMilliseconds = timeInMilliseconds;
        mAreFriends = areFriends;
    }

    public String getToName(){
        return mToName;
    }

    public String getFromName(){
        return mFromName;
    }

    public long getTime(){
        return mTimeInMilliseconds;
    }

    public boolean getAreFriends(){
        return mAreFriends;
    }

    public String toString(){
        return "To: " + mToName + "  From: " + mFromName + "  Time: " + Long.toString(mTimeInMilliseconds)
                + "  areFriends: " + mAreFriends;
    }
}
