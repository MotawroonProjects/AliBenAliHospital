package com.alibenalihospital.activities_fragments.activity_departments;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibenalihospital.R;
import com.alibenalihospital.activities_fragments.activity_map.MapActivity;
import com.alibenalihospital.activities_fragments.activity_reserve_clinic.ReserveClinicActivity;
import com.alibenalihospital.activities_fragments.activity_reserve_foreign_doctor.ReserveForeignDoctorActivity;
import com.alibenalihospital.activities_fragments.activity_reserve_online.ReserveOnlineActivity;
import com.alibenalihospital.adapters.Dept1Adapter;
import com.alibenalihospital.adapters.NotificationAdapter;
import com.alibenalihospital.databinding.ActivityDepartmentsBinding;
import com.alibenalihospital.databinding.ActivityNotificationBinding;
import com.alibenalihospital.language.Language;
import com.alibenalihospital.models.NotificationModel;
import com.alibenalihospital.models.UserModel;
import com.alibenalihospital.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class DepartmentsActivity extends AppCompatActivity {
    private ActivityDepartmentsBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;
    private int type;
    private String title ="";
    private Dept1Adapter adapter;
    private List<Object> list;


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

        switch (type){
            case 2:
                title = getString(R.string.online_booking);

                break;
            case 3:
                title = getString(R.string.home_visit);

                break;
            case 4:
                title = getString(R.string.foreign_consultant);

                break;

            default:
                title = getString(R.string.reserve_clinic);
                break;

        }
    }


    private void initView() {
        list = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setTitle(title);

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(new GridLayoutManager(this,3));
        adapter = new Dept1Adapter(list, this);
        binding.recView.setAdapter(adapter);
       // binding.swipeRefresh.setOnRefreshListener(this::getNotifications);


        binding.llBack.setOnClickListener(view -> finish());
    }


    public void setItemData(Object o) {
        Intent intent = null;
        if (type==1){
            intent = new Intent(this, ReserveClinicActivity.class);

        }else if (type==2){
            intent = new Intent(this, ReserveOnlineActivity.class);

        }else if (type==3){
            intent = new Intent(this, MapActivity.class);

        }else if (type==4){
            intent = new Intent(this, ReserveForeignDoctorActivity.class);

        }

        startActivity(intent);


    }
}
