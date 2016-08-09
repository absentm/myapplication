package com.example.dm.myapplication.beans;

import java.io.Serializable;

/**
 * Created by DUAN on 2016/5/15.
 */
public class News implements Serializable {
    private int id;
    private String title;
    private String image;

    public News() {
    }

    public News(int id, String title, String image) {
        this.id = id;
        this.title = title;
        this.image = image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {

        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }
}
