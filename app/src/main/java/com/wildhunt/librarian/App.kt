package com.wildhunt.librarian

import android.app.Application
import com.wildhunt.librarian.di.AppComponent
import com.wildhunt.librarian.di.AppComponentProvider

class App : Application(), AppComponentProvider {
  override val component: AppComponent by lazy { AppComponent.get(applicationContext) }
}