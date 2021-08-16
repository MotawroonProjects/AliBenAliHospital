package com.alibenalihospital.activities_fragments.activity_home.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.alibenalihospital.R;
import com.alibenalihospital.activities_fragments.activity_about_us.AboutUsActivity;
import com.alibenalihospital.activities_fragments.activity_contact_us.ContactUsActivity;
import com.alibenalihospital.activities_fragments.activity_favorite.FavoriteActivity;
import com.alibenalihospital.activities_fragments.activity_home.HomeActivity;
import com.alibenalihospital.activities_fragments.activity_language.LanguageActivity;
import com.alibenalihospital.activities_fragments.activity_login.LoginActivity;
import com.alibenalihospital.activities_fragments.activity_sign_up.SignUpActivity;
import com.alibenalihospital.databinding.FragmentMoreBinding;
import com.alibenalihospital.interfaces.Listeners;
import com.alibenalihospital.models.UserModel;
import com.alibenalihospital.preferences.Preferences;

import io.paperdb.Paper;

public class Fragment_More extends Fragment implements Listeners.SettingAction {

    private HomeActivity activity;
    private FragmentMoreBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;



    public static Fragment_More newInstance() {
        return new Fragment_More();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more, container, false);
        initView();
        return binding.getRoot();
    }


    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setModel(userModel);
        binding.setAction(this);


    }


    @Override
    public void onLogIn() {
        Intent intent = new Intent(activity, LoginActivity.class);
        startActivityForResult(intent, 100);
    }

    @Override
    public void onFavorite() {
       /* Intent intent = new Intent(activity, FavoriteActivity.class);
        startActivity(intent);*/
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public void onEditProfile() {
        Intent intent = new Intent(activity, SignUpActivity.class);
        startActivityForResult(intent, 200);
    }

    @Override
    public void onLanguageSetting() {
        Intent intent = new Intent(activity, LanguageActivity.class);
        startActivityForResult(intent, 300);
    }

    @Override
    public void onAbout() {
        Intent intent = new Intent(activity, AboutUsActivity.class);
        intent.putExtra("type", 1);
        startActivity(intent);
    }

    @Override
    public void onTerms() {
        Intent intent = new Intent(activity, AboutUsActivity.class);
        intent.putExtra("type", 2);
        startActivity(intent);
    }

    @Override
    public void onContactUs() {
        Intent intent = new Intent(activity, ContactUsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLogout() {
        activity.logout();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100){
            if (resultCode== Activity.RESULT_OK){
                userModel = preferences.getUserData(activity);
                binding.setModel(userModel);
            }
        }else if (requestCode==200){
            if (resultCode== Activity.RESULT_OK){
                userModel = preferences.getUserData(activity);
                binding.setModel(userModel);

            }
        }else if (requestCode==300){
            if (resultCode== Activity.RESULT_OK&&data!=null){
                String lang = data.getStringExtra("lang");
                activity.refreshActivity(lang);
            }
        }
    }
}
