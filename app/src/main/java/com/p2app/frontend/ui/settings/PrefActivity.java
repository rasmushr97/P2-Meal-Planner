package com.p2app.frontend.ui.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.androidbuts.multispinnerfilter.MultiSpinner;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;
import com.p2app.R;
import com.p2app.frontend.AppBackButtonActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrefActivity extends AppBackButtonActivity {

    SharedPreferences sharedPref = null;
    Switch isVegan;
    List<Integer> mUserItems = new ArrayList<>();
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pref);
        setTitle("Preferences");

        // The SharedPreferences class allow you to store primitive data on the phone itself
        sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE);

        isVegan = findViewById(R.id.veganSwitch);
        // Set switch to the previous value set with SharedPreference
        isVegan.setChecked(sharedPref.getBoolean("vegan", false));

        isVegan.setOnCheckedChangeListener((compoundButton, b) -> {
            // When the switch state is change, also change the value saved in SharedPreference
            editor = sharedPref.edit();
            editor.putBoolean("vegan", b);
            editor.apply();
        });


        String[] items = {"Display Name", "Dislike", "Allergies"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PrefActivity.this, android.R.layout.simple_list_item_1, items){
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.rgb(51,51,51));
                return view;
            }
        };

        ListView listView = findViewById(R.id.preferenceListView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            mUserItems = new ArrayList<>();
            switch (i) {
                case 0:
                    displayNameClicked();
                    break;

                case 1:
                    createAlertDialog(R.array.pref_dislikes_list, "dislikes", "Choose Dislikes");
                    break;

                case 2:
                    createAlertDialog(R.array.pref_allergies_list, "allergies", "Pick your Allergies");
                    break;
            }
        });

    }

    private void displayNameClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PrefActivity.this);
        String titleText = "Enter new Display Name";
        // Just changing the color
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.rgb(51,51,51));
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(titleText);
        stringBuilder.setSpan(colorSpan, 0, titleText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        final EditText input = new EditText(PrefActivity.this);
        input.setText(sharedPref.getString("display_name", ""));

        builder.setTitle(stringBuilder);
        builder.setView(input);
        builder.setPositiveButton("OK", (dialog, which) -> {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("display_name", input.getText().toString());
            editor.apply();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void createAlertDialog(int arrayID, String saveAs, String alertTitle){

        String dislikes = sharedPref.getString(saveAs, "");
        if (!dislikes.equals("")) {
            mUserItems = createIntListFromString(dislikes);
        }

        String[] listItems = getResources().getStringArray(arrayID);
        boolean[] checkedItems = new boolean[listItems.length];

        for (int i : mUserItems) {
            checkedItems[i] = true;
        }
        // All this just changes the color
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.rgb(51,51,51));
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(alertTitle);
        stringBuilder.setSpan(colorSpan, 0, alertTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(PrefActivity.this);
        mBuilder
                .setTitle(stringBuilder)
                .setCancelable(false)
                .setMultiChoiceItems(listItems, checkedItems, (dialogInterface, i, isChecked) -> {
                    if (isChecked) {
                        mUserItems.add(i);
                    } else {
                        mUserItems.remove((Integer)i);
                    }
                })
                .setPositiveButton(R.string.ok_label, (dialogInterface, i) -> {
                    editor = sharedPref.edit();
                    editor.remove(saveAs);
                    editor.putString(saveAs, createString(mUserItems));
                    editor.apply();
                })
                .setNegativeButton(R.string.dismiss_label, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .setNeutralButton(R.string.clear_all_label, (dialogInterface, i) -> {
                    mUserItems.clear();
                    editor = sharedPref.edit();
                    editor.remove(saveAs);
                    editor.apply();
                });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private String createString(List<Integer> list) {
        String res = "";
        int counter = 0;
        for (int i : list) {
            if (counter == list.size() - 1) {
                res = res.concat(i + "");
                return res;
            }
            res = res.concat(i + ",");
        }
        return res;
    }

    private List<Integer> createIntListFromString(String string) {
        List<String> separated = Arrays.asList(string.split(","));
        List<Integer> res = new ArrayList<>();

        for (String s : separated) {
            res.add(Integer.parseInt(s));
        }

        return res;
    }
}
