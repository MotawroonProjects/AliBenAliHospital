package com.alibenalihospital.activities_fragments.activty_reservstion_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.alibenalihospital.R;
import com.alibenalihospital.activities_fragments.activity_images.ImagesActivity;
import com.alibenalihospital.databinding.ActivityCreateReservationBinding;
import com.alibenalihospital.databinding.ActivityReservationDetailsBinding;
import com.alibenalihospital.language.Language;
import com.alibenalihospital.models.AddReservationModel;
import com.alibenalihospital.models.FileModel;
import com.alibenalihospital.models.ReservationModel;
import com.alibenalihospital.models.UserModel;
import com.alibenalihospital.preferences.Preferences;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class ReservationDetailsActivity extends AppCompatActivity {
    private ActivityReservationDetailsBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang = "ar";
    private ReservationModel model;
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reservation_details);
        getDataFromIntent();
        initView();

    }
    private void getDataFromIntent() {
        Intent intent = getIntent();
        model = (ReservationModel) intent.getSerializableExtra("data");
    }

    private void initView() {
        Paper.init(this);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.llBack.setOnClickListener(view -> finish());
        binding.setModel(model);
        if (model.getFiles().size()>0){
            binding.btnFile.setBackgroundResource(R.drawable.small_rounded_primary);
        }else{
            binding.btnFile.setBackgroundResource(R.color.transparent);

        }

        binding.btnFile.setOnClickListener(v -> {
            if (model.getFiles().size()>0){
                List<String> images = new ArrayList<>();
                for (FileModel model: model.getFiles()){
                    images.add(model.getFile());
                }

                Intent intent = new Intent(this, ImagesActivity.class);
                intent.putExtra("data", (Serializable) images);
                startActivity(intent);
            }
        });
    }


}