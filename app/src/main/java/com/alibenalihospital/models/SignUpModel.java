package com.alibenalihospital.models;

import android.content.Context;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.alibenalihospital.BR;
import com.alibenalihospital.R;


public class SignUpModel extends BaseObservable {
    private String name;
    private String phone_code;
    private String phone;
    private String password;
    private String re_password;

    public ObservableField<String> error_phone = new ObservableField<>();
    public ObservableField<String> error_password = new ObservableField<>();
    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_re_password = new ObservableField<>();


    public boolean isDataValid(Context context) {
        if (!name.trim().isEmpty() &&
                !phone.isEmpty() &&
                !password.isEmpty() &&
                password.length() >= 6 && password.equals(re_password)) {
            error_phone.set(null);
            error_password.set(null);
            error_name.set(null);
            error_re_password.set(null);
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

            if (password.isEmpty()) {
                error_password.set(context.getString(R.string.field_req));
            } else if (password.length() < 6) {
                error_password.set(context.getString(R.string.password_short));
            } else {
                error_password.set(null);

                if (!password.equals(re_password)) {
                    error_re_password.set(context.getString(R.string.pas_not_match));
                } else {
                    error_re_password.set(null);

                }
            }

            return false;
        }
    }

    public SignUpModel() {
        setName("");
        setPhone_code("+966");
        setPhone("");
        setPassword("");
        setRe_password("");


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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Bindable
    public String getRe_password() {
        return re_password;
    }

    public void setRe_password(String re_password) {
        this.re_password = re_password;
        notifyPropertyChanged(BR.re_password);
    }
}
