package com.p2app.frontend.ui.misc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.p2app.R;
import com.p2app.backend.InRAM;
import com.p2app.cloud.DBHandler;
import com.p2app.exceptions.NoUserException;
import com.p2app.frontend.ui.homeScreenPages.MainActivity;
import com.p2app.frontend.ui.login.LoginActivity;

public class LoadingScreenActivity extends AppCompatActivity {
    final int startAnimationTime = 100; // Milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        /* This handles everything in the block after a certain delay
         A slight delay ensures that the the layout is displayed before it's tries get stuff from the database
         If this wasn't done the loading screen would just be black and the layout would only appear briefly after the database is loaded in */
        new Handler().postDelayed(() -> {
            // Get everything from the database
            if (!DBHandler.isConnected()) {
                DBHandler.createCon();
            }

            initializeInRAM();

            Intent intent = new Intent(LoadingScreenActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }, startAnimationTime);
    }


    private void initializeInRAM(){
        InRAM.initializeUser();

        try{
            InRAM.initializeCalender();
        } catch (NoUserException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(LoadingScreenActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        InRAM.initializeRecipes();
        InRAM.findUsers();
    }


}

