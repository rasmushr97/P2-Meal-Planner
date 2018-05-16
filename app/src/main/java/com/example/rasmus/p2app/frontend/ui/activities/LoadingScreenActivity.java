package com.example.rasmus.p2app.frontend.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.rasmus.p2app.R;
import com.example.rasmus.p2app.backend.InRAM;
import com.example.rasmus.p2app.backend.userclasses.LocalUser;
import com.example.rasmus.p2app.cloud.DBHandler;

public class LoadingScreenActivity extends AppCompatActivity {
    final int startAnimationTime = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        new Handler().postDelayed(() -> {
            if (!DBHandler.isConnected()) {
                DBHandler.createCon();
            }

            InRAM.initializeUser(InRAM.userID);
            InRAM.initializeCalender();
            InRAM.test();

            //DBHandler.closeCon();

            Intent intent = new Intent(LoadingScreenActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }, startAnimationTime);
    }
}
