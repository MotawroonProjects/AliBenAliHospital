package com.alibenalihospital.models;

import java.io.Serializable;

public class FileModel implements Serializable {
    private int id;
    private int reservation_id;
    private String file;

    public int getId() {
        return id;
    }

    public int getReservation_id() {
        return reservation_id;
    }

    public String getFile() {
        return file;
    }
}
