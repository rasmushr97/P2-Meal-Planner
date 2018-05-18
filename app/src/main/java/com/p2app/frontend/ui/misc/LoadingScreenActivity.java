package com.p2app.frontend.ui.misc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.p2app.R;
import com.p2app.backend.InRAM;
import com.p2app.cloud.DBHandler;
import com.p2app.frontend.ui.homeScreenPages.MainActivity;

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

            InRAM.initializeUser();
            InRAM.initializeCalender();
            InRAM.initializeRecipes();
            InRAM.findUsers();

            //DBHandler.closeCon();

            Intent intent = new Intent(LoadingScreenActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }, startAnimationTime);
    }
}
