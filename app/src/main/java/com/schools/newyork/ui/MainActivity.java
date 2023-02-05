package com.schools.newyork.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.schools.newyork.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_school_containter_view, SchoolListFragment.class, /* args= */ null)
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}