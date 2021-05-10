package com.alibenalihospital.activities_fragments.activity_reserve_foreign_doctor;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;

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
import com.alibenalihospital.models.UserModel;
import com.alibenalihospital.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class ReserveForeignDoctorActivity extends AppCompatActivity {
    private ActivityReserveForeignBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;
    private ForeignDoctorAdapter adapter;
    private List<Object> list;



    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reserve_foreign);
        initView();
    }



    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        list = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ForeignDoctorAdapter(list,this);
        binding.recView.setAdapter(adapter);
       // binding.swipeRefresh.setOnRefreshListener(this::getNotifications);


        binding.llBack.setOnClickListener(view -> finish());
    }

    public void setItemData(Object o) {
        Intent intent = new Intent(this, DoctorDetailsActivity.class);
        startActivity(intent);
    }

}
