package com.alibenalihospital.models;

import java.io.Serializable;

public class StatusResponse implements Serializable {
    protected int code;
    protected String message;

    public int getStatus() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
