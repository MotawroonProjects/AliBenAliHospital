package com.alibenalihospital.models;

import java.io.Serializable;

public class SliderModel implements Serializable {
    private int id;
    private int offer_id;
    private String image;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public int getOffer_id() {
        return offer_id;
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
