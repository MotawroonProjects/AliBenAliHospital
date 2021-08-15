package com.alibenalihospital.models;

import java.io.Serializable;

public class ClinicModel implements Serializable {
    private int id;
    private String image;
    private String name_ar;
    private String name_en;
    private String created_at;
    private String updated_at;
    private String name;

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getName_ar() {
        return name_ar;
    }

    public String getName_en() {
        return name_en;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getName() {
        return name;
    }
}
