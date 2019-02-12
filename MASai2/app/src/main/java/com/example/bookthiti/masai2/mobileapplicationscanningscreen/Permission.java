package com.example.bookthiti.masai2.mobileapplicationscanningscreen;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;

public class Permission implements Parcelable {

    private String title;
    private String description;
    private String level;
    private String owaspId;
    private String info;

    public Permission() {
    }

    public Permission(String title, String description, String level, String owaspId, String info) {
        this.title = title;
        this.description = description;
        this.level = level;
        this.owaspId = owaspId;
        this.info = info;
    }

    protected Permission(Parcel in) {
        title = in.readString();
        description = in.readString();
        level = in.readString();
        owaspId = in.readString();
        info = in.readString();
    }

    public static final Creator<Permission> CREATOR = new Creator<Permission>() {
        @Override
        public Permission createFromParcel(Parcel in) {
            return new Permission(in);
        }

        @Override
        public Permission[] newArray(int size) {
            return new Permission[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getOwaspId() {
        return owaspId;
    }

    public void setOwaspId(String owaspId) {
        this.owaspId = owaspId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(level);
        parcel.writeString(owaspId);
        parcel.writeString(info);
    }

    public static class PermissionDeserializer implements JsonDeserializer<Permission> {

        @Override
        public Permission deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String title = jsonObject.get("title") != null && !jsonObject.get("title").isJsonNull() ? jsonObject.get("title").getAsString() : null;
            String status = jsonObject.get("status") != null && !jsonObject.get("status").isJsonNull() ? jsonObject.get("status").getAsString() : null;
            String description = jsonObject.get("description") != null && !jsonObject.get("description").isJsonNull() ? jsonObject.get("description").getAsString() : null;
            String info = jsonObject.get("info") != null && !jsonObject.get("info").isJsonNull() ? jsonObject.get("info").getAsString() : null;
            String owaspId = jsonObject.get("owaspId") != null && !jsonObject.get("owaspId").isJsonNull() ? jsonObject.get("owaspId").getAsString() : null;
            return new Permission(title, description, status, owaspId, info);
        }
    }
}
