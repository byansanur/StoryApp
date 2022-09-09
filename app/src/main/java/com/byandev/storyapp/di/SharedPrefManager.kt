package com.byandev.storyapp.di

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPrefManager @Inject constructor(context: Context) {

    private val pref: SharedPreferences = context.getSharedPreferences("storyApp", Context.MODE_PRIVATE)
    private val editor = pref.edit()

    companion object {
        const val SP_IS_LOGIN = "sp_is_login"
        const val SP_TOKEN = "sp_token"
    }

    fun clearSharedPref() {
        editor.clear().apply()
        isLogin = false
    }

    var isLogin: Boolean
        get() = pref.getBoolean(SP_IS_LOGIN, false)
        set(value) = editor.putBoolean(SP_IS_LOGIN, value).apply()

    var token: String
        get() = pref.getString(SP_TOKEN, "")!!
        set(value) = editor.putString(SP_TOKEN, value).apply()
}