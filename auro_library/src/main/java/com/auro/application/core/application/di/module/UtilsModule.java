package com.auro.application.core.application.di.module;


import android.util.Log;

import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.core.network.URLConstant;
import com.auro.application.util.AppLogger;
import com.auro.application.util.DeviceUtil;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


@Module
public class UtilsModule {

    @Provides
    @Singleton
    HttpLoggingInterceptor provideInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    @Provides
    @Singleton
    Gson provideGson() {

        // set the field name policy as you want to send like with underscores, lowercase, with dases policy
        GsonBuilder builder = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY);
        return builder.setLenient().create();
    }

    @Provides
    @Singleton
    OkHttpClient getRequestHeader(HttpLoggingInterceptor httpLoggingInterceptor) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header(AppConstant.CONTENT_TYPE, AppConstant.APPLICATION_JSON)
                        .header(AppConstant.DEVICE_ID, DeviceUtil.getDeviceId(AuroApp.getAppContext()))
                        .header(AppConstant.DEVICE_TYPE, AppConstant.PLATFORM_ANDROID)
                        .header(AppConstant.LANGUAGE, "EN")
                        .header("Authorization", Credentials.basic("bhanu", "123"))
                        //.header(AUTH_TOKEN, AuroAppPref.INSTANCE.getModelInstance().getOtpRes().getAuthToken())
                        .header(AppConstant.LANG_CODE, AppConstant.LANGUAGE_ENG)
                        .header(AppConstant.SECRET_ID, AppConstant.SECRETE_ID_TOKEN)
                        .header(AppConstant.SECRET_KEY, AppConstant.SECRET_KEY_TOKEN)
                        .build();
                return chain.proceed(request);
            }
        })
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS);

        return httpClient.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {

        return new Retrofit.Builder()
                .baseUrl(URLConstant.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

}
