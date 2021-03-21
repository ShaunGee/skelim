package com.example.skelim;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.net.URL;

public class DownloadImageTask {
    String url;
    ImageView imageView;


    public DownloadImageTask(String url, ImageView imageView) {
        this.url = url;
        this.imageView = imageView;

    }

    public void setImg(){
        Picasso.get().load(url).into(imageView);
    }
}
