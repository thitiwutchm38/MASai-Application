package com.example.bookthiti.masai2.internet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

import static com.example.bookthiti.masai2.utils.LogConstants.TAG_ERROR;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    Bitmap bitmap;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    public DownloadImageTask(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public DownloadImageTask(ImageView bmImage, Bitmap bitmap) {
        this.bmImage = bmImage;
        this.bitmap = bitmap;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e(TAG_ERROR, e.getMessage());
            e.printStackTrace();
        }
        this.bitmap = mIcon11;
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        if (bmImage != null) bmImage.setImageBitmap(result);
    }

}
