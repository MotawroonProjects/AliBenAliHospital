package com.alibenalihospital.activities_fragments.activity_reserve_foreign_doctor;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibenalihospital.R;
import com.alibenalihospital.activities_fragments.activity_doctor_details.DoctorDetailsActivity;
import com.alibenalihospital.adapters.ForeignDoctorAdapter;
import com.alibenalihospital.adapters.OnlineDoctorAdapter;
import com.alibenalihospital.databinding.ActivityReserveForeignBinding;
import com.alibenalihospital.databinding.ActivityReserveOnlineBinding;
import com.alibenalihospital.language.Language;
import com.alibenalihospital.models.DoctorModel;
import com.alibenalihospital.models.DoctorsDataModel;
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

public class ReserveForeignDoctorActivity extends AppCompatActivity {
    private ActivityReserveForeignBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;
    private ForeignDoctorAdapter adapter;
    private List<DoctorModel> list;
    private int department_id;
    private Call<DoctorsDataModel> call;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reserve_foreign);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        department_id = intent.getIntExtra("id", 0);
    }

    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        list = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ForeignDoctorAdapter(list,this);
        binding.recView.setAdapter(adapter);
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()){
                    getData("all");
                }else {
                    getData(s.toString());
                }
            }
        });


        binding.llBack.setOnClickListener(view -> finish());
        getData("all");
    }
    private void getData(String query) {
        list.clear();
        adapter.notifyDataSetChanged();
        binding.tvNoData.setVisibility(View.GONE);
        binding.progBar.setVisibility(View.VISIBLE);

        if (call!=null){
            call.cancel();
        }
        call = Api.getService(Tags.base_url).searchDoctorsByDepartment(lang, query, department_id+"");
        call.enqueue(new Callback<DoctorsDataModel>() {
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


    public void setItemData(DoctorModel model) {
        Intent intent = new Intent(this, DoctorDetailsActivity.class);
        intent.putExtra("data", model.getId()+"");
        startActivity(intent);
    }

}
