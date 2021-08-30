package com.alibenalihospital.activities_fragments.activity_offer_detials;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibenalihospital.R;
import com.alibenalihospital.activities_fragments.activity_create_offer_reservation.CreateOfferReservationActivity;
import com.alibenalihospital.adapters.DayAdapter;
import com.alibenalihospital.adapters.HourAdapter;
import com.alibenalihospital.adapters.RateAdapter;
import com.alibenalihospital.adapters.SliderAdapter;
import com.alibenalihospital.databinding.ActivityOfferDetialsBinding;
import com.alibenalihospital.interfaces.Listeners;
import com.alibenalihospital.language.Language;
import com.alibenalihospital.models.AddOfferReservationModel;
import com.alibenalihospital.models.DateModel;
import com.alibenalihospital.models.HourModel;
import com.alibenalihospital.models.OfferDataModel;
import com.alibenalihospital.models.RateModel;
import com.alibenalihospital.models.ReservationOfferModel;
import com.alibenalihospital.models.SliderModel;
import com.alibenalihospital.models.StatusResponse;
import com.alibenalihospital.models.UserModel;
import com.alibenalihospital.preferences.Preferences;
import com.alibenalihospital.remote.Api;
import com.alibenalihospital.share.Common;
import com.alibenalihospital.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferDetialsActivity extends AppCompatActivity implements Listeners.DateListener, Listeners.HourListener{
    private ActivityOfferDetialsBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;
    private SliderAdapter sliderAdapter;
    private List<SliderModel> sliderModelList;
    private Timer timer;
    private TimerTask timerTask;
    private RateAdapter adapter;
    private List<RateModel> rateModelList;
    private String offerid;
    private Animation animation;
    private List<DateModel> dateModelList;
    private DayAdapter dayAdapter;
    private DateModel selectedDate;
    private HourModel selectedHourModel;
    private OfferDataModel.OfferData offerModel;
    private ActivityResultLauncher<Intent> launcher;
    private int req ;
    private ReservationOfferModel reservationOfferModel;

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
            if (intent.hasExtra("reservation")){
                reservationOfferModel = (ReservationOfferModel) intent.getSerializableExtra("reservation");
                selectedHourModel = reservationOfferModel.getHour();
                selectedDate = reservationOfferModel.getDate();
            }
            offerid = intent.getStringExtra("offerid");


        }

    }

    private void initView() {
        dateModelList = new ArrayList<>();
        rateModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        sliderModelList = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);

        binding.tvOldPrice.setPaintFlags(binding.tvOldPrice.getPaintFlags()|Paint.STRIKE_THRU_TEXT_FLAG);
        binding.llBack.setOnClickListener(view -> finish());


        binding.btnConfirm.setOnClickListener(v -> {
            openSheet();
        });

        binding.imageCloseSheet.setOnClickListener(v -> {
            closeSheet();
        });

        binding.btnReserve.setOnClickListener(v -> {
            userModel = preferences.getUserData(this);
            if (userModel==null){
                Toast.makeText(this, R.string.log_sign_up, Toast.LENGTH_SHORT).show();
                return;
            }

            if (reservationOfferModel==null){
                req = 2;

                step2();
            }else {
                updateDate();
            }


        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req ==1&&result.getResultCode()==RESULT_OK){
                step2();
            }else if (req ==2&&result.getResultCode()==RESULT_OK){
                Toast.makeText(this, getString(R.string.suc), Toast.LENGTH_SHORT).show();
            }
        });

        getSingleOffer();

    }

    private void step2() {
        closeSheet();
        AddOfferReservationModel addOfferReservationModel = new AddOfferReservationModel(offerModel,selectedDate,selectedHourModel,userModel.getUser().getName(),userModel.getUser().getPhone_code()+userModel.getUser().getPhone());
        Intent intent = new Intent(this, CreateOfferReservationActivity.class);
        intent.putExtra("data", addOfferReservationModel);
        launcher.launch(intent);
    }

    private void getSingleOffer() {
        String userid = null;
        String reservation_id = null;
        if (userModel != null) {
            userid = userModel.getUser().getId() + "";
        }
        if (reservationOfferModel!=null){
            reservation_id = reservationOfferModel.getId()+"";
        }

        Log.e("res_id", reservation_id+"__");

        Api.getService(Tags.base_url)
                .getSingleOffer(lang, offerid, userid,reservation_id)
                .enqueue(new Callback<OfferDataModel>() {
                    @Override
                    public void onResponse(Call<OfferDataModel> call, Response<OfferDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getStatus() == 200) {
                                if (response.body().getData() != null) {
                                    updateData(response.body().getData());
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
    private void updateData(OfferDataModel.OfferData data) {
        offerModel = data;
        binding.setModel(offerModel);
        binding.setModel(data);
        updateSliderUi(data.getImages());
        if (data.getRates() != null && data.getRates().size() > 0) {


            binding.tvNoData.setVisibility(View.GONE);

            rateModelList.addAll(data.getRates());
            binding.recView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
            adapter = new RateAdapter(rateModelList, this);
            binding.recView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            binding.tvNoData.setVisibility(View.VISIBLE);
        }




//        Log.e("sss", data.getAll_dates().size()+"__");
        binding.recViewDay.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        if (reservationOfferModel==null){
            dateModelList.addAll(data.getAvailable_date());

        }else {
            dateModelList.addAll(data.getAll_dates());
        }

        dayAdapter = new DayAdapter(dateModelList,this,this);
        binding.recViewDay.setAdapter(dayAdapter);
        binding.recViewHour.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));


        if (reservationOfferModel != null) {
            int datePos = findDatePos();
            if (datePos != -1) {
                dayAdapter.updatePos(datePos);

                DateModel dateModel = dateModelList.get(datePos);
                dateModel.setSelected(true);
                dateModelList.set(datePos, dateModel);
                dayAdapter.notifyItemChanged(datePos);

                HourAdapter hourAdapter = new HourAdapter(this, dateModel.getAvailable_hour(), this);

                int hourPos = findHourPos(datePos);
                if (hourPos != -1) {
                    hourAdapter.updatePos(hourPos);

                }

                binding.recViewHour.setAdapter(hourAdapter);
                binding.expandHour.setExpanded(true);
            }
        }

    }

    private int findDatePos() {
        int pos = -1;
        for (int index = 0; index < dateModelList.size(); index++) {
            DateModel dateModel = dateModelList.get(index);
            if (dateModel.getIs_reserved().equals("yes")) {
                pos = index;
                return pos;
            }
        }
        return pos;
    }

    private int findHourPos(int datePos) {
        int pos = -1;
        for (int index = 0; index < dateModelList.get(datePos).getAvailable_hour().size(); index++) {
            HourModel hourModel = dateModelList.get(datePos).getAvailable_hour().get(index);

            if (hourModel.getIs_reserved().equals("yes")) {
                pos = index;
                return pos;
            }
        }
        return pos;
    }

    private void updateDate() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));

        dialog.setCancelable(false);
        dialog.show();

        Api.getService(Tags.base_url)
                .updateReserveDoctor(lang, reservationOfferModel.getId() + "", selectedDate.getId() + "", selectedHourModel.getId() , reservationOfferModel.getName(), reservationOfferModel.getPhone(), reservationOfferModel.getCall_type())
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                setResult(RESULT_OK);
                                finish();
                            }

                        } else {

                            try {
                                Log.e("error", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {

                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    private void openSheet(){
        binding.flSheet.clearAnimation();
        animation = AnimationUtils.loadAnimation(this,R.anim.slide_up);
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

    private void closeSheet(){
        binding.flSheet.clearAnimation();
        animation = AnimationUtils.loadAnimation(this,R.anim.slide_down);
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



    @Override
    public void setDate(DateModel dateModel, int adapterPosition) {
        this.selectedDate = dateModel;

        HourAdapter hourAdapter = new HourAdapter(this,dateModel.getAvailable_hour(),this);
        binding.recViewHour.setAdapter(hourAdapter);
        binding.expandHour.setExpanded(true);
    }

    @Override
    public void setHour(HourModel hourModel) {
        this.selectedHourModel  =hourModel;
        binding.btnReserve.setVisibility(View.VISIBLE);


    }


    @Override
    public void onBackPressed() {
        if (binding.flSheet.getVisibility()==View.VISIBLE){
            closeSheet();
        }else {
            finish();
        }

    }
}