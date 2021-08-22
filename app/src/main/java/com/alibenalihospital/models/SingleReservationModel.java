package com.alibenalihospital.models;

import java.io.Serializable;

public class SingleReservationModel extends StatusResponse implements Serializable {
    private ReservationModel data;

    public ReservationModel getData() {
        return data;
    }
}
