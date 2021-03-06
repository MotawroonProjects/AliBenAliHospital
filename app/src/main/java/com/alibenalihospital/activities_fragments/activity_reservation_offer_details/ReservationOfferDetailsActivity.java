package com.alibenalihospital.activities_fragments.activity_reservation_offer_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.alibenalihospital.R;
import com.alibenalihospital.activities_fragments.activity_images.ImagesActivity;
import com.alibenalihospital.databinding.ActivityReservationDetailsBinding;
import com.alibenalihospital.databinding.ActivityReservationOfferDetailsBinding;
import com.alibenalihospital.language.Language;
import com.alibenalihospital.models.FileModel;
import com.alibenalihospital.models.ReservationModel;
import com.alibenalihospital.models.ReservationOfferModel;
import com.alibenalihospital.models.SingleReservationModel;
import com.alibenalihospital.models.SingleReservationOfferModel;
import com.alibenalihospital.models.UserModel;
import com.alibenalihospital.preferences.Preferences;
import com.alibenalihospital.remote.Api;
import com.alibenalihospital.tags.Tags;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationOfferDetailsActivity extends AppCompatActivity {
    private ActivityReservationOfferDetailsBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang = "ar";
    private ReservationOfferModel model;
    private String reservation_id;
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reservation_offer_details);
        getDataFromIntent();
        initView();

    }
    private void getDataFromIntent() {
        Intent intent = getIntent();
        reservation_id =  intent.getStringExtra("data");
    }

    private void initView() {
        Paper.init(this);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.llBack.setOnClickListener(view -> finish());
        binding.tvOldPrice.setPaintFlags(binding.tvOldPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);



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

        getReservation();
    }

    private void getReservation() {
        Api.getService(Tags.base_url)
                .getReservationOfferById(lang, reservation_id)
                .enqueue(new Callback<SingleReservationOfferModel>() {
                    @Override
                    public void onResponse(Call<SingleReservationOfferModel> call, Response<SingleReservationOfferModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                model = response.body().getData();
                                binding.setModel(model);
                                updateUi();
                            }
                        } else {
                            binding.progBar.setVisibility(View.GONE);

                            if (response.code() == 500) {


                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SingleReservationOfferModel> call, Throwable t) {
                        try {


                        } catch (Exception e) {
                        }
                    }
                });
    }

    private void updateUi() {
        if (model.getFiles().size()>0){
            binding.btnFile.setBackgroundResource(R.drawable.small_rounded_primary);
        }else{
            binding.btnFile.setBackgroundResource(R.color.transparent);

        }
    }
}