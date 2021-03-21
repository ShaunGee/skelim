package com.example.skelim;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class VideoDetails extends Thread{
    String descriptionUrl, imageUrl, description;
    Bitmap displayImg;

    public VideoDetails( String imageUrl){
        this.descriptionUrl = descriptionUrl;
        this.imageUrl = imageUrl;
    }
    public String getDescription(){

        return description;
    }

    public Bitmap getDisplayImg(){

        return displayImg;
    }

    private void proccesVideoDetails(){
        System.out.println("video processing called");
        System.out.println(displayImg);
        try {
            URL url  = new URL(imageUrl);
            URLConnection conn = url.openConnection();
            conn.connect();
            InputStream inputStream = conn.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            displayImg = BitmapFactory.decodeStream(bufferedInputStream);
            bufferedInputStream.close();
            inputStream.close();
            //displayImg = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            //displayImage.setImageBitmap(videoDisplayImg);
            System.out.println(displayImg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        proccesVideoDetails();
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.run();


    }
}
