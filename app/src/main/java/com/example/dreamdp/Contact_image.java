package com.example.dreamdp;

import android.graphics.drawable.Drawable;

public class Contact_image {
    private Drawable pic ;
    private String name ;
    private String number ;

    public void setIcon(Drawable icon) {
        pic = icon ;
    }
    public void setTitle(String title) {
        name = title ;
    }
    public void setDesc(String desc) {
        number = desc ;
    }

    public Drawable getIcon() {
        return this.pic ;
    }
    public String getTitle() {
        return this.name ;
    }
    public String getDesc() {
        return this.number ;
    }
}

