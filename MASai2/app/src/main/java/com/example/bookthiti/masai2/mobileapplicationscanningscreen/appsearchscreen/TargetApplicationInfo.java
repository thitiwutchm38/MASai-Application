package com.example.bookthiti.masai2.mobileapplicationscanningscreen.appsearchscreen;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class TargetApplicationInfo implements Parcelable {

    private String appId;

    private String appName;

    private int appVersionCode;

    private String appVersionString;

    private String appCategory;

    private AppIcon appIcon;

    private String appAuthor;

    public TargetApplicationInfo() {

    }

    public TargetApplicationInfo(String appId, String appName, int appVersionCode, String appVersionString, String appCategory, AppIcon appIcon, String appAuthor) {
        this.appId = appId;
        this.appName = appName;
        this.appVersionCode = appVersionCode;
        this.appVersionString = appVersionString;
        this.appCategory = appCategory;
        this.appIcon = appIcon;
        this.appAuthor = appAuthor;
    }

    protected TargetApplicationInfo(Parcel in) {
        appId = in.readString();
        appName = in.readString();
        appVersionCode = in.readInt();
        appVersionString = in.readString();
        appCategory = in.readString();
        appIcon = in.readParcelable(AppIcon.class.getClassLoader());
        appAuthor = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(appId);
        dest.writeString(appName);
        dest.writeInt(appVersionCode);
        dest.writeString(appVersionString);
        dest.writeString(appCategory);
        dest.writeParcelable(appIcon, flags);
        dest.writeString(appAuthor);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TargetApplicationInfo> CREATOR = new Creator<TargetApplicationInfo>() {
        @Override
        public TargetApplicationInfo createFromParcel(Parcel in) {
            return new TargetApplicationInfo(in);
        }

        @Override
        public TargetApplicationInfo[] newArray(int size) {
            return new TargetApplicationInfo[size];
        }
    };

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

    public AppIcon getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(AppIcon appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppCategory() {
        return appCategory;
    }

    public void setAppCategory(String appCategory) {
        this.appCategory = appCategory;
    }

    public String getAppAuthor() {
        return appAuthor;
    }

    public void setAppAuthor(String appAuthor) {
        this.appAuthor = appAuthor;
    }

    public static class TargetApplicationInfoDeserializer implements JsonDeserializer<TargetApplicationInfo> {
        @Override
        public TargetApplicationInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String appId = jsonObject.get("docId").getAsString();
            String appName = jsonObject.get("title").getAsString();
            int appVersionCode = jsonObject.get("versionCode").getAsInt();
            String appVersionString = jsonObject.get("versionString").getAsString();
            String appCategory = jsonObject.get("appCategory").getAsString();
            AppIcon appIcon = context.deserialize(jsonObject.get("icon"), AppIcon.class);
            String appAuthor = jsonObject.get("author").getAsString();
            return new TargetApplicationInfo(appId, appName, appVersionCode, appVersionString, appCategory, appIcon, appAuthor);
        }
    }
}


