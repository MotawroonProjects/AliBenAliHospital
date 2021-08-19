package com.alibenalihospital.activities_fragments.activity_home.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
    private ActivityResultLauncher<Intent> launcher;
    private int request;


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

        if (userModel != null) {
            binding.tvLogin.setText(getResources().getString(R.string.logout));
        }

    }


    @Override
    public void onLogIn() {
        if (userModel == null) {
            request = 1;
            Intent intent = new Intent(activity, LoginActivity.class);
            launcher.launch(intent);
        } else {
            activity.logout();

        }
    }

    @Override
    public void onFavorite() {
        Intent intent = new Intent(activity, FavoriteActivity.class);
        startActivity(intent);
    }

    @Override
    public void onEditProfile() {
        request = 2;
        Intent intent = new Intent(activity, SignUpActivity.class);
        launcher.launch(intent);
    }

    @Override
    public void onLanguageSetting() {
        request = 3;
        Intent intent = new Intent(activity, LanguageActivity.class);
        launcher.launch(intent);
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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

            if (result.getResultCode() == RESULT_OK) {

                if (request == 1) {
                    userModel = preferences.getUserData(activity);
                    binding.setModel(userModel);
                    if (userModel != null) {
                        binding.tvLogin.setText(getResources().getString(R.string.logout));
                    }
                    activity.refreshFragmentReservation();
                } else if (request == 2) {
                    userModel = Preferences.getInstance().getUserData(activity);
                    binding.setModel(userModel);
                } else if (request == 3 && result.getData() != null) {
                    String lang = result.getData().getStringExtra("lang");
                    activity.refreshActivity(lang);
                }

            }
        });
    }
}
