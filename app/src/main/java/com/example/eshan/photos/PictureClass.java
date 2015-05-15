package com.example.eshan.photos;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by ashish on 4/28/2015.
 */
public class PictureClass {
    public Drawable thumbnail;
    public String name;
    public String album;
    public String caption;
    public String location;
    public String imageUrl;

    public PictureClass(String name,String caption,String url,String location){
        this.name = name;
        this.caption = caption;
//        this.thumbnail = thumbnail;
        this.imageUrl = url;
        this.location = location;
    }
}