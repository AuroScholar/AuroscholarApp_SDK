package com.auro.application.core.application.di.component;

import android.content.Context;

import com.auro.application.core.application.di.module.AppModule;
import com.auro.application.core.application.di.module.HomeModule;
import com.auro.application.core.application.di.module.KycModule;
import com.auro.application.core.application.di.module.PaymentModule;
import com.auro.application.core.application.di.module.QuizModule;
import com.auro.application.core.application.di.module.TeacherModule;
import com.auro.application.core.application.di.module.UtilsModule;

public class DaggerWrapper {

    private static AppComponent mAppComponent;
    private static Context mContext;

    public static AppComponent getComponent(Context context) {
        if (mAppComponent == null) {
            initComponent();
        }
        mContext = context;
        return mAppComponent;
    }

    public static Context getmContext(){
        return mContext;
    }

    private static void initComponent () {
        mAppComponent = DaggerAppComponent
                .builder()
                .homeModule(new HomeModule())
                .kycModule(new KycModule())
                .utilsModule(new UtilsModule())
                .paymentModule(new PaymentModule())
                .quizModule(new QuizModule())
                .teacherModule(new TeacherModule())
                .appModule(new AppModule(mContext))
                .build();
    }
}
