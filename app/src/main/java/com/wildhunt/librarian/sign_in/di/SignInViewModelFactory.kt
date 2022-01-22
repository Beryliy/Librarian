package com.wildhunt.librarian.sign_in.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wildhunt.librarian.data.UserRepo
import com.wildhunt.librarian.sign_in.SignInViewModel

class SignInViewModelFactory(
  private val userRepo: UserRepo
) : ViewModelProvider.Factory {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T =
    SignInViewModel(userRepo) as T
}