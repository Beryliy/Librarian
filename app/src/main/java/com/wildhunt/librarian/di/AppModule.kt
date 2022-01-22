package com.wildhunt.librarian.di

import android.content.Context
import android.content.SharedPreferences
import android.speech.SpeechRecognizer
import com.squareup.moshi.Moshi
import com.wildhunt.librarian.USER_TOKEN
import com.wildhunt.librarian.data.BooksAPI
import com.wildhunt.librarian.data.UserRepo
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
abstract class AppModule {

  companion object {
    @ApplicationScope
    @Provides
    fun provideSpeechRecogniser(context: Context): SpeechRecognizer =
      SpeechRecognizer.createSpeechRecognizer(context)

    @ApplicationScope
    @Provides
    fun providePreferences(context: Context): SharedPreferences =
      context.getSharedPreferences(context.packageName + "_preferences", Context.MODE_PRIVATE)

    @ApplicationScope
    @Provides
    fun provideInterceptor(preferences: SharedPreferences): Interceptor =
      Interceptor { chain ->
        val request = chain.request().newBuilder().apply {
          preferences.getString(USER_TOKEN, "")
          addHeader(
            "Authorization",
            "Bearer ${preferences.getString(USER_TOKEN, "")}"
          )
          addHeader(
            "Accept",
            "application/json"
          )
        }.build()
        chain.proceed(request)
      }

    @ApplicationScope
    @Provides
    fun provideOkHttp(interceptor: Interceptor): OkHttpClient =
      OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    @ApplicationScope
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
      Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://books.googleapis.com")
        .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
        .build()

    @ApplicationScope
    @Provides
    fun provideBooksAPI(retrofit: Retrofit): BooksAPI =
      retrofit.create(BooksAPI::class.java)

    @ApplicationScope
    @Provides
    fun provideUserRepo(preferences: SharedPreferences): UserRepo =
      UserRepo(preferences)
  }
}