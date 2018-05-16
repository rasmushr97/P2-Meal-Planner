package com.example.rasmus.p2app.frontend.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rasmus.p2app.R;
import com.example.rasmus.p2app.backend.InRAM;
import com.example.rasmus.p2app.cloud.DBHandler;
import com.example.rasmus.p2app.frontend.AppBackButtonActivity;
import com.example.rasmus.p2app.frontend.ui.misc.LoadingScreenActivity;

import java.time.LocalDate;

public class RegisterActivity extends AppBackButtonActivity {

    boolean isMale = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Register");


        final EditText etAge = (EditText) findViewById(R.id.etAge);
        final EditText etName = (EditText) findViewById(R.id.etName);
        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final EditText etHeight = (EditText) findViewById(R.id.etHeight);
        final EditText etWeight = (EditText) findViewById(R.id.etWeight);
        final EditText etGoalWeight = (EditText) findViewById(R.id.etGoalWeight);
        final Spinner spinner = (Spinner) findViewById(R.id.chooseSexSpinner);


        String[] paths = {"Female", "Male"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegisterActivity.this,
                android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        isMale = false;
                        break;

                    case 1:
                        isMale = true;
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(RegisterActivity.this, "Choose a gender", Toast.LENGTH_SHORT).show();
            }
        });


        Button registerButton = findViewById(R.id.bRegister);
        registerButton.setOnClickListener(event -> {

            if (etAge.getText().toString().equals("") || etName.getText().toString().equals("") || etPassword.getText().toString().equals("") ||
                    etUsername.getText().toString().equals("") || etHeight.getText().toString().equals("") ||
                    etWeight.getText().toString().equals("") || etGoalWeight.getText().toString().equals("")) {

                Toast.makeText(this, "You have to fill all fields", Toast.LENGTH_LONG).show();
            } else {

                SharedPreferences sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("display_name", etName.getText().toString());
                editor.apply();

                /* Gets the values for database */
                float weight = Float.parseFloat(etWeight.getText().toString());
                float goalweight = Float.parseFloat(etGoalWeight.getText().toString());
                int age = Integer.parseInt(etAge.getText().toString());
                int height = Integer.parseInt(etHeight.getText().toString());
                int sex;
                if(isMale){
                    sex = 1;
                } else{ sex = 0; }
                int toLoseWeight;
                if(weight > goalweight){
                    toLoseWeight = 1;
                } else { toLoseWeight = 0; }

                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if (DBHandler.registerUser(username, password, height, sex, age, toLoseWeight)) {
                    Toast.makeText(this, "Registration was a Success", Toast.LENGTH_SHORT).show();

                    Intent intent;
                    if (DBHandler.login(username, password)) {
                        DBHandler.addWeightMeasurement(LocalDate.now(), weight, goalweight, InRAM.userID);
                        intent = new Intent(RegisterActivity.this, LoadingScreenActivity.class);
                    } else {
                        intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    }
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                } else {
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

}
