package com.byandev.storyapp.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Icon
import android.os.Build
import android.util.Base64.NO_PADDING
import android.util.Base64.encodeToString
import androidx.annotation.RequiresApi
import com.byandev.storyapp.R
import com.byandev.storyapp.data.model.ErrorBody
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.HttpException
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

const val BASE_URL = "https://story-api.dicoding.dev/v1/"

const val PICK_IMAGE = "PICK_IMAGE"

fun handlingError(error: Throwable) : String {
    return when (error) {
        is HttpException -> getMessageError(error.response()?.errorBody()?.string())
        is TimeoutException, is SocketTimeoutException -> "Connection timed out..."
        is ConnectException, is SocketException -> "Connect error occurred"
        is UnknownHostException -> "No address associated with hostname"
        else -> "Unknown error occurred"
    }
}

@Throws(IOException::class)
fun getMessageError(errorBody: String?): String {
    return if (errorBody.isNullOrEmpty()) {
        val gson = Gson()
        val type = object : TypeToken<ErrorBody>() {}.type
        val errorResponse: ErrorBody = gson.fromJson(errorBody, type)
        errorResponse.message
    } else {
        try {
            val gson = Gson()
            val type = object : TypeToken<ErrorBody>() {}.type
            val errorResponse: ErrorBody = gson.fromJson(errorBody, type)
            errorResponse.message
        } catch (e: Exception) {
            e.printStackTrace()
            "Failed to read server"
        }
    }
}

fun dialogLoading(dialog: Dialog) {
    dialog.setContentView(R.layout.item_dialog_loading)
    dialog.setCancelable(false)
    dialog.show()
}


@RequiresApi(Build.VERSION_CODES.M)
fun textAsBitmap(text: String, textSize: Float, textColor: Int): Icon? {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    paint.textSize = textSize
    paint.color = textColor
    paint.textAlign = Paint.Align.LEFT
    val baseline: Float = -paint.ascent() // ascent() is negative
    val width = (paint.measureText(text) + 0.0f).toInt() // round
    val height = (baseline + paint.descent() + 0.0f).toInt()
    val image: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(image)
    canvas.drawText(text, 0F, baseline, paint)
    return Icon.createWithBitmap(image)
}

fun covertTimeToText(dataDate: String?, context: Context): String? {
    var convTime: String? = null
    val suffix = context.getString(R.string.time_ago)
    try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        dateFormat.timeZone = TimeZone.getTimeZone("GMT")
        val pasTime = dateFormat.parse(dataDate).time
        val nowTime = System.currentTimeMillis()
        val dateDiff: Long = nowTime - pasTime
        val second: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
        val minute: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
        val hour: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)
        val day: Long = TimeUnit.MILLISECONDS.toDays(dateDiff)
        when {
            minute < 60 -> {
                convTime = "$minute Minutes $suffix"
            }
            hour < 24 -> {
                convTime = "$hour Hours $suffix"
            }
            day >= 7 -> {
                convTime = when {
                    day > 360 -> (day / 360).toString() + " Years " + suffix
                    day > 30 -> (day / 30).toString() + " Months " + suffix
                    else -> (day / 7).toString() + " Week " + suffix
                }
            }
            day < 7 -> {
                convTime = "$day Days $suffix"
            }
        }
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return convTime
}

fun getEncoded64ImageStringFromBitmap(bitmap: Bitmap): String {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    val byteFormat: ByteArray = stream.toByteArray()
    return encodeToString(byteFormat, NO_PADDING)
}