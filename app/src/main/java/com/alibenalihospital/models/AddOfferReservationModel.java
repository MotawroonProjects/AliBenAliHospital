package com.alibenalihospital.models;

import java.io.Serializable;
import java.util.List;

public class AddOfferReservationModel implements Serializable {
    private OfferDataModel.OfferData offerModel;
    private DateModel dateModel;
    private HourModel hourModel;
    private String name;
    private String phone;
    private String callMethod = "audio";
    private String gender;
    private String age;
    private List<String> diseases;
    private List<String> images;

    public AddOfferReservationModel(OfferDataModel.OfferData offerModel, DateModel dateModel, HourModel hourModel, String name, String phone) {
        this.offerModel = offerModel;
        this.dateModel = dateModel;
        this.hourModel = hourModel;
        this.name = name;
        this.phone = phone;
    }


    public OfferDataModel.OfferData getOfferModel() {
        return offerModel;
    }

    public void setOfferModel(OfferDataModel.OfferData offerModel) {
        this.offerModel = offerModel;
    }

    public DateModel getDateModel() {
        return dateModel;
    }

    public void setDateModel(DateModel dateModel) {
        this.dateModel = dateModel;
    }

    public HourModel getHourModel() {
        return hourModel;
    }

    public String getCallMethod() {
        return callMethod;
    }

    public void setCallMethod(String callMethod) {
        this.callMethod = callMethod;
    }

    public void setHourModel(HourModel hourModel) {
        this.hourModel = hourModel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public List<String> getDiseases() {
        return diseases;
    }

    public void setDiseases(List<String> diseases) {
        this.diseases = diseases;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
