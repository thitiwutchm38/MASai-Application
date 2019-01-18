package com.example.bookthiti.masai2.MobileApplicationScanning;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.lang.reflect.Type;

public class TargetApplicationInfo {
    @SerializedName("docId")
    private String appId;

    @SerializedName("title")
    private String appName;

    @SerializedName("versionCode")
    private int appVersionCode;

    @SerializedName("versionString")
    private String appVersionString;

//    @SerializedName("icon")
//    private AppIcon appIcon;

    public TargetApplicationInfo(String appId, String appName, int appVersionCode, String appVersionString){//, AppIcon appIcon) {
        this.appId = appId;
        this.appName = appName;
        this.appVersionCode = appVersionCode;
        this.appVersionString = appVersionString;
//        this.appIcon = appIcon;
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

//    public AppIcon getAppIcon() {
//        return appIcon;
//    }
//
//    public void setAppIcon(AppIcon appIcon) {
//        this.appIcon = appIcon;
//    }
}

class AppIcon {
    private int height;
    private int width;
    private String url;

    public AppIcon(int height, int width, String url) {
        this.height = height;
        this.width = width;
        this.url = url;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

class MyDeserializer<A> implements JsonDeserializer<AppIcon> {
    @Override
    public AppIcon deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonElement appIcon = json.getAsJsonObject().get("url");
        return new Gson().fromJson(appIcon, AppIcon.class);
    }
}
