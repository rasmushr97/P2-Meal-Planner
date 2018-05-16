package com.example.rasmus.p2app.frontend.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.rasmus.p2app.R;
import com.example.rasmus.p2app.frontend.AppBackButtonActivity;

public class PrefActivity extends AppBackButtonActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pref);
        setTitle("Preferences");
    }
}
