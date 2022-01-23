package com.wildhunt.librarian.di

import android.content.Context
import com.wildhunt.librarian.ui.ChatViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.wildhunt.librarian.ui.MainActivity
import com.wildhunt.librarian.data.UserRepo
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
  modules = [
    AppModule::class
  ]
)
interface AppComponent {
  fun userRepo(): UserRepo
  fun inject(viewModel: ChatViewModel)
  @ExperimentalPermissionsApi
  fun inject(activity: MainActivity)

  @Component.Builder
  interface Builder {
    fun appContext(@BindsInstance context: Context): Builder
    fun build(): AppComponent
  }

  companion object {
    fun get(context: Context): AppComponent =
      DaggerAppComponent.builder()
        .appContext(context)
        .build()
  }
}