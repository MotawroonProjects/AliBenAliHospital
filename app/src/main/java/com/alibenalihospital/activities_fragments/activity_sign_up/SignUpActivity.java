package com.alibenalihospital.activities_fragments.activity_sign_up;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.alibenalihospital.R;
import com.alibenalihospital.activities_fragments.activity_home.HomeActivity;
import com.alibenalihospital.activities_fragments.activity_login.LoginActivity;
import com.alibenalihospital.activities_fragments.activity_verification_code.VerificationCodeActivity;
import com.alibenalihospital.adapters.SpinnerAdapter;
import com.alibenalihospital.databinding.ActivitySignUpBinding;
import com.alibenalihospital.language.Language;
import com.alibenalihospital.models.SettingModel;
import com.alibenalihospital.models.SignUpModel;
import com.alibenalihospital.models.UserModel;
import com.alibenalihospital.preferences.Preferences;
import com.alibenalihospital.remote.Api;
import com.alibenalihospital.share.Common;
import com.alibenalihospital.tags.Tags;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private SignUpModel signUpModel;
    private UserModel userModel;
    private Preferences preferences;
    private String phone;
    private String phone_code;
    private String lang;

    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        initView();

    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        binding.setLang(lang);

        signUpModel = new SignUpModel();
        if (userModel == null) {
            signUpModel.setPhone_code("+20");
            signUpModel.setPhone("");
            binding.tvLogin.setText(Html.fromHtml(getString(R.string.have_account)));

        } else {
            binding.llPassword.setVisibility(View.GONE);
            signUpModel.setName(userModel.getUser().getName());
            signUpModel.setPhone(userModel.getUser().getPhone());
            signUpModel.setPhone_code(userModel.getUser().getPhone_code());
            signUpModel.setPassword("123456");
            signUpModel.setRe_password("123456");
            binding.ll.setVisibility(View.GONE);
            binding.btnSignUp.setText(R.string.update_profile);

        }

        binding.setModel(signUpModel);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                signUp();

            }
        });
        binding.btnSignUp.setOnClickListener(view -> {
            checkDataValid();
        });


        binding.tvLogin.setOnClickListener(view -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

    }


//    private void getDataFromIntent() {
//        Intent intent = getIntent();
//        if (intent != null) {
//            phone_code = intent.getStringExtra("phone_code");
//            phone = intent.getStringExtra("phone");
//
//        }
//    }


    public void checkDataValid() {


        if (signUpModel.isDataValid(this)) {
            if (userModel == null) {
                Common.CloseKeyBoard(this, binding.edtPhone);
                Intent intent = new Intent(this, VerificationCodeActivity.class);
                intent.putExtra("phone_code", signUpModel.getPhone_code());
                intent.putExtra("phone", signUpModel.getPhone());
                launcher.launch(intent);
            } else {
                // finish();
                signUp();

            }
        }

    }


    private void signUp() {


        if (userModel == null) {
            signUpWithoutImage();
        } else {
            updateProfile();
        }

    }

    private void updateProfile() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .updateProfile(userModel.getUser().getId()+"",signUpModel.getName())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                preferences.create_update_userdata(SignUpActivity.this, response.body());
                                setResult(RESULT_OK);
                                finish();
                            } else if (response.body().getStatus() == 409) {
                                Toast.makeText(SignUpActivity.this, R.string.user_exist, Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            if (response.code() == 500) {
                                Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }

                            try {
                                Log.e("error", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(SignUpActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }


    private void signUpWithoutImage() {
        if(signUpModel.getPhone().startsWith("0")){
            signUpModel.setPhone(signUpModel.getPhone().replaceFirst("0",""));
        }
       // signUpModel.setPhone(phone);
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .signUpWithoutImage(signUpModel.getName(), signUpModel.getPhone_code(), signUpModel.getPhone(), signUpModel.getPassword(), signUpModel.getRe_password())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            Log.e("flkfkfk",response.body().getStatus()+"");
                            if (response.body().getStatus() == 200) {
                                preferences.create_update_userdata(SignUpActivity.this, response.body());
                                navigateToHomeActivity();
                            } else if (response.body().getStatus() == 409) {
                                Toast.makeText(SignUpActivity.this, R.string.user_exist, Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            if (response.code() == 500) {
                                Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }

                            try {
                                Log.e("error", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(SignUpActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }


    private void navigateToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (userModel != null) {

        }
        finish();
    }
}