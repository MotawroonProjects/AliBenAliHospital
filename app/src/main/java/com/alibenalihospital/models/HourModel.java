package com.alibenalihospital.models;

import java.io.Serializable;

public class HourModel implements Serializable {
    private String id;
    private int date_id;
    private String hour;
    private String is_reserved;
    private String phone_hour;
    private String period;

    public String getId() {
        return id;
    }

    public int getDate_id() {
        return date_id;
    }

    public String getHour() {
        return hour;
    }

    public String getIs_reserved() {
        return is_reserved;
    }

    public String getPhone_hour() {
        return phone_hour;
    }

    public String getPeriod() {
        return period;
    }
}
