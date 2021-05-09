package com.alibenalihospital.models;

import java.io.Serializable;

public class SliderModel implements Serializable {
    private int id;
    private String title;
    private String image;
    private String is_shown;

    public int getId() {

        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getIs_shown() {
        return is_shown;
    }
}
