package com.alibenalihospital.activities_fragments.activity_offers;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibenalihospital.R;
import com.alibenalihospital.activities_fragments.activity_offer_detials.OfferDetialsActivity;
import com.alibenalihospital.adapters.Dept2Adapter;
import com.alibenalihospital.adapters.OfferAdapter;
import com.alibenalihospital.adapters.SliderAdapter;
import com.alibenalihospital.databinding.ActivityOfferDetialsBinding;
import com.alibenalihospital.databinding.ActivityOffersBinding;
import com.alibenalihospital.databinding.ActivityServiceProcessBinding;
import com.alibenalihospital.language.Language;
import com.alibenalihospital.models.AllOfferModel;
import com.alibenalihospital.models.SliderModel;
import com.alibenalihospital.models.UserModel;
import com.alibenalihospital.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;

public class OffersActivity extends AppCompatActivity {
    private ActivityOffersBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;

    private SliderAdapter sliderAdapter;
    private List<SliderModel> sliderModelList;
    private Timer timer;
    private TimerTask timerTask;
    private Dept2Adapter adapter;
    private List<Object> list;
    private List<AllOfferModel.OfferData> offerDataList;
    private OfferAdapter offerAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_offers);
        initView();
    }


    private void initView() {
        list = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        sliderModelList = new ArrayList<>();
        offerDataList = new ArrayList<>();
        offerAdapter = new OfferAdapter(offerDataList, this);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.progBar.setVisibility(View.GONE);

        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(offerAdapter);

    }


    public void show() {
        Intent intent = new Intent(OffersActivity.this, OfferDetialsActivity.class);
        startActivity(intent);
    }

    public void setItemData(Object o) {
    }


}