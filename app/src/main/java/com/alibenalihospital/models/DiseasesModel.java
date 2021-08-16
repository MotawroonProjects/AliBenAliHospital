package com.alibenalihospital.models;

import java.io.Serializable;

public class DiseasesModel implements Serializable {
    private int id;
    private String title_ar;
    private String title_en;
    private String title;

    public int getId() {
        return id;
    }

    public String getTitle_ar() {
        return title_ar;
    }

    public String getTitle_en() {
        return title_en;
    }

    public String getTitle() {
        return title;
    }
}
