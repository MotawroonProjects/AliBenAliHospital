package com.alibenalihospital.models;

import java.io.Serializable;

public class RateModel implements Serializable {
    private int id;
    private float rate;
    private int doctor_id;
    private String offer_id;
    private int user_id;
    private String title;
    private String created_at;
    private String updated_at;
    private UserModel.User user;

    public int getId() {
        return id;
    }

    public float getRate() {
        return rate;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public String getOffer_id() {
        return offer_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getTitle() {
        return title;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public UserModel.User getUser() {
        return user;
    }
}
