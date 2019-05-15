package com.example.bookthiti.masai2.mobileapplicationscanningscreen.appsearchscreen;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.InputStream;
import java.lang.reflect.Type;

import static com.example.bookthiti.masai2.utils.LogConstants.TAG_ERROR;

public class AppIcon implements Parcelable {
    private int height;
    private int width;
    private String url;
    private Bitmap bitmap;

    protected AppIcon(Parcel in) {
        height = in.readInt();
        width = in.readInt();
        url = in.readString();
//        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
//        new DownloadImageTask(bitmap).execute(url);
    }

    public static final Creator<AppIcon> CREATOR = new Creator<AppIcon>() {
        @Override
        public AppIcon createFromParcel(Parcel in) {
            return new AppIcon(in);
        }

        @Override
        public AppIcon[] newArray(int size) {
            return new AppIcon[size];
        }
    };

    public static AppIcon getAppIconInstance(int height, int width, String url) {
        return new AppIcon(height, width, url);
    }

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

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setBitmap(String url) {
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e(TAG_ERROR, e.getMessage());
            e.printStackTrace();
        }
        this.bitmap = mIcon11;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(height);
        parcel.writeInt(width);
        parcel.writeString(url);
//        parcel.writeParcelable(bitmap, i);
    }

    public static class AppIconDeserializer implements JsonDeserializer<AppIcon> {
        @Override
        public AppIcon deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String url = jsonObject.get("url").getAsString();
            int width = jsonObject.get("width").getAsInt();
            int height = jsonObject.get("height").getAsInt();
            AppIcon appIcon = getAppIconInstance(height, width, url);
            appIcon.setBitmap(url);
            return appIcon;
        }
    }
}