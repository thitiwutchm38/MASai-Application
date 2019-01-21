package com.example.bookthiti.masai2.networksearchingscreen;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

import com.example.bookthiti.masai2.R;
import com.google.gson.annotations.SerializedName;

public class RouterModel implements Parcelable {
    @SerializedName("SSID")
    private String mSsid;

    @SerializedName("MODE")
    private String mMode;

    @SerializedName("SIGNAL")
    private float mSignal;

    @SerializedName("CHAN")
    private int mChannel;

    @SerializedName("BSSID")
    private String mBssid;

    @SerializedName("SECURITY")
    private String mSecurity;

    private int mIconSignalId;
    private boolean isConnecting = false;

    //    private String mFrequency;

    public RouterModel() {
    }

    public RouterModel(String ssid, String mode, float signal, int channel, String bssid, String security) {
        this.mSsid = ssid;
        this.mMode = mode;
        this.mSignal = signal;
        this.mChannel = channel;
        this.mBssid = bssid;
        this.mSecurity = security;
        if ((this.mSignal <= 100) && (this.mSignal >= 75))
            this.mIconSignalId = R.drawable.ic_signal_wifi_4_bar_green_a700_36dp;
        else if ((this.mSignal < 75) && (this.mSignal >= 50))
            this.mIconSignalId = R.drawable.ic_signal_wifi_3_bar_yellow_a200_36dp;
        else if ((this.mSignal < 50) && (this.mSignal >= 25))
            this.mIconSignalId = R.drawable.ic_signal_wifi_2_bar_orange_a700_36dp;
        else if (this.mSignal < 25)
            this.mIconSignalId = R.drawable.ic_signal_wifi_1_bar_red_400_36dp;
    }

    public RouterModel(Parcel in) {
        this.mSsid = in.readString();
        this.mMode = in.readString();
        this.mSignal = in.readFloat();
//        this.mFrequency = in.readString();
        this.mChannel = in.readInt();
//        this.mCompany = in.readString();
        this.mBssid = in.readString();
        this.mSecurity = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Storing the Movie data to Parcel object
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mSsid);
        dest.writeString(mMode);
        dest.writeFloat(mSignal);
//        dest.writeString(mFrequency);
        dest.writeInt(mChannel);
//        dest.writeString(mCompany);
        dest.writeString(mBssid);
        dest.writeString(mSecurity);

    }


    public static final Parcelable.Creator<RouterModel> CREATOR = new Parcelable.Creator<RouterModel>() {
        @Override
        public RouterModel createFromParcel(Parcel source) {
            return new RouterModel(source);
        }

        @Override
        public RouterModel[] newArray(int size) {
            return new RouterModel[size];
        }
    };


    public String getSsid() {
        return mSsid;
    }

    public void setSsid(String ssid) {
        this.mSsid = ssid;

    }


    public String getMode() {
        return mMode;
    }

    public void setMode(String mode) {
        this.mMode = mode;
    }

    public float getSignal() {
        return mSignal;
    }

    public void setSignal(float signal) {
        this.mSignal = signal;
    }

    public int getChannel() {
        return mChannel;
    }

    public void setChannel(int channel) {
        this.mChannel = channel;
    }

    public int getIconSignalId() {
        return mIconSignalId;
    }

    public void setIconSignalId(int iconSignalId) {
        this.mIconSignalId = iconSignalId;
    }

    public String getBssid() {
        return mBssid;
    }

    public void setBssid(String bssid) {
        this.mBssid = bssid;
    }

    public String getSecurity() {
        return mSecurity;
    }

    public void setSecurity(String security) {
        this.mSecurity = security;
    }

    public boolean isConnecting() {
        return isConnecting;
    }

    public void setConnecting(boolean connecting) {
        isConnecting = connecting;
    }
}
