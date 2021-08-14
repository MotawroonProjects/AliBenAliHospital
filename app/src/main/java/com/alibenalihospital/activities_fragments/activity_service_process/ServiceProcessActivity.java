package com.alibenalihospital.activities_fragments.activity_service_process;

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
import com.alibenalihospital.activities_fragments.activity_departments.DepartmentsActivity;
import com.alibenalihospital.activities_fragments.activity_offer_detials.OfferDetialsActivity;
import com.alibenalihospital.activities_fragments.activity_offers.OffersActivity;
import com.alibenalihospital.adapters.Dept2Adapter;
import com.alibenalihospital.adapters.OfferAdapter;
import com.alibenalihospital.adapters.SliderAdapter;
import com.alibenalihospital.databinding.ActivityServiceProcessBinding;
import com.alibenalihospital.language.Language;
import com.alibenalihospital.models.AllDepartmentModel;
import com.alibenalihospital.models.AllOfferModel;
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

public class ServiceProcessActivity extends AppCompatActivity {
    private ActivityServiceProcessBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;

    private SliderAdapter sliderAdapter;
    private List<SliderModel> sliderModelList;
    private Timer timer;
    private TimerTask timerTask;
    private Dept2Adapter adapter;
    private List<AllDepartmentModel.DepartmentData> departmentDataList;
    private List<AllOfferModel.OfferData> offerDataList;
    private OfferAdapter offerAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_service_process);
        initView();
    }


    private void initView() {
        departmentDataList = new ArrayList<>();
        offerDataList=new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        sliderModelList = new ArrayList<>();

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        offerAdapter=new OfferAdapter(offerDataList,this);

        binding.progBar.setVisibility(View.GONE);
        binding.progBarSlider.setVisibility(View.GONE);
        binding.progBarType.setVisibility(View.GONE);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recViewOffers.setLayoutManager(new LinearLayoutManager(this));
        binding.recViewOffers.setAdapter(offerAdapter);
        binding.recViewSubDepartment.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, true));
        adapter = new Dept2Adapter(departmentDataList, this);
        binding.recViewSubDepartment.setAdapter(adapter);
//        sliderModelList.add(new SliderModel());
//        sliderModelList.add(new SliderModel());
//        sliderModelList.add(new SliderModel());
//        sliderModelList.add(new SliderModel());
//        sliderModelList.add(new SliderModel());
//        updateSliderUi(sliderModelList);
//        binding.pager.setClipToPadding(false);
//        binding.pager.setPageMargin(15);
//        binding.pager.setPadding(20, 2, 20, 0);
        binding.llBack.setOnClickListener(view -> finish());
        binding.tvshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceProcessActivity.this, DepartmentsActivity.class);
                intent.putExtra("type", 5);
                startActivity(intent);
            }
        });
        getDepartments();
        getOffers();
    }

//    private void updateSliderUi(List<SliderModel> data) {
//        if (data.size() > 0) {
//            sliderModelList.addAll(data);
//            sliderAdapter = new SliderAdapter(sliderModelList, this);
//            binding.pager.setAdapter(sliderAdapter);
//
//            if (data.size() > 1) {
//                timer = new Timer();
//                timerTask = new MyTask();
//                timer.scheduleAtFixedRate(timerTask, 6000, 6000);
//            }
//        } else {
//            binding.pager.setVisibility(View.GONE);
//        }
//    }

    public void show() {
        Intent intent = new Intent(ServiceProcessActivity.this, OfferDetialsActivity.class);
        startActivity(intent);
    }

    public void setItemData(Object o) {
        Intent intent = new Intent(ServiceProcessActivity.this, OffersActivity.class);
        startActivity(intent);
    }

    public class MyTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(() -> {
                int current_page = binding.pager.getCurrentItem();
                if (current_page < sliderAdapter.getCount() - 1) {
                    binding.pager.setCurrentItem(binding.pager.getCurrentItem() + 1);
                } else {
                    binding.pager.setCurrentItem(0);

                }
            });

        }
    }
    private void getOffers() {
        binding.tvNoData.setVisibility(View.GONE);

        binding.progBar.setVisibility(View.VISIBLE);
        Api.getService(Tags.base_url)
                .getOffers(lang)
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
    private void getDepartments() {

        binding.progBarType.setVisibility(View.VISIBLE);
        Api.getService(Tags.base_url)
                .getDepartments(lang)
                .enqueue(new Callback<AllDepartmentModel>() {
                    @Override
                    public void onResponse(Call<AllDepartmentModel> call, Response<AllDepartmentModel> response) {
                        binding.progBarType.setVisibility(View.GONE);
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getStatus() == 200) {
                                if (response.body().getData() != null) {
                                    if (response.body().getData().size() > 0) {
                                        departmentDataList.addAll(response.body().getData());
                                        adapter.notifyDataSetChanged();
                                    } else {
                                      //  binding.tvNoData.setVisibility(View.VISIBLE);

                                    }
                                }
                            } else {
                               // binding.tvNoData.setVisibility(View.VISIBLE);

                                //  Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }


                        } else {

                           // binding.tvNoData.setVisibility(View.VISIBLE);

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
                    public void onFailure(Call<AllDepartmentModel> call, Throwable t) {
                        try {
                            binding.progBarType.setVisibility(View.GONE);
                           // binding.tvNoData.setVisibility(View.VISIBLE);
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


}