package com.alibenalihospital.models;

import java.io.Serializable;
import java.util.List;

public class ReservationOfferModel implements Serializable {
    private int id;
    private int doctor_id;
    private String offer_id;
    private int date_id;
    private int hour_id;
    private String disease_id;
    private int user_id;
    private String name;
    private String phone;
    private String type;
    private String call_type;
    private String gender;
    private String age;
    private String image;
    private String status;
    private String is_deleted;
    private String report_text;
    private String can_deleted;
    private String can_canceled;
    private String can_updated;
    private OfferDataModel.OfferData offer;
    private DateModel date;

    private HourModel hour;
    private List<ReservationDiseasesModel> reservation_diseases;
    private List<FileModel> files;

    public int getId() {
        return id;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public String getOffer_id() {
        return offer_id;
    }

    public int getDate_id() {
        return date_id;
    }

    public int getHour_id() {
        return hour_id;
    }

    public String getDisease_id() {
        return disease_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getType() {
        return type;
    }

    public String getCall_type() {
        return call_type;
    }

    public String getGender() {
        return gender;
    }

    public String getAge() {
        return age;
    }

    public String getImage() {
        return image;
    }

    public String getStatus() {
        return status;
    }

    public String getIs_deleted() {
        return is_deleted;
    }

    public String getReport_text() {
        return report_text;
    }

    public String getCan_deleted() {
        return can_deleted;
    }

    public String getCan_canceled() {
        return can_canceled;
    }

    public String getCan_updated() {
        return can_updated;
    }

    public OfferDataModel.OfferData getOffer() {
        return offer;
    }

    public HourModel getHour() {
        return hour;
    }

    public List<ReservationDiseasesModel> getReservation_diseases() {
        return reservation_diseases;
    }

    public List<FileModel> getFiles() {
        return files;
    }

    public DateModel getDate() {
        return date;
    }

    public void setIs_deleted(String is_deleted) {
        this.is_deleted = is_deleted;
    }
}
