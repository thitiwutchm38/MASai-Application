package com.example.bookthiti.masai2;

public class OwaspData {
    private String mOwasptopic;
    private String mOwaspNum;
    private String mOwaspDetail;

    public OwaspData( String mOwaspNum , String mOwasptopic, String mOwaspDetail) {
        this.mOwasptopic = mOwasptopic;
        this.mOwaspNum = mOwaspNum;
        this.mOwaspDetail = mOwaspDetail;
    }

    public String getOwasptopic() { return mOwasptopic; }
    public String getmOwaspNum() {
        return mOwaspNum;
    }
    public String getmOwaspDetail() {
        return mOwaspDetail;
    }

}
