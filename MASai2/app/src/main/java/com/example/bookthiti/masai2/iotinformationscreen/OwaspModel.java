package com.example.bookthiti.masai2.iotinformationscreen;

import android.os.Parcel;
import android.os.Parcelable;

public class OwaspModel implements Parcelable {
    private String topic;
    private String topicId;
    private String generalDetail;
    private String example;
    private String guideline;
    private int imageId;

    public OwaspModel(String topicId , String topic, String generalDetail, String example, String guideline, int imageId) {
        this.topic = topic;
        this.topicId = topicId;
        this.generalDetail = generalDetail;
        this.example = example;
        this.guideline = guideline;
        this.imageId = imageId;
    }

    public OwaspModel(Parcel in) {
        topic = in.readString();
        topicId = in.readString();
        generalDetail = in.readString();
        example = in.readString();
        guideline = in.readString();
        imageId = in.readInt();
    }

    public static final Creator<OwaspModel> CREATOR = new Creator<OwaspModel>() {
        @Override
        public OwaspModel createFromParcel(Parcel in) {
            return new OwaspModel(in);
        }

        @Override
        public OwaspModel[] newArray(int size) {
            return new OwaspModel[size];
        }
    };

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getGeneralDetail() {
        return generalDetail;
    }

    public void setGeneralDetail(String generalDetail) {
        this.generalDetail = generalDetail;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getGuideline() {
        return guideline;
    }

    public void setGuideline(String guideline) {
        this.guideline = guideline;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.topic);
        parcel.writeString(this.topicId);
        parcel.writeString(this.generalDetail);
        parcel.writeString(this.example);
        parcel.writeString(this.guideline);
        parcel.writeInt(this.imageId);
    }
}
