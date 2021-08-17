package com.alibenalihospital.models;

import java.io.Serializable;
import java.util.List;

public class DiseasesDataModel extends StatusResponse implements Serializable {
    private List<DiseasesModel> data;

    public List<DiseasesModel> getData() {
        return data;
    }
}
