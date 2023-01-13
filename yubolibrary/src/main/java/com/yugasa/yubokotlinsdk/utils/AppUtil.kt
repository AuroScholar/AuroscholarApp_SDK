package com.yugasa.yubokotlinsdk.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.yugasa.yubokotlinsdk.BuildConfig
import com.yugasa.yubokotlinsdk.utils.AppConstants
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*
import java.lang.reflect.Type

object AppUtil {

    val TAG = AppUtil::class.java.simpleName

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
            val requestJson: String = getCustomGson()?.toJson(`object`)!!
            //            LogMsg("requestJson", url+"  "+requestJson);  String url,
            requestBody = requestJson.toRequestBody(JSON)
            return requestBody
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return requestBody
    }

    fun showToast(context: Context?, msg: String?) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }


    @Throws(IOException::class)
    fun rotateImageIfRequired(context: Context, img: Bitmap, selectedImage: Uri): Bitmap? {
        val input: InputStream? = context.contentResolver.openInputStream(selectedImage)
        val ei: ExifInterface =
            if (Build.VERSION.SDK_INT > 23) ExifInterface(input!!) else ExifInterface(selectedImage.path!!)
        return when (ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270)
            else -> img
        }
    }

    private fun rotateImage(img: Bitmap, degree: Int): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
        img.recycle()
        return rotatedImg
    }


    fun <T> getPayloadAsList(listType: Type, jsonData: String): List<T>? {
        return Gson().fromJson<List<T>>(jsonData, listType)
    }

    fun <T> getPayload(payloadClass: Class<T>, jsonData: String): T {
        return Gson().fromJson(jsonData, payloadClass)
    }
}