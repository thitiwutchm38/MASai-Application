package com.example.bookthiti.masai2;

public class NewsData {
    private String mSender;
    private String mTitle;
    private String mDetails;
    private String mTime;

    public NewsData(String mSender, String mTitle, String mDetails, String mTime) {
        this.mSender = mSender;
        this.mTitle = mTitle;
        this.mDetails = mDetails;
        this.mTime = mTime;
    }

    public String getmSender() {
        return mSender;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmDetails() {
        return mDetails;
    }

    public String getmTime() {
        return mTime;
    }
}
