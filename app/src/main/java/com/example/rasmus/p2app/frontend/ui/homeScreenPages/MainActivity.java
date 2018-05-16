package com.example.rasmus.p2app.frontend.ui.homeScreenPages;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.example.rasmus.p2app.frontend.AppDrawerActivity;
import com.example.rasmus.p2app.R;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MainActivity extends AppDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.item_1);


        // Bottom navigation bar
        BottomNavigationViewEx bottomNavigationView = findViewById(R.id.bottom_navigation);
                findViewById(R.id.bottom_navigation);

        /* Bottom navigation bar styling, animations and stuff */
        bottomNavigationView.enableAnimation(true);
        bottomNavigationView.enableShiftingMode(false);
        bottomNavigationView.enableItemShiftingMode(true);

        // Highlight the first item
        bottomNavigationView.setCurrentItem(1);

        // Icon and text size
        bottomNavigationView.setIconSize(24, 24);
        bottomNavigationView.setTextSize(14);

        // Bottom navigation bar onclickListener
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        // select a page (fragment) to switch to
                        switch (item.getItemId()) {
                            case R.id.action_home_page:
                                selectedFragment = HomePageFragment.newInstance();
                                setTitle(R.string.item_1);
                                break;
                            case R.id.action_calendar:
                                selectedFragment = CalenderFragment.newInstance();
                                setTitle(R.string.item_2);
                                break;
                            case R.id.action_shopping_list:
                                selectedFragment = ShoppingListFragment.newInstance();
                                setTitle(R.string.item_3);
                                break;
                        }
                        // Switch to the chosen fragment
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout1, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        // Display the home page fragment, when main activity is active
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout1, HomePageFragment.newInstance());
        transaction.commit();
    }

}
