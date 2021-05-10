package com.alibenalihospital.models;

import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.alibenalihospital.BR;
import com.alibenalihospital.R;


public class HomeVisitModel extends BaseObservable {
    private String name;
    private String phone_code;
    private String phone;
    private String details;

    public ObservableField<String> error_phone = new ObservableField<>();
    public ObservableField<String> error_details = new ObservableField<>();
    public ObservableField<String> error_name = new ObservableField<>();


    public boolean isDataValid(Context context) {
        if (!name.trim().isEmpty() &&
                !phone.isEmpty() &&
                !details.isEmpty()) {
            error_phone.set(null);
            error_details.set(null);
            error_name.set(null);
            return true;
        } else {
            if (name.isEmpty()) {
                error_name.set(context.getString(R.string.field_required));

            } else {
                error_name.set(null);

            }
            if (phone.isEmpty()) {
                error_phone.set(context.getString(R.string.field_req));

            } else {
                error_phone.set(null);

            }

            if (details.isEmpty()) {
                error_details.set(context.getString(R.string.field_req));
            } else {
                error_details.set(null);

            }

            return false;
        }
    }

    public HomeVisitModel() {
        setName("");
        setPhone_code("+966");
        setPhone("");
        setDetails("");


    }


    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);

    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);

    }

    @Bindable
    public String getDetails() {
        return details;
    }

    public void setDetails(String password) {
        this.details = details;
        notifyPropertyChanged(BR.details);

    }


}
