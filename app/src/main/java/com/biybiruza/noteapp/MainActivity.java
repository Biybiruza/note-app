package com.biybiruza.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.biybiruza.noteapp.ui.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment, MainFragment.class, null)
                .commit();
    }
}