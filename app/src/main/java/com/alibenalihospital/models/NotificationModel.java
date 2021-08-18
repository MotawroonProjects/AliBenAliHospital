package com.alibenalihospital.models;

import java.io.Serializable;

public class NotificationModel implements Serializable {
    private int id;
    private String title;
    private String message;
    private String date;


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }
}
