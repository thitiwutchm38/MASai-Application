package com.example.bookthiti.masai2.MobileApplicationScanning;

import com.google.gson.annotations.SerializedName;

public class TargetApplicationInfo {
    @SerializedName("docId")
    private String appId;

    @SerializedName("title")
    private String appName;

    @SerializedName("versionCode")
    private int appVersionCode;

    @SerializedName("versionString")
    private String appVersionString;

    public TargetApplicationInfo(String appId, String appName, int appVersionCode, String appVersionString) {
        this.appId = appId;
        this.appName = appName;
        this.appVersionCode = appVersionCode;
        this.appVersionString = appVersionString;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getAppVersionCode() {
        return appVersionCode;
    }

    public void setAppVersionCode(int appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    public String getAppVersionString() {
        return appVersionString;
    }

    public void setAppVersionString(String appVersionString) {
        this.appVersionString = appVersionString;
    }
}
