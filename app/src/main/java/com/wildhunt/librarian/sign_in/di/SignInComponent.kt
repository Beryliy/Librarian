package com.wildhunt.librarian.sign_in.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.wildhunt.librarian.di.AppComponent
import com.wildhunt.librarian.di.AppComponentProvider
import com.wildhunt.librarian.sign_in.SignInActivity
import dagger.BindsInstance
import dagger.Component

@SignInScope
@Component(
  dependencies = [
    AppComponent::class
  ],
  modules = [
    SignInModule::class
  ]
)
interface SignInComponent {
  fun inject(activity: SignInActivity)

  fun viewModelFactory(): ViewModelProvider.Factory

  @Component.Builder
  interface Builder {
    fun context(@BindsInstance context: Context): Builder
    fun appComponent(component: AppComponent): Builder
    fun build(): SignInComponent
  }

  companion object {
    fun get(context: Context): SignInComponent =
      DaggerSignInComponent.builder()
        .context(context)
        .appComponent((context as AppComponentProvider).component)
        .build()
  }
}