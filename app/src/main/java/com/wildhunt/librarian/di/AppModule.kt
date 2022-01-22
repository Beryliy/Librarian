package com.wildhunt.librarian.di

import android.content.Context
import android.content.SharedPreferences
import android.speech.SpeechRecognizer
import com.squareup.moshi.Moshi
import com.wildhunt.librarian.USER_TOKEN
import com.wildhunt.librarian.data.BooksAPI
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
class AppModule {

  @Provides
  fun provideSpeechRecogniser(context: Context): SpeechRecognizer =
    SpeechRecognizer.createSpeechRecognizer(context)

  @Provides
  fun providePreferences(context: Context): SharedPreferences =
    context.getSharedPreferences(context.packageName + "_preferences", Context.MODE_PRIVATE)

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

  @Provides
  fun provideOkHttp(interceptor: Interceptor): OkHttpClient =
    OkHttpClient.Builder()
      .addInterceptor(interceptor)
      .build()

  @Provides
  fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
    Retrofit.Builder()
      .client(okHttpClient)
      .baseUrl("https://books.googleapis.com")
      .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
      .build()

  @Provides
  fun provideBooksAPI(retrofit: Retrofit): BooksAPI =
    retrofit.create(BooksAPI::class.java)
}