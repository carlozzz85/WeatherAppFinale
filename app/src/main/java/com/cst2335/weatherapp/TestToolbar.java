package com.cst2335.weatherapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
//import com.cst2335.weatherapp.databinding.ActivityTestToolbarBinding;

public class TestToolbar extends AppCompatActivity {

    //private ActivityTestToolbarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // binding = ActivityTestToolbarBinding.inflate(getLayoutInflater());
       // setContentView(binding.getRoot());

        // Setting up the toolbar
       // Toolbar toolbar = binding.toolbar;
       // setSupportActionBar(toolbar);

        // Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // You can set a title for your toolbar here
        getSupportActionBar().setTitle("Test Toolbar");
    }
}
