package com.wildhunt.librarian.di

import android.content.Context
import android.content.SharedPreferences
import android.speech.SpeechRecognizer
import com.squareup.moshi.Moshi
import com.wildhunt.librarian.USER_TOKEN
import com.wildhunt.librarian.data.BooksAPI
import com.wildhunt.librarian.data.WitApi
import com.wildhunt.librarian.data.WitRepositoryImpl
import com.wildhunt.librarian.domain.repository.WitRepository
import com.wildhunt.librarian.domain.use_cases.GetKeywordsUseCase
import com.wildhunt.librarian.domain.use_cases.GetKeywordsUseCaseImpl
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier

@Module
class AppModule {
  @Provides
  fun provideGetKeywordsUseCase(witRepository: WitRepository): GetKeywordsUseCase {
    return GetKeywordsUseCaseImpl(witRepository)
  }

  @Provides
  fun provideWitRepository(witApi: WitApi): WitRepository {
    return WitRepositoryImpl(witApi)
  }

  @Provides
  fun provideWitApi(retrofit: Retrofit): WitApi {
    val client = OkHttpClient.Builder()
      .addInterceptor { chain ->
        val request = chain.request()
          .newBuilder()
          .addHeader(
            "Authorization",
            "Bearer WKAEAOBORPWX3ERM7HWDLHWFD72R7K2H"
          ).build()

        chain.proceed(request)
      }.build()

    val retrofit = Retrofit.Builder()
      .baseUrl("https://api.wit.ai")
      .client(client)
      .build()

    return retrofit.create(WitApi::class.java)
  }

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