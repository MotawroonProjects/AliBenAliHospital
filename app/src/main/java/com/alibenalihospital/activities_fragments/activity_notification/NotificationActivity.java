package com.alibenalihospital.activities_fragments.activity_notification;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.alibenalihospital.R;
import com.alibenalihospital.adapters.NotificationAdapter;
import com.alibenalihospital.databinding.ActivityNotificationBinding;
import com.alibenalihospital.databinding.DialogAlertBinding;
import com.alibenalihospital.databinding.RateDialogBinding;
import com.alibenalihospital.language.Language;

import com.alibenalihospital.models.NotificationDataModel;
import com.alibenalihospital.models.NotificationModel;
import com.alibenalihospital.models.StatusResponse;
import com.alibenalihospital.models.UserModel;
import com.alibenalihospital.preferences.Preferences;
import com.alibenalihospital.remote.Api;
import com.alibenalihospital.share.Common;
import com.alibenalihospital.tags.Tags;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {
    private ActivityNotificationBinding binding;
    private String lang;
    private List<NotificationModel> notificationModelList;
    private NotificationAdapter adapter;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        initView();
    }


    private void initView() {
        notificationModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationAdapter(notificationModelList, this);
        binding.recView.setAdapter(adapter);
        binding.swipeRefresh.setOnRefreshListener(this::getNotifications);


        binding.llBack.setOnClickListener(view -> finish());
        getNotifications();
    }

    private void getNotifications() {
        try {
            binding.progBar.setVisibility(View.VISIBLE);

            if (userModel == null) {
                binding.progBar.setVisibility(View.GONE);
                binding.tvNoData.setVisibility(View.VISIBLE);
                binding.swipeRefresh.setRefreshing(false);

                return;
            }

            notificationModelList.clear();
            adapter.notifyDataSetChanged();

            Api.getService(Tags.base_url)
                    .getNotifications(lang, userModel.getUser().getId() + "")
                    .enqueue(new Callback<NotificationDataModel>() {
                        @Override
                        public void onResponse(Call<NotificationDataModel> call, Response<NotificationDataModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                            binding.swipeRefresh.setRefreshing(false);
                            if (response.isSuccessful() && response.body() != null) {
                                if (response.body().getStatus() == 200) {
                                    if (response.body().getData().size() > 0) {
                                        notificationModelList.addAll(response.body().getData());
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        binding.tvNoData.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    Toast.makeText(NotificationActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                binding.progBar.setVisibility(View.GONE);
                                binding.swipeRefresh.setRefreshing(false);

                                if (response.code() == 500) {
                                    Toast.makeText(NotificationActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(NotificationActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<NotificationDataModel> call, Throwable t) {
                            try {
                                binding.progBar.setVisibility(View.GONE);
                                binding.swipeRefresh.setRefreshing(false);

                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(NotificationActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(NotificationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }

    public void setItemData(NotificationModel model) {

    }

    public void showDialog(NotificationModel model) {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .create();

        RateDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.rate_dialog, null, false);
        binding.setModel(model);
        binding.btnCancel.setOnClickListener(v -> dialog.dismiss());
        binding.btnRate.setOnClickListener(v -> {
            String comment = binding.edtComment.getText().toString();
            float rate = binding.rateBar.getRating();
            Common.CloseKeyBoard(this, binding.edtComment);
            dialog.dismiss();
            if (model.getReservation().getType().equals("online")){
                addRateDoctor(model, comment, rate);

            }else{
                addRateOffer(model, comment, rate);

            }
        });
        binding.rateBar.setOnRatingBarChangeListener((simpleRatingBar, rating, fromUser) -> binding.tvRate.setText(String.valueOf(rating)));
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }



    private void addRateDoctor(NotificationModel model, String comment, float rate) {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));

        dialog.setCancelable(false);
        dialog.show();

        Api.getService(Tags.base_url)
                .addRateDoctor(lang, userModel.getUser().getId() + "", model.getReservation().getDoctor().getId()+"",comment,rate)
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                               getNotifications();
                            }

                        } else {

                            try {
                                Log.e("error", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {

                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    private void addRateOffer(NotificationModel model, String comment, float rate) {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));

        dialog.setCancelable(false);
        dialog.show();

        Api.getService(Tags.base_url)
                .addRateOffer(lang, userModel.getUser().getId() + "", model.getReservation().getOffer().getId()+"",comment,rate)
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                getNotifications();
                            }

                        } else {

                            try {
                                Log.e("error", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {

                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }


}
