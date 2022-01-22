package com.wildhunt.librarian.sign_in

import androidx.lifecycle.ViewModel
import com.wildhunt.librarian.data.UserRepo
import com.wildhunt.librarian.sign_in.di.SignInScope
import javax.inject.Inject

@SignInScope
class SignInViewModel @Inject constructor(
  private val userRepo: UserRepo
) : ViewModel() {

  fun userSignedIn(idToken: String?) = userRepo.saveUserToken(idToken)
}