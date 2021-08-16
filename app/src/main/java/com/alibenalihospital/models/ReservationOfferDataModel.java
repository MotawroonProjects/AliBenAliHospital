package com.alibenalihospital.models;

import java.io.Serializable;
import java.util.List;

public class ReservationOfferDataModel extends StatusResponse implements Serializable {
    private List<ReservationOfferModel> data;

    public List<ReservationOfferModel> getData() {
        return data;
    }
}
