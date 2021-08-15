package com.alibenalihospital.activities_fragments.activity_offer_detials;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibenalihospital.R;
import com.alibenalihospital.activities_fragments.activity_complete_reservision.CompleteReservisionActivityActivity;
import com.alibenalihospital.activities_fragments.activity_service_process.ServiceProcessActivity;
import com.alibenalihospital.adapters.DayAdapter;
import com.alibenalihospital.adapters.HourAdapter;
import com.alibenalihospital.adapters.RateAdapter;
import com.alibenalihospital.adapters.SliderAdapter;
import com.alibenalihospital.databinding.ActivityFavoriteBinding;
import com.alibenalihospital.databinding.ActivityOfferDetialsBinding;
import com.alibenalihospital.language.Language;
import com.alibenalihospital.models.AllDepartmentModel;
import com.alibenalihospital.models.OfferDataModel;
import com.alibenalihospital.models.OfferDataModel;
import com.alibenalihospital.models.SliderModel;
import com.alibenalihospital.models.UserModel;
import com.alibenalihospital.preferences.Preferences;
import com.alibenalihospital.remote.Api;
import com.alibenalihospital.tags.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferDetialsActivity extends AppCompatActivity {
    private ActivityOfferDetialsBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;
    private SliderAdapter sliderAdapter;
    private List<OfferDataModel.OfferData.Images> sliderModelList;
    private Timer timer;
    private TimerTask timerTask;
    private RateAdapter adapter;
    private List<Object> list;
private String offerid;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_offer_detials);
        getDataFromIntent();
        initView();
    }
    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            offerid = intent.getStringExtra("offerid");


        }
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



        binding.llBack.setOnClickListener(view -> finish());
        binding.btnAsk.setOnClickListener(v -> openSheet());
        binding.flSheet.setOnClickListener(v -> closeSheet());
        binding.progBar.setVisibility(View.GONE);
        binding.recViewSpecialization.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recViewSpecialization.setAdapter(new DayAdapter(this));
        binding.recviehour.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recviehour.setAdapter(new HourAdapter(this));
        binding.recViewhourhour.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recViewhourhour.setAdapter(new HourAdapter(this));
        binding.recView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new RateAdapter(list, this);
        binding.recView.setAdapter(adapter);
        binding.progBar.setVisibility(View.GONE);
        binding.progBarSlider.setVisibility(View.GONE);
        binding.tvoldprice.setPaintFlags(binding.tvoldprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        binding.btnresev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OfferDetialsActivity.this, CompleteReservisionActivityActivity.class);
                startActivity(intent);
            }
        });
        getSingleoffer();

    }

    private void openSheet() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        binding.flSheet.clearAnimation();
        binding.flSheet.startAnimation(animation);


        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.flSheet.setVisibility(View.VISIBLE);


            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void closeSheet() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_down);

        binding.flSheet.clearAnimation();
        binding.flSheet.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.flSheet.setVisibility(View.GONE);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void updateSliderUi(List<OfferDataModel.OfferData.Images> data) {
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

    public void setItemData(Object o) {
        binding.expandhour.setExpanded(true);
    }

    public void setItemData() {
        if (!binding.expandhourhour.isExpanded()) {
            binding.expandhourhour.setExpanded(true);
        } else {
            Log.e("ddlldldl", "dlldldl");
            binding.btnresev.setVisibility(View.VISIBLE);
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
    private void getSingleoffer() {
        String userid=null;
        if(userModel!=null){
            userid=userModel.getUser().getId()+"";
        }
       
        Api.getService(Tags.base_url)
                .getSingleOffer(lang, offerid,userid)
                .enqueue(new Callback<OfferDataModel>() {
                    @Override
                    public void onResponse(Call<OfferDataModel> call, Response<OfferDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getStatus() == 200) {
                                if (response.body().getData() != null) {
                                  updatedata(response.body().getData());
                                }
                            } else {

                                //  Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }


                        } else {


                            switch (response.code()) {
                                case 500:
                                    //   Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    //   Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            try {
                                Log.e("error_code", response.code() + "_");
                            } catch (NullPointerException e) {

                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<OfferDataModel> call, Throwable t) {
                        try {
                           
//                            binding.arrow.setVisibility(View.VISIBLE);
//
//                            binding.progBar.setVisibility(View.GONE);
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

    private void updatedata(OfferDataModel.OfferData data) {
        binding.setModel(data);
        updateSliderUi(data.getImages());
        binding.progBarSlider.setVisibility(View.GONE);

    }

}