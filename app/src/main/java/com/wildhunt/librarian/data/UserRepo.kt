package com.wildhunt.librarian.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.wildhunt.librarian.USER_TOKEN

class UserRepo(private val preferences: SharedPreferences) {

  fun saveUserToken(idToken: String?) {
    preferences.edit { putString(USER_TOKEN, idToken) }
  }
}