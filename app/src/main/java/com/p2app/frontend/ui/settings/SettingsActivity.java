package com.p2app.frontend.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.p2app.R;
import com.p2app.frontend.AppBackButtonActivity;
import com.p2app.frontend.ui.login.LoginActivity;

public class SettingsActivity extends AppBackButtonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");

        ListView listView = findViewById(R.id.settingListView);

        String[] items = {"Preferences", "Login Settings"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(SettingsActivity.this, android.R.layout.simple_list_item_1, items);
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
