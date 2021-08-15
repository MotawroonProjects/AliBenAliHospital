package com.alibenalihospital.interfaces;


import com.alibenalihospital.models.DoctorModel;

public interface Listeners {

    interface DoctorListener{
        void setDoctorData(DoctorModel model);

    }
    interface FavListener{
        void setDoctorData(DoctorModel model);
        void unFav(DoctorModel model,int pos);

    }


    interface SettingAction{
        void onLogIn();
        void onFavorite();
        void onEditProfile();
        void onLanguageSetting();
        void onAbout();
        void onTerms();
        void onContactUs();
        void onLogout();
    }
    interface BackListener
    {
        void back();
    }


}
