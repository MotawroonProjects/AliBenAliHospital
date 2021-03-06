package com.alibenalihospital.activities_fragments.activity_home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.alibenalihospital.R;
import com.alibenalihospital.activities_fragments.activity_home.fragments.Fragment_Home;
import com.alibenalihospital.activities_fragments.activity_home.fragments.Fragment_More;
import com.alibenalihospital.activities_fragments.activity_home.fragments.Fragment_Search;
import com.alibenalihospital.activities_fragments.activity_home.fragments.fragment_reservations.Fragment_My_Reservations;
import com.alibenalihospital.activities_fragments.activity_splash.SplashActivity;
import com.alibenalihospital.databinding.ActivityHomeBinding;
import com.alibenalihospital.language.Language;

import com.alibenalihospital.models.StatusResponse;
import com.alibenalihospital.models.UserModel;
import com.alibenalihospital.preferences.Preferences;
import com.alibenalihospital.remote.Api;
import com.alibenalihospital.share.Common;
import com.alibenalihospital.tags.Tags;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private Preferences preferences;
    private FragmentManager fragmentManager;
    private Fragment_Home fragment_home;
    private Fragment_My_Reservations fragment_my_reservations;
    private Fragment_Search fragment_search;
    private Fragment_More fragment_more;
    private UserModel userModel;
    private String lang;
    private boolean backPressed= false;


    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();


    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);


        binding.setModel(userModel);

        displayFragmentMain();


        if (userModel != null) {
           // EventBus.getDefault().register(this);
            updateFirebaseToken();
        }

        binding.bottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull  MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.search:
                        displayFragmentSearch();
                        break;
                    case R.id.books:
                        displayFragmentMyReservation();
                        break;
                    case R.id.more:
                        displayFragmentMore();
                        break;
                    default:
                        if (!backPressed){
                            displayFragmentMain();
                        }
                        break;
                }
                return true;
            }
        });

    }


    public void displayFragmentMain() {
        try {
            if (fragment_home == null) {
                fragment_home = Fragment_Home.newInstance();
            }


            if (fragment_search != null && fragment_search.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_search).commit();
            }
            if (fragment_my_reservations != null && fragment_my_reservations.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_my_reservations).commit();
            }

            if (fragment_more != null && fragment_more.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_more).commit();
            }

            if (fragment_home.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_home).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_home, "fragment_home").commit();

            }
        } catch (Exception e) {
        }

    }

    public void displayFragmentSearch() {

        try {

            if (fragment_search == null) {
                fragment_search = Fragment_Search.newInstance();
            }


            if (fragment_home != null && fragment_home.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_home).commit();
            }


            if (fragment_my_reservations != null && fragment_my_reservations.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_my_reservations).commit();
            }

            if (fragment_more != null && fragment_more.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_more).commit();
            }

            if (fragment_search.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_search).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_search, "fragment_myDates").commit();

            }
        } catch (Exception e) {
        }
    }

    public void displayFragmentMyReservation() {

        try {


            if (fragment_my_reservations == null) {
                fragment_my_reservations = Fragment_My_Reservations.newInstance();
            }

            if (fragment_my_reservations.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_my_reservations).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_my_reservations, "fragment_myTreatments").commit();

            }

            if (fragment_home != null && fragment_home.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_home).commit();
            }
            if (fragment_search != null && fragment_search.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_search).commit();
            }

            if (fragment_more != null && fragment_more.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_more).commit();
            }


        } catch (Exception e) {
        }
    }

    public void displayFragmentMore() {

        try {
            if (fragment_more == null) {
                fragment_more = Fragment_More.newInstance();
            }


            if (fragment_home != null && fragment_home.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_home).commit();
            }
            if (fragment_search != null && fragment_search.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_search).commit();
            }

            if (fragment_my_reservations != null && fragment_my_reservations.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_my_reservations).commit();
            }


            if (fragment_more.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_more).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_more, "fragment_profile").commit();

            }
            binding.setTitle(getString(R.string.profile));
        } catch (Exception e) {
        }
    }


    public void refreshActivity(String lang) {
        Paper.book().write("lang", lang);
        Language.setNewLocale(this, lang);
        new Handler()
                .postDelayed(() -> {

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }, 500);


    }



    @Override
    public void onBackPressed() {
        backPressed = true;
        binding.bottomNavView.setSelectedItemId(R.id.home);
        backPressed = false;

        if (fragment_home != null && fragment_home.isAdded() && fragment_home.isVisible()) {
            finish();
        } else {
            displayFragmentMain();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void refreshFragmentReservation(){
        if (fragment_my_reservations!=null&&fragment_my_reservations.isAdded()){
            fragment_my_reservations.refresh();
        }
    }
    private void updateFirebaseToken() {
        FirebaseInstanceId.getInstance()
                .getInstanceId().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        String token = task.getResult().getToken();
                        try {
                            Api.getService(Tags.base_url)
                                    .updateFirebaseToken(lang,userModel.getUser().getId()+"",token, "android")
                                    .enqueue(new Callback<StatusResponse>() {
                                        @Override
                                        public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                                            if (response.isSuccessful() && response.body() != null) {
                                                userModel.getUser().setFirebaseToken(token);
                                                preferences.create_update_userdata(HomeActivity.this, userModel);

                                                Log.e("token", "updated successfully");
                                            } else {
                                                try {

                                                    Log.e("errorToken", response.code() + "_" + response.errorBody().string());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<StatusResponse> call, Throwable t) {
                                            try {

                                                if (t.getMessage() != null) {
                                                    Log.e("errorToken2", t.getMessage());
                                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                                        Toast.makeText(HomeActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                            } catch (Exception e) {
                                            }
                                        }
                                    });
                        } catch (Exception e) {

                        }
                    }
                });
    }

    public void logout() {

        if (userModel==null){
            finish();
            return;
        }
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .logout(lang, userModel.getUser().getId()+"", userModel.getUser().getFirebaseToken())
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                preferences.clear(HomeActivity.this);
                                navigateToSplashActivity();
                            }

                        } else {
                            dialog.dismiss();
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(HomeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(HomeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

    }

    private void navigateToSplashActivity() {
        Intent intent  = new Intent(this, SplashActivity.class);
        startActivity(intent);
        finish();
    }

}
