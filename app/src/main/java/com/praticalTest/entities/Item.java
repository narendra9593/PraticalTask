package com.praticalTest.entities;

/**
 * Created by Andy on 14-10-2018.
 */
public class Item {
    String title;
    String description;
    String imageHref;

    public Item() {
    }


    public String getName() {
        return title;
    }

    public void setName(String name) {
        this.title = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageHref() {
        return imageHref;
    }

    public void setImageHref(String imageHref) {
        this.imageHref = imageHref;
    }
}
