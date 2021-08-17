package com.alibenalihospital.models;

import java.io.Serializable;
import java.util.List;

public class DateModel implements Serializable {
   private int id;
   private int doctor_id;
   private String offer_id;
   private String date;
   private String is_reserved;
   private String day_number;
   private String day_text;
   private String month;
   private boolean isSelected = false;
   private List<HourModel> available_hour;

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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public List<HourModel> getAvailable_hour() {
        return available_hour;
    }
}
