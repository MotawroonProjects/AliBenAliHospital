package com.alibenalihospital.activities_fragments.activity_doctor_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.alibenalihospital.BuildConfig;
import com.alibenalihospital.R;
import com.alibenalihospital.adapters.NotificationAdapter;
import com.alibenalihospital.adapters.RateAdapter;
import com.alibenalihospital.adapters.SingleDoctorModel;
import com.alibenalihospital.databinding.ActivityDoctorDetailsBinding;
import com.alibenalihospital.databinding.ActivityNotificationBinding;
import com.alibenalihospital.language.Language;
import com.alibenalihospital.models.DoctorModel;
import com.alibenalihospital.models.DoctorsDataModel;
import com.alibenalihospital.models.NotificationModel;
import com.alibenalihospital.models.RateModel;
import com.alibenalihospital.models.StatusResponse;
import com.alibenalihospital.models.UserModel;
import com.alibenalihospital.preferences.Preferences;
import com.alibenalihospital.remote.Api;
import com.alibenalihospital.tags.Tags;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorDetailsActivity extends AppCompatActivity {
    private ActivityDoctorDetailsBinding binding;
    private String lang;
    private RateAdapter adapter;
    private List<RateModel> list;
    private Preferences preferences;
    private UserModel userModel;
    private String doctor_id = "";
    private DoctorModel model;
    private boolean isFavChanged = false;



    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_details);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("data")){
            doctor_id = intent.getStringExtra("data");

        }else {
            doctor_id = intent.getData().getLastPathSegment();

        }
    }


    private void initView() {
        list = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setModel(model);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.recView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.llBack.setOnClickListener(view -> onBackPressed());

        binding.checkbox.setOnClickListener(v -> {
            favorite(binding.checkbox.isChecked());
        });

        binding.imageShare.setOnClickListener(v -> {
            share();
        });

        getDoctorById();
    }

    private void share() {
        Picasso.get().load(Uri.parse(Tags.IMAGE_URL+model.getImage())).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                Intent intent = new Intent();
                String data = model.getName()+"\n"+model.getCategory()+"\n"+Tags.base_url+"details/" + model.getId();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_TEXT,data);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                if (bitmap!=null){

                    File file = new File(getExternalCacheDir(),"share.png");
                    try {
                        FileOutputStream fos = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
                        fos.flush();
                        fos.close();
                        Uri myImageFileUri = FileProvider.getUriForFile(DoctorDetailsActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);

                        intent.putExtra(Intent.EXTRA_STREAM, myImageFileUri);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }


                startActivity(intent);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Intent intent = new Intent();
                String data = model.getName()+"\n"+model.getCategory()+"\n"+Tags.base_url+"details/" + model.getId();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_TEXT,data);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    private void favorite(boolean checked) {
        String user_id;
        if (userModel==null){
            binding.checkbox.setChecked(!checked);
            return;
        }else {
            user_id = userModel.getUser().getId()+"";
        }

        isFavChanged = true;

        Api.getService(Tags.base_url).fav_un_fav(lang,user_id, doctor_id)
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        binding.progBar.setVisibility(View.GONE);

                        if (response.isSuccessful()) {

                            if (response.body() != null&& response.body().getStatus() == 200) {
                               if (checked){
                                   model.setIs_favourate("yes");
                               }else{
                                   model.setIs_favourate("no");

                               }

                               binding.setModel(model);
                            }


                        } else {
                            binding.checkbox.setChecked(!checked);
                            if (checked){
                                model.setIs_favourate("no");
                            }else{
                                model.setIs_favourate("yes");

                            }

                            binding.setModel(model);

                            try {
                                Log.e("error_code", response.code() + "_"+response.errorBody().string());
                            } catch (NullPointerException e) {

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        try {
                            binding.checkbox.setChecked(!checked);
                            if (checked){
                                model.setIs_favourate("no");
                            }else{
                                model.setIs_favourate("yes");

                            }

                            binding.setModel(model);
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

    private void getDoctorById() {
        String user_id = null;
        if (userModel!=null){
            user_id = userModel.getUser().getId()+"";
        }
        Api.getService(Tags.base_url).doctorById(lang,user_id, doctor_id)
                .enqueue(new Callback<SingleDoctorModel>() {
                    @Override
                    public void onResponse(Call<SingleDoctorModel> call, Response<SingleDoctorModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getData() != null && response.body().getStatus() == 200) {
                               model = response.body().getData();
                               binding.setModel(model);
                               updateUi();
                            }


                        } else {


                            try {
                                Log.e("error_code", response.code() + "_"+response.errorBody().string());
                            } catch (NullPointerException e) {

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<SingleDoctorModel> call, Throwable t) {
                        try {
                            binding.progBar.setVisibility(View.GONE);

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

    private void updateUi() {
        list.clear();
        list.addAll(model.getRates());
        adapter = new RateAdapter(list, this);
        binding.recView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (isFavChanged){
            setResult(RESULT_OK);
        }
        finish();
    }
}