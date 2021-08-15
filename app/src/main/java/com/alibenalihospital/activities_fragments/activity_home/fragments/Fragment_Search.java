package com.alibenalihospital.activities_fragments.activity_home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.alibenalihospital.R;

import com.alibenalihospital.activities_fragments.activity_doctor_details.DoctorDetailsActivity;
import com.alibenalihospital.activities_fragments.activity_home.HomeActivity;

import com.alibenalihospital.adapters.SearchDoctorAdapter;
import com.alibenalihospital.databinding.FragmentSearchBinding;
import com.alibenalihospital.interfaces.Listeners;
import com.alibenalihospital.models.DoctorModel;
import com.alibenalihospital.models.DoctorsDataModel;
import com.alibenalihospital.models.OfferDataModel;
import com.alibenalihospital.models.UserModel;
import com.alibenalihospital.preferences.Preferences;
import com.alibenalihospital.remote.Api;
import com.alibenalihospital.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Search extends Fragment implements Listeners.DoctorListener {

    private HomeActivity activity;
    private FragmentSearchBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    private List<DoctorModel> list;
    private SearchDoctorAdapter adapter;
    private Call<DoctorsDataModel> call;


    public static Fragment_Search newInstance() {
        return new Fragment_Search();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        initView();
        return binding.getRoot();
    }


    private void initView() {

        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        list = new ArrayList<>();
        binding.setLang(lang);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new SearchDoctorAdapter(list, activity, this);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
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
        getData("all");
    }

    private void getData(String query) {
        list.clear();
        adapter.notifyDataSetChanged();
        binding.tvNoData.setVisibility(View.GONE);
        binding.progBar.setVisibility(View.VISIBLE);
        String user_id = null;
        if (userModel != null) {
            user_id = String.valueOf(userModel.getUser().getId());
        }
        if (call!=null){
            call.cancel();
        }
        call = Api.getService(Tags.base_url).searchDoctors(lang, query, user_id);
        call.enqueue(new Callback<DoctorsDataModel>() {
            @Override
            public void onResponse(Call<DoctorsDataModel> call, Response<DoctorsDataModel> response) {
                binding.progBar.setVisibility(View.GONE);
                Log.e("ff", "fff");
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
        Intent intent = new Intent(activity, DoctorDetailsActivity.class);
        intent.putExtra("data", model.getId()+"");
        startActivity(intent);
    }
}
