package com.auro.application.util.firebaseAnalytics;

import android.content.Context;

import androidx.annotation.NonNull;

import com.auro.application.core.application.AuroApp;
import com.auro.application.core.database.PrefModel;

import java.util.HashMap;

public enum AnalyticsRegistry {

    INSTANCE;

    private final String PreferenceName = AuroApp.getAppContext().getPackageName();
    private FirebaseAnalyticsEvent firebaseAnalyticsEvent;

    /**
     * GET PREF MODEL OBJECT
     * SET DATA IN MODEL
     */
    public FirebaseAnalyticsEvent getModelInstance() {

        if (firebaseAnalyticsEvent == null) {
            firebaseAnalyticsEvent = new FirebaseAnalyticsEvent(AuroApp.getAppContext());
        }
        return firebaseAnalyticsEvent;
    }
}
