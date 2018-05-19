package com.p2app.frontend.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

// Async task is a task that is run on another thread
// Its an Abstract class that forces you to implement: doInBackGround() and onPostExecute
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    // use it by calling this
    // new DownloadImageTask((ImageView) findViewById(R.id.imageView)).execute(URL);

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    // This method is called, in AsyncTask's method execute
    protected Bitmap doInBackground(String... urls) {
        // The image is encoded into a bitmap
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        // The image i set using the bitmap
        bmImage.setImageBitmap(result);
    }
}