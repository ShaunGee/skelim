package com.example.skelim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class videoDownload extends AppCompatActivity {
    private static final int PERMISSION_STORAGE_CODE = 1000;
    String url;
    RequestQueue mQueue;
    String r;
    TextView displayText;
    ImageView displayImage;

    String videoTitle,videoDownloadUrl, videoDescription, imageUrl;
    Bitmap videoDisplayImg;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_download);

        url = getIntent().getStringExtra("url");
        url = urlFilter(url);

        System.out.println("url:   " + url.toString());

        mQueue = Volley.newRequestQueue(this);

        displayText = findViewById(R.id.displayurl);
        displayImage = findViewById(R.id.videoImg);



        jsonParse();

    }

    private String urlFilter(String url){

        if (url.contains("instagram")){
            String[] u = url.split("\\?");
            r = u[0] + "?__a=1";


        }

        return r;
    }

    private void jsonParse(){
        //TODO: Create a video title and link to text view and downloader and delet the test one
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //System.out.println(response);
                try {
                    videoDownloadUrl = response.getJSONObject("graphql").getJSONObject("shortcode_media").getString("video_url");
                    imageUrl = response.getJSONObject("graphql").getJSONObject("shortcode_media").getString("display_url");
                    //videoTitle = response.getJSONObject("graphql").getJSONObject("shortcode_media").getString("id");
                    videoDescription = response.getJSONObject("graphql").getJSONObject("shortcode_media").getJSONObject("edge_media_to_caption").getJSONArray("edges").getJSONObject(0).getJSONObject("node").getString("text");

                    displayText.setText(videoDescription);


                    //image display
                    DownloadImageTask img = new DownloadImageTask(imageUrl, displayImage);
                    img.setImg();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /*
                try {

                    JSONArray jsonArray = response.getJSONArray("display_resources");
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject display_resources =  jsonArray.getJSONObject(i);

                        String src = display_resources.getString("src");
                        displayText.append(src);

                    }

                } catch (JSONException e) {
                    //System.out.println("catch executed");
                    e.printStackTrace();
                }

                 */
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //System.out.println("on error being called");
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }

    private void setImage() {
        try {
            URL url  = new URL(imageUrl);
            videoDisplayImg = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            displayImage.setImageBitmap(videoDisplayImg);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void videoDownload(){
        System.out.println("This is the video:     "+videoDownloadUrl);
        DownloadManager.Request downloadRequest = new DownloadManager.Request(Uri.parse(videoDownloadUrl));

        downloadRequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        downloadRequest.setTitle("testDownload");
        downloadRequest.setDescription("test Description");
        //downloadRequest.allowScanningByMediaScanner(DownloadManager.Request.VISIBILITY_VISIBLE);
        downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        downloadRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"/videoDownloader/" + System.currentTimeMillis() + ".mp4");

        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(downloadRequest);
    }

    public void downloadBtn(View view){
        //if marshmello or above
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //permission denied then request it
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_STORAGE_CODE);
            }
            else{
                //permission granted then downwload it
                videoDownload();
            }
        }else{
            //less then marshmello then just download
            videoDownload();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permission from pop up
                    videoDownload();
                }
                else {
                    Toast.makeText(this, "Permission Denied!!!!", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    //TODO: go to this link. You might have to make a different thread to run this. https://stackoverflow.com/questions/6343166/how-to-fix-android-os-networkonmainthreadexception
}