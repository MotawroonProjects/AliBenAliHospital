package com.alibenalihospital.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DefaultSettings implements Serializable {
    private boolean isLanguageSelected = false;
    private String ringToneUri="";
    private String ringToneName;

    public DefaultSettings() {
    }

    public boolean isLanguageSelected() {
        return isLanguageSelected;
    }

    public void setLanguageSelected(boolean languageSelected) {
        isLanguageSelected = languageSelected;
    }


    public String getRingToneUri() {
        return ringToneUri;
    }

    public void setRingToneUri(String ringToneUri) {
        this.ringToneUri = ringToneUri;
    }

    public String getRingToneName() {
        return ringToneName;
    }

    public void setRingToneName(String ringToneName) {
        this.ringToneName = ringToneName;
    }

}
