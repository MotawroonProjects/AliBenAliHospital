package com.alibenalihospital.activities_fragments.activity_favorite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.alibenalihospital.R;
import com.alibenalihospital.activities_fragments.activity_doctor_details.DoctorDetailsActivity;
import com.alibenalihospital.adapters.FavoriteDoctorAdapter;
import com.alibenalihospital.adapters.NotificationAdapter;
import com.alibenalihospital.adapters.SingleDoctorModel;
import com.alibenalihospital.databinding.ActivityFavoriteBinding;
import com.alibenalihospital.databinding.ActivityNotificationBinding;
import com.alibenalihospital.interfaces.Listeners;
import com.alibenalihospital.language.Language;
import com.alibenalihospital.models.DoctorModel;
import com.alibenalihospital.models.DoctorsDataModel;
import com.alibenalihospital.models.NotificationModel;
import com.alibenalihospital.models.OfferDataModel;
import com.alibenalihospital.models.StatusResponse;
import com.alibenalihospital.models.UserModel;
import com.alibenalihospital.preferences.Preferences;
import com.alibenalihospital.remote.Api;
import com.alibenalihospital.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteActivity extends AppCompatActivity implements Listeners.FavListener {
    private ActivityFavoriteBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;
    private List<DoctorModel> list;
    private FavoriteDoctorAdapter adapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorite);
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
        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FavoriteDoctorAdapter(list, this, this);
        binding.recView.setAdapter(adapter);
        binding.llBack.setOnClickListener(view -> finish());
        getFavorite();
    }

    private void getFavorite() {
        binding.progBar.setVisibility(View.VISIBLE);
        binding.tvNoData.setVisibility(View.GONE);
        if (userModel==null){
            binding.progBar.setVisibility(View.GONE);
            binding.tvNoData.setVisibility(View.VISIBLE);
            return;
        }

        Api.getService(Tags.base_url).getFavorites(lang, userModel.getUser().getId()+"")
                .enqueue(new Callback<DoctorsDataModel>() {
            @Override
            public void onResponse(Call<DoctorsDataModel> call, Response<DoctorsDataModel> response) {
                binding.progBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {

                    if (response.body() != null && response.body().getData() != null && response.body().getStatus() == 200) {
                        if (response.body().getData().size() > 0) {
                            binding.tvNoData.setVisibility(View.GONE);
                            list.addAll(response.body().getData());
                            adapter.notifyDataSetChanged();
                        } else {
                            binding.tvNoData.setVisibility(View.VISIBLE);

                        }
                    }


                } else {


                    try {
                        Log.e("error_code", response.code() + "_");
                    } catch (NullPointerException e) {

                    }
                }


            }

            @Override
            public void onFailure(Call<DoctorsDataModel> call, Throwable t) {
                try {
                    binding.progBar.setVisibility(View.GONE);
                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage());
                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                            //     Toast.makeText(SignUpActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                        } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                        } else {
                            //  Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {

                }
            }
        });

    }


    @Override
    public void setDoctorData(DoctorModel model) {
        Intent intent = new Intent(this, DoctorDetailsActivity.class);
        intent.putExtra("data", model.getId() + "");
        startActivity(intent);
    }

    @Override
    public void unFav(DoctorModel model, int pos) {
        Api.getService(Tags.base_url).fav_un_fav(lang, userModel.getUser().getId() + "", model.getId() + "")
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        binding.progBar.setVisibility(View.GONE);

                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getStatus() == 200) {
                                list.remove(pos);
                                adapter.notifyItemRemoved(pos);
                                if (list.size() == 0) {
                                    binding.tvNoData.setVisibility(View.VISIBLE);
                                }
                            }


                        } else {


                            try {
                                Log.e("error_code", response.code() + "_" + response.errorBody().string());
                            } catch (NullPointerException e) {

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        try {

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    //     Toast.makeText(SignUpActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    //  Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {

                        }
                    }
                });
    }
}