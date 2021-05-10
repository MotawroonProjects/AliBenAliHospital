package com.alibenalihospital.activities_fragments.activity_doctor_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;

import com.alibenalihospital.R;
import com.alibenalihospital.adapters.NotificationAdapter;
import com.alibenalihospital.adapters.RateAdapter;
import com.alibenalihospital.databinding.ActivityDoctorDetailsBinding;
import com.alibenalihospital.databinding.ActivityNotificationBinding;
import com.alibenalihospital.language.Language;
import com.alibenalihospital.models.NotificationModel;
import com.alibenalihospital.models.UserModel;
import com.alibenalihospital.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class DoctorDetailsActivity extends AppCompatActivity {
    private ActivityDoctorDetailsBinding binding;
    private String lang;
    private RateAdapter adapter;
    private List<Object> list;
    private Preferences preferences;
    private UserModel userModel;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_details);
        initView();
    }


    private void initView() {
        list = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.recView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        adapter = new RateAdapter(list, this);
        binding.recView.setAdapter(adapter);


        binding.llBack.setOnClickListener(view -> finish());
    }
}