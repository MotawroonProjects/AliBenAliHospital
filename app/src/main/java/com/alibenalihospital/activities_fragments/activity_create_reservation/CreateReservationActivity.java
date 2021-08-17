package com.alibenalihospital.activities_fragments.activity_create_reservation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.alibenalihospital.R;
import com.alibenalihospital.databinding.ActivityContactUsBinding;
import com.alibenalihospital.databinding.ActivityCreateReservationBinding;
import com.alibenalihospital.language.Language;
import com.alibenalihospital.models.AddReservationModel;
import com.alibenalihospital.models.ContactUsModel;
import com.alibenalihospital.models.UserModel;
import com.alibenalihospital.preferences.Preferences;

import io.paperdb.Paper;

public class CreateReservationActivity extends AppCompatActivity {

    private ActivityCreateReservationBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang = "ar";
    private AddReservationModel model;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_reservation);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        model = (AddReservationModel) intent.getSerializableExtra("data");
    }

    private void initView() {
        Paper.init(this);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.llBack.setOnClickListener(view -> finish());
        binding.setModel(model);

        binding.flMale.setOnClickListener(v -> {
            model.setGender("male");
            binding.setModel(model);
        });

        binding.flFemale.setOnClickListener(v -> {
            model.setGender("female");
            binding.setModel(model);
        });

    }
}