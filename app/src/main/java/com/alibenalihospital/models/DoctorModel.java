package com.alibenalihospital.models;

import java.io.Serializable;
import java.util.List;

public class DoctorModel implements Serializable {
    private int id;
    private String email;
    private String phone;
    private String type;
    private String name_ar;
    private String name_en;
    private String category_ar;
    private String category_en;
    private String image;
    private int country_id;
    private int clinic_id;
    private String min_age;
    private String max_age;
    private String address;
    private String latitude;
    private String longitude;
    private String status;
    private String price;
    private String rate;
    private String audio;
    private String video;
    private String is_advisory;
    private String advisory_languages;
    private String has_home_visit;
    private String created_at;
    private String updated_at;
    private String is_favourate;
    private String name;
    private String category;
    private ClinicModel clinic;
    private CountryModel country;
    private List<RateModel> rates;
    private List<Object> available_date;


    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getType() {
        return type;
    }

    public String getName_ar() {
        return name_ar;
    }

    public String getName_en() {
        return name_en;
    }

    public String getCategory_ar() {
        return category_ar;
    }

    public String getCategory_en() {
        return category_en;
    }

    public String getImage() {
        return image;
    }

    public int getCountry_id() {
        return country_id;
    }

    public int getClinic_id() {
        return clinic_id;
    }

    public String getMin_age() {
        return min_age;
    }

    public String getMax_age() {
        return max_age;
    }

    public String getAddress() {
        return address;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getStatus() {
        return status;
    }

    public String getPrice() {
        return price;
    }

    public String getRate() {
        return rate;
    }

    public String getAudio() {
        return audio;
    }

    public String getVideo() {
        return video;
    }

    public String getIs_advisory() {
        return is_advisory;
    }

    public String getAdvisory_languages() {
        return advisory_languages;
    }

    public String getHas_home_visit() {
        return has_home_visit;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getIs_favourate() {
        return is_favourate;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public ClinicModel getClinic() {
        return clinic;
    }

    public CountryModel getCountry() {
        return country;
    }

    public List<RateModel> getRates() {
        return rates;
    }

    public List<Object> getAvailable_date() {
        return available_date;
    }
}
