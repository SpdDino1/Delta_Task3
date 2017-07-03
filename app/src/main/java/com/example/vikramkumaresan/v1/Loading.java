package com.example.vikramkumaresan.v1;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

public class Loading extends AppCompatActivity {
    public static Loading ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        ctx=this;

        ImageView img = (ImageView)findViewById(R.id.imgload);
        Glide.with(this).load(R.mipmap.loading).into(img);
    }
}
