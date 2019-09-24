package com.example.homework;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DetailsActivity extends AppCompatActivity {

    private ImageView mIvDetalis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        initView();
    }

    private void initView() {
        mIvDetalis = (ImageView) findViewById(R.id.iv_detalis);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        Glide.with(this).load(url).into(mIvDetalis);
    }
}
