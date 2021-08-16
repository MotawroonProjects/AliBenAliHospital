package com.alibenalihospital.models;

import java.io.Serializable;

public class ReservationDiseasesModel implements Serializable {
   private int id;
   private int disease_id;
   private int reservation_id;
   private DiseasesModel diseases;

    public int getId() {
        return id;
    }

    public int getDisease_id() {
        return disease_id;
    }

    public int getReservation_id() {
        return reservation_id;
    }

    public DiseasesModel getDiseases() {
        return diseases;
    }
}
