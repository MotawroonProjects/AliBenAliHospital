package com.alibenalihospital.models;

import java.io.Serializable;

public class SingleReservationOfferModel extends StatusResponse implements Serializable {
    private ReservationOfferModel data;

    public ReservationOfferModel getData() {
        return data;
    }
}
