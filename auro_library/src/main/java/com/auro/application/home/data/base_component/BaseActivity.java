package com.auro.application.home.data.base_component;

import android.content.Context;
import android.os.Bundle;
import android.os.TransactionTooLargeException;

import androidx.appcompat.app.AppCompatActivity;



abstract public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected abstract void init();

    protected abstract void setListener();

    protected abstract int getLayout();

}
