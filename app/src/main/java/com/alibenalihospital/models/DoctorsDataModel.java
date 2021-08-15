package com.alibenalihospital.models;

import java.io.Serializable;
import java.util.List;

public class DoctorsDataModel extends StatusResponse implements Serializable {
    private List<DoctorModel> data;

    public List<DoctorModel> getData() {
        return data;
    }
}
