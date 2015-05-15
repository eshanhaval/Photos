package com.example.eshan.photos;

import android.graphics.drawable.Drawable;


/**
 * Created by root on 4/27/15.
 */
public class AlbumClass {

    public final Drawable icon;
    public final String title;
    public final String description;
    public final String uniqueId;
    public final String email;
    public AlbumClass(String title,String description,Drawable icon, String uniqueId, String email){
        this.title = title;
        this.description = description;
        this.icon = icon;
        this.uniqueId = uniqueId;
        this.email = email;
    }

}
