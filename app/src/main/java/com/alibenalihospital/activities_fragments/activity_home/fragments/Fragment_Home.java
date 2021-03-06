package com.alibenalihospital.activities_fragments.activity_home.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.alibenalihospital.R;
import com.alibenalihospital.activities_fragments.activity_departments.DepartmentsActivity;
import com.alibenalihospital.activities_fragments.activity_home.HomeActivity;
import com.alibenalihospital.activities_fragments.activity_notification.NotificationActivity;
import com.alibenalihospital.activities_fragments.activity_service_process.ServiceProcessActivity;
import com.alibenalihospital.adapters.SliderAdapter;
import com.alibenalihospital.databinding.FragmentHomeBinding;

import com.alibenalihospital.models.SliderDataModel;
import com.alibenalihospital.models.SliderModel;
import com.alibenalihospital.models.UserModel;
import com.alibenalihospital.preferences.Preferences;
import com.alibenalihospital.remote.Api;
import com.alibenalihospital.tags.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Home extends Fragment {

    private HomeActivity activity;
    private FragmentHomeBinding binding;
    private Preferences preferences;
    private String lang;
    private UserModel userModel;
    private SliderAdapter sliderAdapter;
    private List<SliderModel> sliderModelList;
    private Timer timer;
    private TimerTask timerTask;

    public static Fragment_Home newInstance() {
        return new Fragment_Home();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        initView();

        return binding.getRoot();
    }


    private void initView() {
        sliderModelList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);

        binding.imageNotifications.setOnClickListener(v -> {
            Intent intent = new Intent(activity, NotificationActivity.class);
            startActivity(intent);
        });
        binding.cardViewServiceProcess.setOnClickListener(v -> {
            Intent intent = new Intent(activity, ServiceProcessActivity.class);
            startActivity(intent);
        });
        binding.cardViewReserveClinic.setOnClickListener(v -> {
            navigateToDepartmentActivity(1);
        });

        binding.cardViewOnlineBooking.setOnClickListener(v -> {
            navigateToDepartmentActivity(2);
        });

        binding.cardViewHomeVisit.setOnClickListener(v -> {
            navigateToDepartmentActivity(3);
        });

        binding.cardViewForeign.setOnClickListener(v -> {
            navigateToDepartmentActivity(4);
        });

        getSlider();
    }

    private void navigateToDepartmentActivity(int type) {
        Intent intent = new Intent(activity, DepartmentsActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }


    private void getSlider() {
        Api.getService(Tags.base_url)
                .getSlider()
                .enqueue(new Callback<SliderDataModel>() {
                    @Override
                    public void onResponse(Call<SliderDataModel> call, Response<SliderDataModel> response) {
                        binding.progBarSlider.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {

                            if (response.body().getData().size() > 0) {
                                updateSliderUi(response.body().getData());

                            } else {

                                binding.flslider.setVisibility(View.GONE);
                                binding.progBarSlider.setVisibility(View.GONE);
                            }

                        } else {

                            binding.flslider.setVisibility(View.GONE);
                            binding.progBarSlider.setVisibility(View.GONE);


                        }


                    }

                    @Override
                    public void onFailure(Call<SliderDataModel> call, Throwable t) {
                        try {
                            Log.e("error", t.getMessage() + "__");
                            binding.flslider.setVisibility(View.GONE);
                            binding.progBarSlider.setVisibility(View.GONE);
                        } catch (Exception e) {

                        }
                    }
                });
    }


    private void updateSliderUi(List<SliderModel> data) {
        sliderModelList.clear();
        sliderModelList.addAll(data);
        sliderAdapter = new SliderAdapter(sliderModelList, activity);
        binding.pager.setAdapter(sliderAdapter);
        binding.pager.setClipToPadding(false);
        binding.pager.setPadding(20, 8, 20, 8);
        binding.pager.setPageMargin(24);
        binding.pager.setOffscreenPageLimit(sliderModelList.size());
        binding.flslider.setVisibility(View.VISIBLE);
        binding.pager.setVisibility(View.VISIBLE);

        if (data.size() > 1) {
            timer = new Timer();
            timerTask = new MyTask();
            timer.scheduleAtFixedRate(timerTask, 6000, 6000);
        }
    }

    public class MyTask extends TimerTask {
        @Override
        public void run() {
            activity.runOnUiThread(() -> {
                int current_page = binding.pager.getCurrentItem();
                if (current_page < sliderAdapter.getCount() - 1) {
                    binding.pager.setCurrentItem(binding.pager.getCurrentItem() + 1);
                } else {
                    binding.pager.setCurrentItem(0);

                }
            });

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timer != null) {
            timer.purge();
            timer.cancel();
        }
        if (timerTask != null) {
            timerTask.cancel();
        }

    }

}
