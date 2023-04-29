package com.auroscholar.final_auroscholarapp_sdk;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.core.util.AuroScholar;
import com.auro.application.home.data.model.AuroScholarInputModel;

import com.auro.application.home.presentation.view.activity.SDKActivity;

public class MainActivity extends AppCompatActivity {
    Button btncall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btncall=findViewById(R.id.btncall);

        btncall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SDKActivity.class));

            }
        });

    }

}