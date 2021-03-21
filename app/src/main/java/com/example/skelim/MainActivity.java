package com.example.skelim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Intent intent = new Intent();

    EditText url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        url = (EditText) findViewById(R.id.url);


    }

    public void searchBtn(View view){
        Intent search = new Intent(this, videoDownload.class);
        search.putExtra("url", url.getText().toString());
        startActivity(search);
    }
}