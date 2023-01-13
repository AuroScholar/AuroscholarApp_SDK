package com.yugasa.composework.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.content.res.TypedArray
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.yugasa.composework.BuildConfig
import com.yugasa.composework.R
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.PrintWriter
import java.io.StringWriter
import java.io.Writer
import java.lang.Exception
import java.util.HashMap

class AppUtil {

    companion object {
        protected val TAG = AppUtil::class.java.simpleName

        var typefaceCache: HashMap<String, Typeface> = HashMap<String, Typeface>()

        val JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()

        // Log msg
        fun LogMsg(tag: String?, msg: String?) {
            if (BuildConfig.DEBUG) {
                Log.e(tag, msg!!)
            }
        }

        fun LogException(ex: Throwable) {
            if (true == AppConstants._isFileExceptionLoggingEnabled) {
                val writer: Writer = StringWriter()
                val printWriter = PrintWriter(writer)
                ex.printStackTrace(printWriter)
                val stackTrace = writer.toString()
                printWriter.close()
            }
        }

        fun LogError(tag: String?, msg: String?) {
            if (AppConstants._isLoggingEnabled) {
                if (AppConstants._isFileLoggingEnabled) {
                    Log.e(tag, msg!!)
                }
            }
        }

        fun getAppSharedPreference(aContext: Context?): SharedPreferences? {
            var sp: SharedPreferences? = null
            if (null != aContext) {
                sp = aContext.getSharedPreferences(
                        AppConstants.APP_SHARED_PREF_NAME,
                        Context.MODE_PRIVATE
                )
            }
            return sp
        }

        fun getCustomGson(): Gson? {
            val gb = GsonBuilder()
            return gb.create()
        }

        fun hideKeyboard(activity: Activity) {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            var view = activity.currentFocus
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun getRequestBody(`object`: Any?): RequestBody? {
            var requestBody: RequestBody? = null
            try {
                val requestJson: String? = getCustomGson()?.toJson(`object`)
                //            LogMsg("requestJson", url+"  "+requestJson);  String url,
                requestBody = requestJson!!.toRequestBody(JSON)
                return requestBody
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return requestBody
        }

        fun showToast(context: Context?, msg: String?) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }

        fun setTypeface(attrs: AttributeSet?, textView: TextView) {
            val context = textView.context
            val values = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView)
            val typefaceName = values.getString(R.styleable.CustomTextView_typeface)
            if (typefaceCache.containsKey(typefaceName)) {
                textView.setTypeface(typefaceCache.get(typefaceName))
            } else {
                val typeface: Typeface
                typeface = try {
                    Typeface.createFromAsset(
                        textView.context.assets,
                        context.getString(R.string.assets_fonts_folder) + typefaceName
                    )
                } catch (e: Exception) {
                    Log.v(context.getString(R.string.app_name), e.toString())
                    return
                }
                typefaceName?.let { typefaceCache.put(it, typeface) }
                textView.setTypeface(typeface)
            }
            values.recycle()
        }

        fun exitApp(context: Context?, activity: AppCompatActivity) {
            AlertDialog.Builder(context)
                .setTitle("Confirm action")
                .setMessage("Do you want to Exit?")
                .setPositiveButton("Yes") { dialog: DialogInterface?, which: Int ->
                    activity.finishAffinity()
                    activity.finish()
                }.setNegativeButton("No", null).show()
        }
    }
}