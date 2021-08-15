package com.alibenalihospital.models;

import java.io.Serializable;

public class CvModel implements Serializable {
    private int id;
    private String title_ar;
    private String title_en;
    private String text_ar;
    private String text_en;
    private int doctor_id;

    public int getId() {
        return id;
    }

    public String getTitle_ar() {
        return title_ar;
    }

    public String getTitle_en() {
        return title_en;
    }

    public String getText_ar() {
        return text_ar;
    }

    public String getText_en() {
        return text_en;
    }

    public int getDoctor_id() {
        return doctor_id;
    }
}
