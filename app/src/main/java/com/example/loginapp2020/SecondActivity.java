package com.example.loginapp2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent intent = getIntent();
        if (intent != null) {
            TextView t1 = findViewById(R.id.text_view_second_username);
            TextView t2 = findViewById(R.id.text_view_second_password);
            TextView t3 = findViewById(R.id.text_view_second_try_password);
            t1.setText("USERNAME: " + intent.getStringExtra("USERNAME"));
            t2.setText("PASSWORD: " + intent.getStringExtra("PASSWORD"));
            t3.setText("ATTEMPETD PASSWORD: " + intent.getStringExtra("TRY"));
        }
    }
}
