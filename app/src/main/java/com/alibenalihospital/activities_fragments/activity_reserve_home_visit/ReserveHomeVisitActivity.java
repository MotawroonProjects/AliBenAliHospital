package com.alibenalihospital.activities_fragments.activity_reserve_home_visit;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibenalihospital.R;
import com.alibenalihospital.adapters.NotificationAdapter;
import com.alibenalihospital.databinding.ActivityNotificationBinding;
import com.alibenalihospital.databinding.ActivityReserveHomeVisitBinding;
import com.alibenalihospital.language.Language;
import com.alibenalihospital.models.HomeVisitModel;
import com.alibenalihospital.models.NotificationModel;
import com.alibenalihospital.models.SelectedLocation;
import com.alibenalihospital.models.UserModel;
import com.alibenalihospital.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class ReserveHomeVisitActivity extends AppCompatActivity {
    private ActivityReserveHomeVisitBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;
    private SelectedLocation location;
    private HomeVisitModel model;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reserve_home_visit);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        location = (SelectedLocation) intent.getSerializableExtra("location");

    }


    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        model = new HomeVisitModel();

        if (userModel!=null){

        }

        binding.setModel(model);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);


        binding.llBack.setOnClickListener(view -> finish());
    }


}
