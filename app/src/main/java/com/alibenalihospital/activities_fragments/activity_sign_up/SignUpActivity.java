package com.alibenalihospital.activities_fragments.activity_sign_up;

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
    private Uri uri = null;
    private SignUpModel signUpModel;
    private UserModel userModel;
    private Preferences preferences;
    private String phone;
    private String phone_code;
    private String lang;


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
            signUpModel.setPhone_code("+966");
            signUpModel.setPhone("");
        } else {
            signUpModel.setName(userModel.getUser().getName());
            signUpModel.setPhone(userModel.getUser().getPhone());
            signUpModel.setPhone_code(userModel.getUser().getPhone_code());
            signUpModel.setPassword("123456");
            binding.ll.setVisibility(View.GONE);
            binding.btnSignUp.setText(R.string.update_profile);
        }

        binding.setModel(signUpModel);

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
                startActivityForResult(intent, 100);
            } else {
                // finish();
                signUp();

            }
        }

    }


    private void signUp() {


        if (userModel == null) {
            if (uri == null) {
                signUpWithoutImage();
            } else {
                signUpWithImage();
            }
        } else {
            if (uri == null) {
                updateProfileWithoutImage();
            } else {
                updateProfileWithImage();
            }
        }

    }

    private void updateProfileWithImage() {
      /*  ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        RequestBody user_id_part = Common.getRequestBodyText(userModel.getUser().getId() + "");
        RequestBody name_part = Common.getRequestBodyText(signUpModel.getName());
        RequestBody gender_part = Common.getRequestBodyText(signUpModel.getGender());
        RequestBody birthday_part = Common.getRequestBodyText(signUpModel.getBirth_date());

        MultipartBody.Part image = Common.getMultiPartImage(this, uri, "logo");


        Api.getService(Tags.base_url)
                .updateProfileWithImage("Bearer " + userModel.getUser().getToken(), user_id_part, name_part, gender_part, birthday_part, image)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                preferences.create_update_userdata(SignUpActivity.this, response.body());
                                setResult(RESULT_OK);
                                finish();
                            } else if (response.body().getStatus() == 402) {
                                Toast.makeText(SignUpActivity.this, R.string.user_exist, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 500) {
                                Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                });*/
    }

    private void updateProfileWithoutImage() {
      /*  ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .updateProfileWithoutImage("Bearer " + userModel.getUser().getToken(),userModel.getUser().getId()+"", signUpModel.getName(), signUpModel.getGender(),signUpModel.getBirth_date())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                preferences.create_update_userdata(SignUpActivity.this, response.body());
                                setResult(RESULT_OK);
                                finish();
                            } else if (response.body().getStatus() == 402) {
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
                });*/
    }

    private void signUpWithoutImage() {
       /* ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .signUpWithoutImage(signUpModel.getName(), signUpModel.getPhone_code(), signUpModel.getPhone(), signUpModel.getPassword(), signUpModel.getGender(), signUpModel.getBirth_date(), "android")
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                preferences.create_update_userdata(SignUpActivity.this, response.body());
                                navigateToHomeActivity();
                            } else if (response.body().getStatus() == 406) {
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
                });*/
    }

    private void signUpWithImage() {

      /*  ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        RequestBody name_part = Common.getRequestBodyText(signUpModel.getName());
        RequestBody phone_code_part = Common.getRequestBodyText(signUpModel.getPhone_code());
        RequestBody phone_part = Common.getRequestBodyText(signUpModel.getPhone());
        RequestBody pass_part = Common.getRequestBodyText(signUpModel.getPassword());
        RequestBody gender_part = Common.getRequestBodyText(signUpModel.getGender());
        RequestBody birthdate_part = Common.getRequestBodyText(signUpModel.getBirth_date());
        RequestBody software_type_part = Common.getRequestBodyText("android");

        MultipartBody.Part image = Common.getMultiPart(this, uri, "logo");


        Api.getService(Tags.base_url)
                .signUpWithImage(name_part, phone_code_part, phone_part, pass_part, gender_part, birthdate_part, software_type_part, image)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                preferences.create_update_userdata(SignUpActivity.this, response.body());
                                navigateToHomeActivity();
                            } else if (response.body().getStatus() == 406) {
                                Toast.makeText(SignUpActivity.this, R.string.user_exist, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 500) {
                                Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                });*/

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