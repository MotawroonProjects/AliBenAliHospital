package com.alibenalihospital.adapters;

import com.alibenalihospital.models.DoctorModel;
import com.alibenalihospital.models.StatusResponse;

import java.io.Serializable;

public class SingleDoctorModel extends StatusResponse implements Serializable {
    private DoctorModel data;

    public DoctorModel getData() {
        return data;
    }
}
