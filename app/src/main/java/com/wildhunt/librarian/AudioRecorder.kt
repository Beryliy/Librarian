package com.wildhunt.librarian

import android.media.MediaRecorder
import android.util.Log
import java.io.IOException
import java.lang.IllegalStateException

class AudioRecorder {

  private var recorder: MediaRecorder? = null

  fun startRecording(fileName: String) {
    recorder = MediaRecorder().apply {
      setAudioSource(MediaRecorder.AudioSource.MIC)
      setOutputFile(fileName)
      setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
      setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

      try {
        prepare()
      } catch (e: IOException) {
        Log.e(AudioRecorder::class.java.simpleName, e.toString())
      } catch (e: IllegalStateException) {
        Log.e(AudioRecorder::class.java.simpleName, e.toString())
      }

      try {
        start()
      } catch (e: IllegalStateException) {
        Log.e(AudioRecorder::class.java.simpleName, e.toString())
      }
    }
  }

  fun stopRecording() {
    recorder?.stop()
    recorder?.release()
    recorder = null
  }
}