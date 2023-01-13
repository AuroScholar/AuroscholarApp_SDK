package com.yugasa.yubobotsdk.utils

import android.util.Log
import com.yugasa.yubobotsdk.BuildConfig

class AppLogger {
    companion object {
        private val DEBUG = BuildConfig.DEBUG

        fun d(tag: String, msg: String) {
            if (DEBUG) {
                Log.d(tag, "" + msg)
            }
        }

        fun e(tag: String, msg: String?) {
            if (DEBUG) {
                Log.e(tag, "" + msg)
            }
        }

        fun v(tag: String, msg: String) {
            if (DEBUG) {
                Log.v(tag, "" + msg)
            }
        }

        fun i(tag: String, msg: String) {
            if (DEBUG) {
                Log.i(tag, "" + msg)
            }
        }

        fun w(tag: String, msg: String) {
            if (DEBUG) {
                Log.w(tag, "" + msg)
            }
        }
    }
}
