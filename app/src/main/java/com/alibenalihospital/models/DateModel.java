package com.alibenalihospital.models;

import java.io.Serializable;

public class DateModel implements Serializable {
   private int id;
   private int doctor_id;
   private String offer_id;
   private String date;
   private String is_reserved;
   private String day_number;
   private String day_text;
   private String month;

    public int getId() {
        return id;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public String getOffer_id() {
        return offer_id;
    }

    public String getDate() {
        return date;
    }

    public String getIs_reserved() {
        return is_reserved;
    }

    public String getDay_number() {
        return day_number;
    }

    public String getDay_text() {
        return day_text;
    }

    public String getMonth() {
        return month;
    }
}
