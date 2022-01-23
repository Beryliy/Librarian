package com.wildhunt.librarian.di

import android.content.Context
import com.wildhunt.librarian.ui.ChatViewModel
import com.wildhunt.librarian.ui.MainActivity
import dagger.BindsInstance
import dagger.Component

@Component(
  modules = [
    AppModule::class
  ]
)
interface AppComponent {
  fun inject(viewModel: ChatViewModel)

  @Component.Builder
  interface Builder {
    fun appContext(@BindsInstance context: Context): Builder
    fun appModule(module: AppModule): Builder
    fun build(): AppComponent
  }

  companion object {
    fun get(context: Context): AppComponent =
      DaggerAppComponent.builder()
        .appContext(context)
        .appModule(AppModule())
        .build()
  }
}