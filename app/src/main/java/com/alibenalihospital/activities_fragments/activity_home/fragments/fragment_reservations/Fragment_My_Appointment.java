package com.alibenalihospital.activities_fragments.activity_home.fragments.fragment_reservations;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.alibenalihospital.R;
import com.alibenalihospital.activities_fragments.activity_home.HomeActivity;
import com.alibenalihospital.adapters.MyPagerAdapter;
import com.alibenalihospital.databinding.FragmentMyDatesBinding;
import com.alibenalihospital.databinding.FragmentMyReservationBinding;
import com.alibenalihospital.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class Fragment_My_Appointment extends Fragment  {

    private HomeActivity activity;
    private FragmentMyDatesBinding binding;
    private Preferences preferences;
    private String lang;

    public static Fragment_My_Appointment newInstance() {

        return new Fragment_My_Appointment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_dates, container, false);
        initView();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);



    }



}
