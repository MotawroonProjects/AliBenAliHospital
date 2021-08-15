package com.alibenalihospital.activities_fragments.activity_complete_reservision;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibenalihospital.R;
import com.alibenalihospital.adapters.DayAdapter;
import com.alibenalihospital.adapters.HourAdapter;
import com.alibenalihospital.adapters.RateAdapter;
import com.alibenalihospital.adapters.SliderAdapter;
import com.alibenalihospital.databinding.ActivityCompleteReservBinding;
import com.alibenalihospital.databinding.ActivityOfferDetialsBinding;
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

public class CompleteReservisionActivityActivity extends AppCompatActivity {
    private ActivityCompleteReservBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;
    private SliderAdapter sliderAdapter;
    private List<SliderModel> sliderModelList;
    private Timer timer;
    private TimerTask timerTask;
    private RateAdapter adapter;
    private List<Object> list;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_complete_reserv);
        initView();
    }


    private void initView() {
        list=new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        sliderModelList = new ArrayList<>();

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        sliderModelList.add(new SliderModel());
        sliderModelList.add(new SliderModel());
        sliderModelList.add(new SliderModel());
        sliderModelList.add(new SliderModel());
        sliderModelList.add(new SliderModel());
        updateSliderUi(sliderModelList);



        binding.llBack.setOnClickListener(view -> finish());

        binding.tvoldprice.setPaintFlags(binding.tvoldprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

    }


    private void updateSliderUi(List<SliderModel> data) {
//        if (data.size() > 0) {
//            sliderModelList.addAll(data);
//            sliderAdapter = new SliderAdapter(sliderModelList, this);
//            binding.pager.setAdapter(sliderAdapter);
//
//            if (data.size() > 1) {
//                timer = new Timer();
//                timerTask = new MyTask();
//                timer.scheduleAtFixedRate(timerTask, 6000, 6000);
//            }
//        } else {
//            binding.pager.setVisibility(View.GONE);
//        }
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