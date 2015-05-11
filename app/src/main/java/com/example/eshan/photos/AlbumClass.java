package com.example.eshan.photos;

import android.graphics.drawable.Drawable;


/**
 * Created by root on 4/27/15.
 */
public class AlbumClass {

    public final Drawable icon;
    public final String title;
    public final String description;
    public AlbumClass(String title,String description,Drawable icon){
        this.title = title;
        this.description = description;
        this.icon = icon;
    }

}
