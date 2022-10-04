package com.byandev.storyapp.di

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPrefManager @Inject constructor(context: Context) {

    private val pref: SharedPreferences = context.getSharedPreferences("storyApp", Context.MODE_PRIVATE)
    private val editor = pref.edit()

    companion object {
        const val SP_INTRO = "sp_intro"
        const val SP_IS_LOGIN = "sp_is_login"
        const val SP_USER_ID = "sp_user_id"
        const val SP_NAME = "sp_name"
        const val SP_TOKEN = "sp_token"
        const val SP_IS_REMEMBER = "sp_is_remember"
        const val SP_EMAIL = "sp_email"
        const val SP_PASS = "sp_pass"
    }

    fun logoutRemoveToken() {
        editor.remove(SP_TOKEN).apply()
        isLogin = false
    }

    fun logoutNotRemember() {
        editor.remove(SP_USER_ID).apply()
        editor.remove(SP_NAME).apply()
        editor.remove(SP_TOKEN).apply()
        editor.remove(SP_IS_REMEMBER).apply()
        editor.remove(SP_EMAIL).apply()
        editor.remove(SP_PASS).apply()
        isLogin = false
    }

    var isIntroSp: Boolean
        get() = pref.getBoolean(SP_INTRO, true)
        set(value) = editor.putBoolean(SP_INTRO, value).apply()

    var email: String
        get() = String(Base64.decode(pref.getString(SP_EMAIL, ""), 0))
        set(value) = editor.putString(SP_EMAIL, Base64.encodeToString(value.toByteArray(), 0)).apply()

    var userKey: String
        get() = String(Base64.decode(pref.getString(SP_PASS, ""), 0))
        set(value) = editor.putString(SP_PASS, Base64.encodeToString(value.toByteArray(), 0)).apply()

    var isLogin: Boolean
        get() = pref.getBoolean(SP_IS_LOGIN, false)
        set(value) = editor.putBoolean(SP_IS_LOGIN, value).apply()

    var userId: String
        get() = String(Base64.decode(pref.getString(SP_USER_ID, ""), 0))
        set(value) = editor.putString(SP_USER_ID, Base64.encodeToString(value.toByteArray(), 0)).apply()

    var userName: String
        get() = pref.getString(SP_NAME, "")!!
        set(value) = editor.putString(SP_NAME, value).apply()

    var token: String
        get() = pref.getString(SP_TOKEN, "")!!
        set(value) = editor.putString(SP_TOKEN, value).apply()

    var isRemember: Boolean
        get() = pref.getBoolean(SP_IS_REMEMBER, false)
        set(value) = editor.putBoolean(SP_IS_REMEMBER, value).apply()
}