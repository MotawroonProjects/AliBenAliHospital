package com.alibenalihospital.models;

import java.io.Serializable;

public class NotificationModel implements Serializable {
    private int id;
    private String title;
    private String message;
    private String date;
    private String can_rate;
    private NotReservationModel reservation;



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

    public String getCan_rate() {
        return can_rate;
    }

    public NotReservationModel getReservation() {
        return reservation;
    }
}
