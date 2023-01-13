package com.yugasa.composework.application

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.multidex.MultiDex
import com.yugasa.composework.utils.AppUtil

class YuBoApplication : Application() {

    override fun onCreate() {
        _instance = this
        context = applicationContext
        MultiDex.install(this)
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO);

        AppUtil.LogMsg(TAG, "onCreate--")
    }

    companion object {

        private val TAG = YuBoApplication::class.java.simpleName
        var _instance: YuBoApplication? = null
        var context: Context? = null


        fun getAppInstance(): YuBoApplication? {
            return _instance
        }
    }
}