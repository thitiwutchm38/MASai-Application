package com.example.bookthiti.masai2.devicediscoveryscreen;

public class DeviceModel {

    private String mIP_address;
    private String mMac_address;
    private String mDevice_types;
    private int mIcon;


//    public DeviceModel( String mIP_address, String mMac_address, String mDevice_types) {
////        this.mIP_address = mIP_address;
////        this.mMac_address = mMac_address;
////        this.mDevice_types = mDevice_types;
////    }



    public void setmIP_address(String mIP_address) {
        this.mIP_address = mIP_address;
    }

    public String getmIP_address() {
        return mIP_address;
    }



    public void setmMac_address(String mMac_address) {
        this.mMac_address = mMac_address;
    }

    public String getmMac_address() {
        return mMac_address;
    }


    public void setmDevice_types(String mDevice_types) {
        this.mDevice_types = mDevice_types;
    }

    public String getmDevice_types() {
        return mDevice_types;
    }


    public int getOfferIcon() {
        return mIcon;
    }

    public void setOfferIcon(int mIcon) {
        this.mIcon = mIcon;
    }





}
