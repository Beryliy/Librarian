package com.wildhunt.librarian.ui

import android.media.MediaPlayer
import android.util.Log
import java.io.IOException

class AudioPlayer {

  private var player: MediaPlayer? = null

  fun startPlaying(fileName: String) {
    player = MediaPlayer().apply {
      try {
        setDataSource(fileName)
        prepare()
        start()
      } catch (e: IOException) {
        Log.e(AudioPlayer::class.java.simpleName, "prepare() failed")
      }
    }
  }

  fun pausePlaying() {
    player?.pause()
  }
}