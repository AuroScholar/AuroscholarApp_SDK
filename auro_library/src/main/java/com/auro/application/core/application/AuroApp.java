package com.auro.application.core.application;

import android.app.Application;
import android.content.Context;

import com.auro.application.BuildConfig;
import com.auro.application.core.application.di.component.AppComponent;


import com.auro.application.core.application.di.component.DaggerAppComponent;
import com.auro.application.core.application.di.module.AppModule;
import com.auro.application.core.application.di.module.UtilsModule;
import com.auro.application.home.data.model.AuroScholarDataModel;


import io.reactivex.plugins.RxJavaPlugins;

/**
 * Created by AAK on 09-Mar-2019.
 */

public class AuroApp extends Application {

    AppComponent appComponent;
    public static AuroApp context;
    public static Application mcontext;
    public static AuroScholarDataModel auroScholarDataModel;
    public static int fragmentContainerUiId = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        RxJavaPlugins.setErrorHandler(throwable -> {}); // nothing or some logging// bY pradeep Kumar
        context = this;
        //Restring.init(context);

        AuroApp.context = this;
        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .utilsModule(new UtilsModule())
                .build();

        appComponent.injectAppContext(this);
        //FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG);

    }

    public static AppComponent getAppComponent() {
       // return appComponent;
        return null;
    }
    public static void initializeWithDefaults(Application mApplication){
        mcontext = mApplication;
    }

    /*public static AuroApp getAppContext() {
        return context;
    }*/

    public static Application getAppContext() {
        return mcontext;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }


    public static AuroScholarDataModel getAuroScholarModel() {
        return auroScholarDataModel;
    }

    public static void setAuroModel(AuroScholarDataModel model) {
        auroScholarDataModel = model;
        fragmentContainerUiId = model.getFragmentContainerUiId();
    }

    public static int getFragmentContainerUiId() {
        return fragmentContainerUiId;
    }

}
