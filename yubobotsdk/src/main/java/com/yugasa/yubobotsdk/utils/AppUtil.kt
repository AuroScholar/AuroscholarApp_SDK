package com.yugasa.yubobotsdk.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.io.InputStream

object AppUtil {

    val TAG = AppUtil::class.java.simpleName

    val JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()

    fun getCustomGson(): Gson? {
        val gb = GsonBuilder()
        return gb.create()
    }

    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context, code: Int, yuboDeviceId: String = ""): String {
        return when (code) {
            YuboBotConstants.YUBO_DEVICE_ID_0 -> {
                Settings.Secure.getString(
                    context.contentResolver,
                    Settings.Secure.ANDROID_ID
                ) + "_" + context.packageName + "_" + System.currentTimeMillis()
                    .toString()
            }
            YuboBotConstants.YUBO_DEVICE_ID_1 -> {
                Settings.Secure.getString(
                    context.contentResolver,
                    Settings.Secure.ANDROID_ID
                ) + "_" + context.packageName
            }
            YuboBotConstants.YUBO_DEVICE_ID_2 -> {
                yuboDeviceId.ifEmpty { throw RuntimeException("This is a crash") }
            }
            else -> {
                throw RuntimeException("This is a crash");
            }
        }
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
            requestBody = requestJson.toRequestBody(JSON)
            return requestBody
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return requestBody
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

}