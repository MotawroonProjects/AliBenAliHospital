package com.alibenalihospital.interfaces;


public interface Listeners {



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
