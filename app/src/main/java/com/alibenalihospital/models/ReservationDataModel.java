package com.alibenalihospital.models;

import java.io.Serializable;
import java.util.List;

public class ReservationDataModel extends StatusResponse implements Serializable {
    private List<ReservationModel> data;

    public List<ReservationModel> getData() {
        return data;
    }
}
