package com.example.fidelmomolo.myapplication;

/**
 * Created by Fidel M Omolo on 2/11/2018.
 */

public class Upload {
    private String mName;
    private String mImageUrl;

    public Upload() {

    }


    public Upload(String name, String imageUrl) {

        if (name.trim().equals("")){

            name="No Name";
        }

        this.mName = name;
        this.mImageUrl = imageUrl;

    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }
}
