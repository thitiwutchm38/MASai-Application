package com.example.bookthiti.masai2;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Comparator;


public class MainModel implements Parcelable  {
    private String offerSSID;
    private String offerMode;
    private String offerSignal;

    private String offerFrequency;
    private String offerChannel;
    private String offerCompany;


    private String offerMac_address;

    private String offerSecurity;


    private int offerIcon;


    private int modelsignal;
    private MainModel comparestu;


    public MainModel() {

//        this.getOfferChannel();
//        this.getOfferSecurity();
//        this.getOfferMac_address();
//        this.getOfferFrequency();
//        this.getOfferMode();
//        this.getOfferSSID();
//        this.getOfferCompany();
//        this.getOfferFrequency();
//        this.getOfferIcon();

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



    public static Comparator<MainModel> modelSigno = new Comparator<MainModel>() {

        public int compare(MainModel s1, MainModel s2) {

            int rollno1 = Integer.parseInt(s1.getOfferSignal());
            int rollno2 = Integer.parseInt(s2.getOfferSignal());

            /*For ascending order*/
            return rollno2-rollno1;

            /*For descending order*/
            //rollno1-rollno2;
        }};
    public static Comparator<MainModel> modelSig_des = new Comparator<MainModel>() {

        public int compare(MainModel s1, MainModel s2) {

            int rollno1 = Integer.parseInt(s1.getOfferSignal());
            int rollno2 = Integer.parseInt(s2.getOfferSignal());

            /*For ascending order*/
            //return rollno2-rollno1;

            /*For descending order*/
            return rollno1-rollno2;
        }};


    public static Comparator<MainModel> modelssid_asc = new Comparator<MainModel>() {

        public int compare(MainModel s1, MainModel s2) {


            String c1 = s1.getOfferSSID();
            String c2 = s2.getOfferSSID();



            if (c1.toLowerCase().equals(s1) && c2.toUpperCase().equals(s2)) {
                return 1;
            }

            if (c1.toUpperCase().equals(s1) && c2.toLowerCase().equals(s2)) {
                return -1;
            }

            return c1.compareTo(c2);

        }};

    public static Comparator<MainModel> modelssid_des = new Comparator<MainModel>() {

        public int compare(MainModel s1, MainModel s2) {


            String c1 = s1.getOfferSSID();
            String c2 = s2.getOfferSSID();



            if (c1.toLowerCase().equals(s1) && c2.toUpperCase().equals(s2)) {
                return 1;
            }

            if (c1.toUpperCase().equals(s1) && c2.toLowerCase().equals(s2)) {
                return -1;
            }

            return c2.compareTo(c1);

        }};



    public static Comparator<MainModel> modelmode_asc = new Comparator<MainModel>() {

        public int compare(MainModel s1, MainModel s2) {


            String c1 = s1.getOfferMode();
            String c2 = s2.getOfferMode();



            if (c1.toLowerCase().equals(s1) && c2.toUpperCase().equals(s2)) {
                return 1;
            }

            if (c1.toUpperCase().equals(s1) && c2.toLowerCase().equals(s2)) {
                return -1;
            }

            return c1.compareTo(c2);

        }};

    public static Comparator<MainModel> modelmode_des = new Comparator<MainModel>() {

        public int compare(MainModel s1, MainModel s2) {


            String c1 = s1.getOfferMode();
            String c2 = s2.getOfferMode();



            if (c1.toLowerCase().equals(s1) && c2.toUpperCase().equals(s2)) {
                return 1;
            }

            if (c1.toUpperCase().equals(s1) && c2.toLowerCase().equals(s2)) {
                return -1;
            }

            return c2.compareTo(c1);

        }};





}