package com.wildhunt.librarian.di

import android.content.Context
import com.wildhunt.librarian.data.UserRepo
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
  modules = [
    AppModule::class,
    ViewModelModule::class
  ]
)
interface AppComponent {
  fun userRepo(): UserRepo

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