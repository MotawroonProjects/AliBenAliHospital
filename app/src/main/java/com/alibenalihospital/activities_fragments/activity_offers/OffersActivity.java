package com.alibenalihospital.activities_fragments.activity_offers;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibenalihospital.R;
import com.alibenalihospital.activities_fragments.activity_offer_detials.OfferDetialsActivity;
import com.alibenalihospital.adapters.Dept2Adapter;
import com.alibenalihospital.adapters.OfferAdapter;
import com.alibenalihospital.adapters.SliderAdapter;
import com.alibenalihospital.databinding.ActivityOfferDetialsBinding;
import com.alibenalihospital.databinding.ActivityOffersBinding;
import com.alibenalihospital.databinding.ActivityServiceProcessBinding;
import com.alibenalihospital.language.Language;
import com.alibenalihospital.models.AllDepartmentModel;
import com.alibenalihospital.models.AllOfferModel;
import com.alibenalihospital.models.OfferDataModel;
import com.alibenalihospital.models.SliderModel;
import com.alibenalihospital.models.UserModel;
import com.alibenalihospital.preferences.Preferences;
import com.alibenalihospital.remote.Api;
import com.alibenalihospital.tags.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OffersActivity extends AppCompatActivity {
    private ActivityOffersBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;

    private SliderAdapter sliderAdapter;
    private List<SliderModel> sliderModelList;
    private Timer timer;
    private TimerTask timerTask;
    private Dept2Adapter adapter;
    private List<Object> list;
    private List<OfferDataModel.OfferData> offerDataList;
    private OfferAdapter offerAdapter;
    private AllDepartmentModel.DepartmentData department;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_offers);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            department = (AllDepartmentModel.DepartmentData) intent.getSerializableExtra("department");


        }
    }

    private void initView() {
        list = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        sliderModelList = new ArrayList<>();
        offerDataList = new ArrayList<>();
        offerAdapter = new OfferAdapter(offerDataList, this);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setTitle(department.getName());
        binding.progBar.setVisibility(View.GONE);

        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(offerAdapter);
        getOffers();
        binding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getOffers() {
        binding.tvNoData.setVisibility(View.GONE);

        binding.progBar.setVisibility(View.VISIBLE);
        Api.getService(Tags.base_url)
                .getOffers(lang, department.getId() + "")
                .enqueue(new Callback<AllOfferModel>() {
                    @Override
                    public void onResponse(Call<AllOfferModel> call, Response<AllOfferModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getStatus() == 200) {
                                if (response.body().getData() != null) {
                                    if (response.body().getData().size() > 0) {
                                        offerDataList.addAll(response.body().getData());
                                        offerAdapter.notifyDataSetChanged();
                                    } else {
                                        binding.tvNoData.setVisibility(View.VISIBLE);

                                    }
                                }
                            } else {
                                binding.tvNoData.setVisibility(View.VISIBLE);

                                //  Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }


                        } else {

                            binding.tvNoData.setVisibility(View.VISIBLE);

                            switch (response.code()) {
                                case 500:
                                    //   Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    //   Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            try {
                                Log.e("error_code", response.code() + "_");
                            } catch (NullPointerException e) {

                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<AllOfferModel> call, Throwable t) {
                        try {
                            binding.progBar.setVisibility(View.GONE);
                            binding.tvNoData.setVisibility(View.VISIBLE);
//                            binding.arrow.setVisibility(View.VISIBLE);
//
//                            binding.progBar.setVisibility(View.GONE);
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


    public void show() {
        Intent intent = new Intent(OffersActivity.this, OfferDetialsActivity.class);
        startActivity(intent);
    }

    public void setItemData(Object o) {
    }


}