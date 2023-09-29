package com.example.financialkeywordextractor;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ImagePreview extends AppCompatActivity {


    Uri uri;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.image_preview);
        ImageView ImgPre = (ImageView)findViewById(R.id.imgpre);
        setContentView(R.layout.image_preview);
        Bundle extra = getIntent().getExtras();
        if (extra != null && extra.containsKey("KEY")){
            uri = Uri.parse(extra.getString("KEY"));
            ImgPre.setImageURI(uri);
        }
    }
}
