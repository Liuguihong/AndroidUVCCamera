package com.lgh.test;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        String path = getIntent().getStringExtra("path");
        if (!TextUtils.isEmpty(path)) {
            ((ImageView) findViewById(R.id.image)).setImageURI(Uri.parse(path));
        }
    }
}
