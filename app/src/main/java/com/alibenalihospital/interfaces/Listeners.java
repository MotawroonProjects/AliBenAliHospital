package com.alibenalihospital.interfaces;


import com.alibenalihospital.models.DoctorModel;
import com.alibenalihospital.models.ReservationModel;
import com.alibenalihospital.models.ReservationOfferModel;

public interface Listeners {

    interface DoctorListener{
        void setDoctorData(DoctorModel model);

    }
    interface FavListener{
        void setDoctorData(DoctorModel model);
        void unFav(DoctorModel model,int pos);

    }

    interface ReservationListener{
        void setReservationData(ReservationModel model,int pos,int type);

    }

    interface ReservationOfferListener{
        void setReservationData(ReservationOfferModel model, int pos, int type);

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
