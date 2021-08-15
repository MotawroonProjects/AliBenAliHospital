package com.alibenalihospital.models;

import java.io.Serializable;

public class CountryModel implements Serializable {
    private int id;
    private String name_ar;
    private String name_en;
    private String image;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public String getName_ar() {
        return name_ar;
    }

    public String getName_en() {
        return name_en;
    }

    public String getImage() {
        return image;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
