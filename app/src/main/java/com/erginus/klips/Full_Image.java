package com.erginus.klips;

import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;

import com.erginus.klips.Commons.CustomImageVIew;
import com.erginus.klips.Commons.Prefshelper;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class Full_Image extends AppCompatActivity {
    ImageView imageView_home1,back;
    String image="",type="";
    Prefshelper prefshelper;
  CustomImageVIew full_image;
    URL url;
TextView txt_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image);
        prefshelper = new Prefshelper(this);
        full_image=(CustomImageVIew)findViewById(R.id.full);
        back = (ImageView) findViewById(R.id.imageView_back);
        txt_title = (TextView) findViewById(R.id.toolbar_title);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imageView_home1 = (ImageView) findViewById(R.id.imageView_home);
        image = getIntent().getStringExtra("pic");
        type= getIntent().getStringExtra("image_quote");
        if(type.equalsIgnoreCase("image"))
        {
            txt_title.setText("Image");
        }
        else
        {
            txt_title.setText("Quote");
        }
        Picasso.with(Full_Image.this).load(image).into(full_image);

        try {
            url = new URL (image);
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            full_image.setImageBitmap(image);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageView_home1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefshelper.getLoginWithFromPreference().equals("0")) {
                    Intent intent = new Intent(Full_Image.this, GuestHomeActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Full_Image.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

}