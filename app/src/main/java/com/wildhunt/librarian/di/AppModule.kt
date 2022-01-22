package com.wildhunt.librarian.di

import android.content.Context
import android.content.SharedPreferences
import android.speech.SpeechRecognizer
import com.squareup.moshi.Moshi
import com.wildhunt.librarian.USER_TOKEN
import com.wildhunt.librarian.data.BooksAPI
import com.wildhunt.librarian.data.UserRepo
import com.wildhunt.librarian.data.BooksRemoteRepository
import com.wildhunt.librarian.data.WitApi
import com.wildhunt.librarian.data.WitRepositoryImpl
import com.wildhunt.librarian.domain.repository.BooksRepository
import com.wildhunt.librarian.domain.repository.WitRepository
import com.wildhunt.librarian.domain.use_cases.GetBooksUseCase
import com.wildhunt.librarian.domain.use_cases.GetBooksUseCaseImpl
import com.wildhunt.librarian.domain.use_cases.GetKeywordsUseCase
import com.wildhunt.librarian.domain.use_cases.GetKeywordsUseCaseImpl
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
abstract class AppModule {

  companion object {
    @ApplicationScope
    @Provides
    fun provideGetKeywordsUseCase(witRepository: WitRepository): GetKeywordsUseCase =
      GetKeywordsUseCaseImpl(witRepository)

    @ApplicationScope
    @Provides
    fun provideGetBooksUseCase(booksRepository: BooksRepository): GetBooksUseCase =
      GetBooksUseCaseImpl(booksRepository)



    @ApplicationScope
    @Provides
    fun provideWitRepository(witApi: WitApi): WitRepository =
      WitRepositoryImpl(witApi)


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
    fun provideBooksAPI(preferences: SharedPreferences): BooksAPI {
      val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
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
        }.addInterceptor(HttpLoggingInterceptor().apply {
          setLevel(HttpLoggingInterceptor.Level.BODY)
        }).build()

      val retrofit = Retrofit.Builder()
        .baseUrl("https://www.googleapis.com/")
        .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
        .client(client)
        .build()

      return retrofit.create(BooksAPI::class.java)
    }

    @ApplicationScope
    @Provides
    fun provideBooksRepository(booksApi: BooksAPI): BooksRepository {
      return BooksRemoteRepository(booksApi)
    }

    @ApplicationScope
    @Provides
    fun provideUserRepo(preferences: SharedPreferences): UserRepo = UserRepo(preferences)

    @ApplicationScope
    @Provides
    fun provideWitApi(): WitApi {
      val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
          val request = chain.request()
            .newBuilder()
            .addHeader(
              "Authorization",
              "Bearer WKAEAOBORPWX3ERM7HWDLHWFD72R7K2H"
            ).build()
          chain.proceed(request)
        }
        .addInterceptor(HttpLoggingInterceptor().apply {
          setLevel(HttpLoggingInterceptor.Level.BODY)
        })
        .build()

      val retrofit = Retrofit.Builder()
        .baseUrl("https://api.wit.ai")
        .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
        .client(client)
        .build()

      return retrofit.create(WitApi::class.java)
    }
  }
}