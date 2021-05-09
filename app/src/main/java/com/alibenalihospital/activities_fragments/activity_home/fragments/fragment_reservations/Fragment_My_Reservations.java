package com.alibenalihospital.activities_fragments.activity_home.fragments.fragment_reservations;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;


import com.alibenalihospital.adapters.MyPagerAdapter;
import com.alibenalihospital.databinding.FragmentMyReservationBinding;
import com.alibenalihospital.R;

import com.alibenalihospital.activities_fragments.activity_home.HomeActivity;

import com.alibenalihospital.models.UserModel;
import com.alibenalihospital.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class Fragment_My_Reservations extends Fragment  {

    private HomeActivity activity;
    private FragmentMyReservationBinding binding;
    private Preferences preferences;
    private String lang;
    private List<String> titles;
    private List<Fragment> fragments;
    private MyPagerAdapter adapter;

    public static Fragment_My_Reservations newInstance() {

        return new Fragment_My_Reservations();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_reservation, container, false);
        initView();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void initView() {
        titles = new ArrayList<>();
        fragments = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        titles.add(getString(R.string.my_appointment));
        titles.add(getString(R.string.ser_op));
        fragments.add(Fragment_My_Appointment.newInstance());
        fragments.add(Fragment_Service_Operation.newInstance());
        binding.tab.setupWithViewPager(binding.pager);
        adapter = new MyPagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,titles,fragments);
        binding.pager.setAdapter(adapter);
        binding.pager.setOffscreenPageLimit(fragments.size());


    }



}
