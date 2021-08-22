package com.alibenalihospital.activities_fragments.activity_meeting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.alibenalihospital.R;
import com.alibenalihospital.activities_fragments.activity_home.HomeActivity;
import com.alibenalihospital.databinding.ActivityMeetingBinding;
import com.alibenalihospital.databinding.ActivitySplashBinding;
import com.alibenalihospital.language.Language;
import com.alibenalihospital.models.UserModel;
import com.alibenalihospital.preferences.Preferences;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

import io.paperdb.Paper;

public class MeetingActivity extends AppCompatActivity {

    private ActivityMeetingBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang ="ar";
    private String roomId="";

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        roomId = intent.getData().getLastPathSegment();

    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        Paper.init(this);
        lang = Paper.book().read("lang","ar");


        JitsiMeetConferenceOptions options = null;
        try {
            options = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(new URL("https://meet.jit.si"))
                    .setRoom(roomId)
                    .setAudioMuted(false)
                    .setVideoMuted(false)
                    .setWelcomePageEnabled(false)
                    .build();
            JitsiMeetActivity.launch(this,options);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


    }
}