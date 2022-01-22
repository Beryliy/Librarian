package com.wildhunt.librarian.di

import android.content.Context
import android.speech.SpeechRecognizer
import dagger.Module
import dagger.Provides

@Module
class AppModule {

  @Provides
  fun provideSpeechRecogniser(context: Context): SpeechRecognizer =
    SpeechRecognizer.createSpeechRecognizer(context)


}