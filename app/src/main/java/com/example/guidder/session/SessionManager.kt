package com.example.guidder.session

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "guidder_session"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_EMAIL = "email"
        private const val KEY_NAME = "nama"
        private const val KEY_USER_ID = "id_user"
    }

    fun createLoginSession(id_user: Int, email: String, nama: String) {
        val editor = sharedPref.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putInt(KEY_USER_ID, id_user)
        editor.putString(KEY_EMAIL, email)
        editor.putString(KEY_NAME, nama)
        editor.apply()
    }
    fun updateUserName(newUsername: String) {
        sharedPref.edit().apply {
            putString(KEY_NAME, newUsername)
            apply()
        }
    }

    fun isLoggedIn(): Boolean {
        return sharedPref.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getUserID(): Int {
        return sharedPref.getInt(KEY_USER_ID, -1)
    }

    fun getUserEmail(): String? {
        return sharedPref.getString(KEY_EMAIL, null)
    }

    fun getUserName(): String? {
        return sharedPref.getString(KEY_NAME, null)
    }

    fun logout() {
        val editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }
}