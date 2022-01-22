package com.wildhunt.librarian.sign_in.di

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.wildhunt.librarian.data.UserRepo
import dagger.Module
import dagger.Provides

@Module
abstract class SignInModule {

  companion object {
    @SignInScope
    @Provides
    fun provideSighInOptions(): GoogleSignInOptions =
      GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestIdToken("927666289875-ps7q434jr7cg22oaeq4eagk96one3eq7.apps.googleusercontent.com")
        .build()

    @SignInScope
    @Provides
    fun provideSignInClient(
      context: Context,
      gso: GoogleSignInOptions
    ): GoogleSignInClient = GoogleSignIn.getClient(context, gso)

    @SignInScope
    @Provides
    fun provideSignInIntent(signInClient: GoogleSignInClient): Intent = signInClient.signInIntent

    @SignInScope
    @Provides
    fun provideViewModelFactory(userRepo: UserRepo): ViewModelProvider.Factory =
      SignInViewModelFactory(userRepo)
  }
}