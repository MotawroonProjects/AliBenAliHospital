package com.alibenalihospital.activities_fragments.activity_departments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibenalihospital.R;
import com.alibenalihospital.activities_fragments.activity_map.MapActivity;
import com.alibenalihospital.activities_fragments.activity_offers.OffersActivity;
import com.alibenalihospital.activities_fragments.activity_reserve_clinic.ReserveClinicActivity;
import com.alibenalihospital.activities_fragments.activity_reserve_foreign_doctor.ReserveForeignDoctorActivity;
import com.alibenalihospital.activities_fragments.activity_reserve_online.ReserveOnlineActivity;
import com.alibenalihospital.activities_fragments.activity_service_process.ServiceProcessActivity;
import com.alibenalihospital.adapters.Dept1Adapter;
import com.alibenalihospital.adapters.NotificationAdapter;
import com.alibenalihospital.databinding.ActivityDepartmentsBinding;
import com.alibenalihospital.databinding.ActivityNotificationBinding;
import com.alibenalihospital.language.Language;
import com.alibenalihospital.models.AllDepartmentModel;
import com.alibenalihospital.models.NotificationModel;
import com.alibenalihospital.models.UserModel;
import com.alibenalihospital.preferences.Preferences;
import com.alibenalihospital.remote.Api;
import com.alibenalihospital.tags.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DepartmentsActivity extends AppCompatActivity {
    private ActivityDepartmentsBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;
    private int type;
    private String title = "";
    private Dept1Adapter adapter;
    private List<AllDepartmentModel.DepartmentData> departmentDataList;
    private String query = "all";

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_departments);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 1);

        switch (type) {
            case 2:
                title = getString(R.string.online_booking);

                break;
            case 3:
            case 5:
                title = getString(R.string.departments);

                break;
            case 4:
                title = getString(R.string.doctors);
                break;

            default:
                title = getString(R.string.reserve_clinic);
                break;

        }
    }


    private void initView() {
        departmentDataList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setTitle(title);

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new Dept1Adapter(departmentDataList, this);
        binding.recView.setAdapter(adapter);

        getDepartments(query);
        binding.llBack.setOnClickListener(view -> finish());
        // binding.swipeRefresh.setOnRefreshListener(this::getDepartments(query));
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    getDepartments("all");
                } else {
                    getDepartments(s.toString());
                }
            }
        });
    }

    public void setItemData(AllDepartmentModel.DepartmentData departmentData) {
        Intent intent = null;
        if (type == 1) {
            intent = new Intent(this, ReserveClinicActivity.class);

        } else if (type == 2) {
            intent = new Intent(this, ReserveOnlineActivity.class);

        } else if (type == 3) {
            intent = new Intent(this, MapActivity.class);

        } else if (type == 4) {
            intent = new Intent(this, ReserveForeignDoctorActivity.class);

        } else if (type == 5) {
            intent = new Intent(this, OffersActivity.class);

        }

        intent.putExtra("id", departmentData.id);
        startActivity(intent);
    }


    private void getDepartments(String query) {
        departmentDataList.clear();
        adapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);
        Api.getService(Tags.base_url)
                .searchDepartments(lang, query)
                .enqueue(new Callback<AllDepartmentModel>() {
                    @Override
                    public void onResponse(Call<AllDepartmentModel> call, Response<AllDepartmentModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getStatus() == 200) {
                                if (response.body().getData() != null) {
                                    if (response.body().getData().size() > 0) {
                                        departmentDataList.addAll(response.body().getData());
                                        adapter.notifyDataSetChanged();
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
                    public void onFailure(Call<AllDepartmentModel> call, Throwable t) {
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

}
