package com.auro.application.home.presentation.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.auro.application.R;
import com.auro.application.core.application.di.component.DaggerWrapper;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.core.util.AuroScholar;
import com.auro.application.home.data.model.AuroScholarInputModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.LanguageMasterDynamic;
import com.auro.application.home.data.model.response.LanguageListResModel;
import com.auro.application.home.presentation.view.fragment.BottomSheetUsersDialog;
import com.auro.application.util.RemoteApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChildAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdk);
        openBottomSheetDialog();
    }
    void openBottomSheetDialog()
    {

        BottomSheetUsersDialog bottomSheet = new BottomSheetUsersDialog();
        bottomSheet.show(getSupportFragmentManager(),
                "ModalBottomSheet");

    }


}
