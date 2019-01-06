package com.example.bookthiti.masai2;

import android.os.Parcel;
import android.os.Parcelable;

public class MainModel implements Parcelable {
    private String offerSSID;
    private String offerMode;
    private String offerSignal;

    private String offerFrequency;
    private String offerChannel;
    private String offerCompany;


    private String offerMac_address;

    private String offerSecurity;


    private int offerIcon;

    public MainModel() {

    }

    public MainModel(String offerSSID, String offerMode, String offerSignal, String offerChannel, String offerMac_address, String offerSecurity, int offerIcon) {
        this.offerSSID = offerSSID;
        this.offerMode = offerMode;
        this.offerSignal = offerSignal;
        this.offerChannel = offerChannel;
        this.offerMac_address = offerMac_address;
        this.offerSecurity = offerSecurity;
        this.offerIcon = offerIcon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Storing the Movie data to Parcel object
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(offerSSID);
        dest.writeString(offerMode);
        dest.writeString(offerSignal);

        dest.writeString(offerFrequency);
        dest.writeString(offerChannel);
        dest.writeString(offerCompany);

        dest.writeString(offerSecurity);

        dest.writeString(offerMac_address);

    }


    MainModel(Parcel in){
        this.offerSSID = in.readString();
        this.offerMode = in.readString();
        this.offerSignal = in.readString();
        this.offerFrequency = in.readString();
        this.offerChannel = in.readString();
        this.offerCompany = in.readString();

        this.offerMac_address = in.readString();

        this.offerSecurity = in.readString();
    }




    public static final Parcelable.Creator<MainModel> CREATOR = new Parcelable.Creator<MainModel>() {
        @Override
        public MainModel createFromParcel(Parcel source) {
            return new MainModel(source);
        }

        @Override
        public MainModel[] newArray(int size) {
            return new MainModel[size];
        }
    };



    public String getOfferSSID() {
        return offerSSID;
    }
    public void setOfferSSID(String offerSSID) { this.offerSSID = offerSSID;

    }


    public String getOfferMode() {
        return offerMode;
    }
    public void setOfferMode(String offerMode) {
        this.offerMode = offerMode;
    }


    public String getOfferSignal() {
        return offerSignal;
    }
    public void setOfferSignal(String offerSignal) {
        this.offerSignal = offerSignal;
    }


    public String getOfferFrequency() {
        return offerFrequency;
    }
    public void setOfferFrequency(String offerFrequency) {
        this.offerFrequency = offerFrequency;
    }

    public String getOfferChannel() {
        return offerChannel;
    }
    public void setOfferChannel(String offerChannel) {
        this.offerChannel = offerChannel;
    }


    public String getOfferCompany() {
        return offerCompany;
    }
    public void setOfferCompany(String offerCompany) {
        this.offerCompany = offerCompany;
    }


    public int getOfferIcon() {
        return offerIcon;
    }
    public void setOfferIcon(int offerIcon) {
        this.offerIcon = offerIcon;
    }


    public String getOfferMac_address() {
        return offerMac_address;
    }
    public void setOfferMac_address(String offerMac_address) { this.offerMac_address = offerMac_address;
    }

    public String getOfferSecurity() {
        return offerSecurity;
    }
    public void setOfferSecurity(String offerSecurity) {
        this.offerSecurity = offerSecurity;
    }
}