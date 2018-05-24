package com.p2app.frontend.ui.settings;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.p2app.R;
import com.p2app.frontend.AppBackButtonActivity;
import com.p2app.frontend.AppDrawerActivity;
import com.p2app.frontend.ui.login.LoginActivity;

public class SettingsActivity extends AppDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");

        ListView listView = findViewById(R.id.settingListView);

        String[] items = {"Preferences", "Login Settings"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SettingsActivity.this, android.R.layout.simple_list_item_1, items){
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.rgb(51,51,51));
                return view;
            }
        };
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, i, l) ->  {
            Intent intent = null;
            switch (i){
                case 0:
                    intent = new Intent(SettingsActivity.this, PrefActivity.class);
                    break;

                case 1:
                    intent = new Intent(SettingsActivity.this, LoginActivity.class);
                    break;
            }
            startActivity(intent);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        });

    }

}
