package com.salwa.salwa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Handler untuk menampilkan splash screen selama 4 detik (4000 mil detik) sebelum masuk ke login / homepage
        new Handler()
                .postDelayed(()
                -> startActivity(new Intent(this, LoginActivity.class)), 4000);
    }
}