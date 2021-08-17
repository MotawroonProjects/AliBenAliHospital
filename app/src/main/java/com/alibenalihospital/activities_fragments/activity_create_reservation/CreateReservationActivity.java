package com.alibenalihospital.activities_fragments.activity_create_reservation;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.alibenalihospital.R;
import com.alibenalihospital.adapters.DiseasesAdapter;
import com.alibenalihospital.adapters.FilesAdapter;
import com.alibenalihospital.adapters.SpinnerDiseasesAdapter;
import com.alibenalihospital.databinding.ActivityContactUsBinding;
import com.alibenalihospital.databinding.ActivityCreateReservationBinding;
import com.alibenalihospital.interfaces.Listeners;
import com.alibenalihospital.language.Language;
import com.alibenalihospital.models.AddReservationModel;
import com.alibenalihospital.models.ContactUsModel;
import com.alibenalihospital.models.DiseasesDataModel;
import com.alibenalihospital.models.DiseasesModel;
import com.alibenalihospital.models.ReservationOfferDataModel;
import com.alibenalihospital.models.StatusResponse;
import com.alibenalihospital.models.UserModel;
import com.alibenalihospital.preferences.Preferences;
import com.alibenalihospital.remote.Api;
import com.alibenalihospital.share.Common;
import com.alibenalihospital.tags.Tags;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateReservationActivity extends AppCompatActivity implements Listeners.DeleteDiseaseListener {

    private ActivityCreateReservationBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang = "ar";
    private AddReservationModel model;
    private List<DiseasesModel> list;
    private List<DiseasesModel> selectedDiseasesList;
    private DiseasesAdapter adapter;
    private SpinnerDiseasesAdapter spinnerDiseasesAdapter;
    private List<String> images;
    private FilesAdapter filesAdapter;
    private final String WRITE_PERM = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String CAMERA_PERM = Manifest.permission.CAMERA;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private ActivityResultLauncher<Intent> launcher;


    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_reservation);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        model = (AddReservationModel) intent.getSerializableExtra("data");
    }

    private void initView() {
        images = new ArrayList<>();
        list = new ArrayList<>();
        selectedDiseasesList = new ArrayList<>();
        Paper.init(this);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.llBack.setOnClickListener(view -> finish());
        binding.setModel(model);

        list.add(new DiseasesModel(0, getString(R.string.ch)));

        binding.recView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new DiseasesAdapter(selectedDiseasesList, this, this);
        binding.recView.setAdapter(adapter);

        binding.recViewImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        filesAdapter = new FilesAdapter(images, this);
        binding.recViewImages.setAdapter(filesAdapter);

        spinnerDiseasesAdapter = new SpinnerDiseasesAdapter(list, this);
        binding.spinner.setAdapter(spinnerDiseasesAdapter);

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    int pos = getItemDiseasePos(list.get(position).getId() + "");
                    if (pos == -1) {
                        selectedDiseasesList.add(list.get(position));
                        adapter.notifyItemInserted(list.size() - 1);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.flMale.setOnClickListener(v -> {
            model.setGender("male");
            binding.imageMale.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            binding.imageFemale.setColorFilter(ContextCompat.getColor(this, R.color.gray1), PorterDuff.Mode.SRC_IN);

            binding.setModel(model);
        });

        binding.flFemale.setOnClickListener(v -> {
            model.setGender("female");
            binding.imageFemale.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            binding.imageMale.setColorFilter(ContextCompat.getColor(this, R.color.gray1), PorterDuff.Mode.SRC_IN);

            binding.setModel(model);
        });

        binding.flCall.setOnClickListener(v -> {
            model.setCallMethod("audio");
            binding.setModel(model);
            binding.imageCall.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            binding.tvCall.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            binding.imageVideo.setColorFilter(ContextCompat.getColor(this, R.color.gray1), PorterDuff.Mode.SRC_IN);
            binding.tvVideo.setTextColor(ContextCompat.getColor(this, R.color.gray1));

        });

        binding.flVideo.setOnClickListener(v -> {
            model.setCallMethod("video");
            binding.setModel(model);
            binding.imageCall.setColorFilter(ContextCompat.getColor(this, R.color.gray1), PorterDuff.Mode.SRC_IN);
            binding.tvCall.setTextColor(ContextCompat.getColor(this, R.color.gray1));
            binding.imageVideo.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            binding.tvVideo.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

        });

        binding.flSelectImage.setOnClickListener(v -> {
            checkPermissions();
        });


        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Uri uri = getResultImageUri(result.getData());
                String path = Common.getImagePath(this,uri);
                images.add(0, path);
                filesAdapter.notifyItemInserted(0);
                binding.recViewImages.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.recViewImages.scrollToPosition(0);
                    }
                });
            }
        });

        binding.btnConfirm.setOnClickListener(v -> {
            Common.CloseKeyBoard(this,binding.edtAge);
            if (images.size()>0){

                reserveWithImage();
            }else {
                reserve();
            }
        });
        getDiseases();

    }

    private void reserve() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        String age = binding.edtAge.getText().toString().trim();

        List<String> diseasesIds = new ArrayList<>();
        for (DiseasesModel model:selectedDiseasesList){
            diseasesIds.add(String.valueOf(model.getId()));
        }

        Api.getService(Tags.base_url)
                .reserveDoctor(lang,model.getDoctorModel().getId()+"",model.getDateModel().getId()+"", model.getHourModel().getId(), userModel.getUser().getId()+"", model.getName(), model.getPhone(), model.getCallMethod(), model.getGender(), age,diseasesIds)
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                removeImagesFromStorage();
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

    private void reserveWithImage() {

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        String age = binding.edtAge.getText().toString().trim();
        RequestBody doc_id_part = Common.getRequestBodyText(model.getDoctorModel().getId()+"");
        RequestBody date_id_part = Common.getRequestBodyText(model.getDateModel().getId()+"");
        RequestBody hour_id_part = Common.getRequestBodyText(model.getHourModel().getId());
        RequestBody user_id_part = Common.getRequestBodyText(userModel.getUser().getId()+"");
        RequestBody name_part = Common.getRequestBodyText(model.getName());
        RequestBody phone_part = Common.getRequestBodyText(model.getPhone());
        RequestBody call_part = Common.getRequestBodyText(model.getCallMethod());
        RequestBody gender_part = Common.getRequestBodyText(model.getGender());
        RequestBody age_part = Common.getRequestBodyText(age);


        List<RequestBody> diseasesIds = new ArrayList<>();
        for (DiseasesModel model:selectedDiseasesList){
            RequestBody requestBody = Common.getRequestBodyText(model.getId()+"");
            diseasesIds.add(requestBody);
        }

        List<MultipartBody.Part> imagesParts = new ArrayList<>();
        for (String path:images){
           MultipartBody.Part part = Common.getMultiPartFromPath(path,"reservation_files[]");
           imagesParts.add(part);
        }

        Api.getService(Tags.base_url)
                .reserveDoctorWithFiles(lang,doc_id_part,date_id_part,hour_id_part,user_id_part,name_part,phone_part,call_part,gender_part,age_part,diseasesIds,imagesParts)
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                removeImagesFromStorage();
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

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, CAMERA_PERM) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, READ_PERM) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, WRITE_PERM) == PackageManager.PERMISSION_GRANTED

        ) {
            launcher.launch(getImageChooserIntent());

        } else {
            String[] perms = {CAMERA_PERM, READ_PERM, WRITE_PERM};
            ActivityCompat.requestPermissions(this, perms, 100);

        }

    }

    private Uri getImageUri() {
        Uri uri = null;
        File mainFolder = Environment.getExternalStorageDirectory();
        File appFile = new File(mainFolder.getPath(),"AliHospital");
        if (!appFile.exists()){
            appFile.mkdir();

        }
        final String imageName = "img"+(images.size()+1)+".png";
        uri = Uri.fromFile(new File(appFile.getPath(), imageName));


        return uri;
    }

    private Uri getResultImageUri(Intent intent) {
        boolean isCamera = true;
        if (intent != null) {
            String action = intent.getAction();

            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);

        }

        return isCamera ? getImageUri() : intent.getData();
    }

    private Intent getImageChooserIntent() {
        Uri uriOutput = getImageUri();
        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(cameraIntent, 0);
        for (ResolveInfo info : resolveInfo) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
            intent.setPackage(info.activityInfo.packageName);
            if (uriOutput != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriOutput);
            }

            allIntents.add(intent);
        }


        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");

        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo info : resolveInfos) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
            intent.setPackage(info.activityInfo.packageName);
            if (uriOutput != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriOutput);
            }

            allIntents.add(intent);
        }

        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().contains("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
            }
        }
        allIntents.remove(mainIntent);
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));
        return chooserIntent;
    }

    private void removeImagesFromStorage(){
        File mainFolder = Environment.getExternalStorageDirectory();
        File appFile = new File(mainFolder.getPath(),"AliHospital");
        if (appFile.exists()){
            appFile.delete();
        }
    }

    private void getDiseases() {

        Api.getService(Tags.base_url).getDiseases(lang).
                enqueue(new Callback<DiseasesDataModel>() {
                    @Override
                    public void onResponse(Call<DiseasesDataModel> call, Response<DiseasesDataModel> response) {

                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getData() != null && response.body().getStatus() == 200) {
                                list.addAll(response.body().getData());
                                runOnUiThread(() -> spinnerDiseasesAdapter.notifyDataSetChanged());
                            }


                        } else {


                            try {
                                Log.e("error_code", response.code() + "_");
                            } catch (NullPointerException e) {

                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<DiseasesDataModel> call, Throwable t) {
                        try {

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

    private int getItemDiseasePos(String id) {
        int pos = -1;

        for (int index = 0; index < selectedDiseasesList.size(); index++) {
            if (String.valueOf(selectedDiseasesList.get(index).getId()).equals(id)) {
                pos = index;
                return pos;
            }
        }

        return pos;
    }

    @Override
    public void deleteDisease(int pos) {
        selectedDiseasesList.remove(pos);
        adapter.notifyItemRemoved(pos);
        if (selectedDiseasesList.size() == 0) {
            binding.spinner.setSelection(0, true);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0) {
            launcher.launch(getImageChooserIntent());

        }
    }
}