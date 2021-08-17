package com.alibenalihospital.models;

import java.io.Serializable;
import java.util.List;

public class AddReservationModel implements Serializable {
    private DoctorModel doctorModel;
    private DateModel dateModel;
    private HourModel hourModel;
    private String name;
    private String phone;
    private String callMethod;
    private String gender;
    private String age;
    private List<String> diseases;
    private List<String> images;

    public AddReservationModel(DoctorModel doctorModel, DateModel dateModel, HourModel hourModel, String name, String phone) {
        this.doctorModel = doctorModel;
        this.dateModel = dateModel;
        this.hourModel = hourModel;
        this.name = name;
        this.phone = phone;
    }

    public DoctorModel getDoctorModel() {
        return doctorModel;
    }

    public void setDoctorModel(DoctorModel doctorModel) {
        this.doctorModel = doctorModel;
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
