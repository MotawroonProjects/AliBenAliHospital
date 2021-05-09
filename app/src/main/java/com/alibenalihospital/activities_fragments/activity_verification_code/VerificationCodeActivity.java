package com.alibenalihospital.activities_fragments.activity_verification_code;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.alibenalihospital.R;
import com.alibenalihospital.activities_fragments.activity_home.HomeActivity;
import com.alibenalihospital.activities_fragments.activity_sign_up.SignUpActivity;
import com.alibenalihospital.databinding.ActivityVerificationCodeBinding;
import com.alibenalihospital.language.Language;
import com.alibenalihospital.preferences.Preferences;
import com.alibenalihospital.share.Common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;

public class VerificationCodeActivity extends AppCompatActivity {
    private ActivityVerificationCodeBinding binding;
    private String lang;
    private String phone_code;
    private String phone;
    private CountDownTimer timer;
    private FirebaseAuth mAuth;
    private String verificationId;
    private String smsCode;
    private Preferences preferences;
    private boolean canSend = false;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verification_code);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            phone_code = intent.getStringExtra("phone_code");
            phone = intent.getStringExtra("phone");

        }
    }

    private void initView() {
        preferences = Preferences.getInstance();

        mAuth = FirebaseAuth.getInstance();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        String phone = phone_code+this.phone;
        binding.setPhone(phone);
        binding.tvResend.setOnClickListener(view -> {
            if (canSend){
                sendSmsCode();
            }
        });
        binding.btnConfirm.setOnClickListener(view -> {
            String code = binding.edtCode.getText().toString().trim();
//            Intent intent=getIntent();
//            setResult(RESULT_OK,intent);
//            finish();
            if (!code.isEmpty()) {
                binding.edtCode.setError(null);
                Common.CloseKeyBoard(this, binding.edtCode);
                checkValidCode(code);
            } else {
                binding.edtCode.setError(getString(R.string.field_required));
            }

        });
        sendSmsCode();

    }

    private void sendSmsCode() {

        startTimer();

        mAuth.setLanguageCode(lang);
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                smsCode = phoneAuthCredential.getSmsCode();
                binding.edtCode.setText(smsCode);
                checkValidCode(smsCode);
            }

            @Override
            public void onCodeSent(@NonNull String verification_id, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verification_id, forceResendingToken);
                VerificationCodeActivity.this.verificationId = verification_id;
            }


            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                if (e.getMessage() != null) {
                    Common.CreateDialogAlert(VerificationCodeActivity.this, e.getMessage());
                } else {
                    Common.CreateDialogAlert(VerificationCodeActivity.this, getString(R.string.failed));

                }
            }
        };
        PhoneAuthProvider.getInstance()
                .verifyPhoneNumber(
                        phone_code + phone,
                        120,
                        TimeUnit.SECONDS,
                        this,
                        mCallBack

                );


    }

    private void startTimer() {
        canSend = false;
        binding.tvResend.setEnabled(false);
        timer = new CountDownTimer(120 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                SimpleDateFormat format = new SimpleDateFormat("mm:ss", Locale.ENGLISH);
                String time = format.format(new Date(l));
                binding.tvCounter.setText(time);
            }

            @Override
            public void onFinish() {
                canSend = true;
                binding.tvCounter.setText("00:00");
                binding.tvResend.setEnabled(true);
            }
        };
        timer.start();
    }


    private void checkValidCode(String code) {

        if (verificationId != null) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            mAuth.signInWithCredential(credential)
                    .addOnSuccessListener(authResult -> {
                        Intent intent=getIntent();
                        setResult(RESULT_OK,intent);
                        finish();
                      //  login();
                    }).addOnFailureListener(e -> {
                if (e.getMessage() != null) {
                    Common.CreateDialogAlert(this, e.getMessage());
                } else {
                    Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(this,"wait sms", Toast.LENGTH_SHORT).show();
        }

    }

    private void login() {
        navigateToSignUpActivity();

    }


    private void navigateToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToSignUpActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("phone_code", phone_code);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
