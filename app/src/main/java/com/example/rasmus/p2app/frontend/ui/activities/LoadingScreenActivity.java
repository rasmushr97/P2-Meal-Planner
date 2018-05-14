package com.example.rasmus.p2app.frontend.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.rasmus.p2app.R;
import com.example.rasmus.p2app.backend.InRAM;
import com.example.rasmus.p2app.cloud.DBHandler;

import java.util.ArrayList;
import java.util.List;

public class LoadingScreenActivity extends AppCompatActivity {
    final int startAnimationTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                DBHandler.createCon();

                InRAM.initializeUser(1);
                InRAM.initializeCalender();
                InRAM.test();

                DBHandler.closeCon();

                Intent intent = new Intent(LoadingScreenActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }, startAnimationTime);
    }
}
