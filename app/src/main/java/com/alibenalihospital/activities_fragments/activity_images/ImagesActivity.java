package com.alibenalihospital.activities_fragments.activity_images;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;

import com.alibenalihospital.R;
import com.alibenalihospital.adapters.FavoriteDoctorAdapter;
import com.alibenalihospital.adapters.ImagesAdapter;
import com.alibenalihospital.databinding.ActivityFavoriteBinding;
import com.alibenalihospital.databinding.ActivityImagesBinding;
import com.alibenalihospital.language.Language;
import com.alibenalihospital.models.DoctorModel;
import com.alibenalihospital.models.UserModel;
import com.alibenalihospital.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class ImagesActivity extends AppCompatActivity {

    private ActivityImagesBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;
    private List<String> list;
    private ImagesAdapter adapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_images);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        list = new ArrayList<>();
        Intent intent = getIntent();
        list = intent.getStringArrayListExtra("data");
    }


    private void initView() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));

        }
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);

        adapter = new ImagesAdapter(list,this);
        binding.pager.setAdapter(adapter);
        binding.pager.setOffscreenPageLimit(list.size());

        binding.llBack.setOnClickListener(view -> finish());


    }
}