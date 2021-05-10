package com.alibenalihospital.activities_fragments.activity_service_process;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibenalihospital.R;
import com.alibenalihospital.activities_fragments.activity_home.fragments.Fragment_Home;
import com.alibenalihospital.adapters.Dept1Adapter;
import com.alibenalihospital.adapters.Dept2Adapter;
import com.alibenalihospital.adapters.OfferAdapter;
import com.alibenalihospital.adapters.SliderAdapter;
import com.alibenalihospital.databinding.ActivityFavoriteBinding;
import com.alibenalihospital.databinding.ActivityServiceProcessBinding;
import com.alibenalihospital.language.Language;
import com.alibenalihospital.models.SliderModel;
import com.alibenalihospital.models.UserModel;
import com.alibenalihospital.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;

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
    private List<Object> list;

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
        list = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        sliderModelList = new ArrayList<>();

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.progBar.setVisibility(View.GONE);
        binding.progBarSlider.setVisibility(View.GONE);
        binding.progBarType.setVisibility(View.GONE);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recViewOffers.setLayoutManager(new LinearLayoutManager(this));
        binding.recViewOffers.setAdapter(new OfferAdapter(this));
        binding.recViewSubDepartment.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, true));
        adapter = new Dept2Adapter(list, this);
        binding.recViewSubDepartment.setAdapter(adapter);
        sliderModelList.add(new SliderModel());
        sliderModelList.add(new SliderModel());
        sliderModelList.add(new SliderModel());
        sliderModelList.add(new SliderModel());
        sliderModelList.add(new SliderModel());
        updateSliderUi(sliderModelList);
        binding.pager.setClipToPadding(false);
        binding.pager.setPageMargin(15);
        binding.pager.setPadding(20, 2, 20, 0);
        binding.llBack.setOnClickListener(view -> finish());
    }

    private void updateSliderUi(List<SliderModel> data) {
        if (data.size() > 0) {
            sliderModelList.addAll(data);
            sliderAdapter = new SliderAdapter(sliderModelList, this);
            binding.pager.setAdapter(sliderAdapter);

            if (data.size() > 1) {
                timer = new Timer();
                timerTask = new MyTask();
                timer.scheduleAtFixedRate(timerTask, 6000, 6000);
            }
        } else {
            binding.pager.setVisibility(View.GONE);
        }
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


}