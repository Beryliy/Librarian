package com.wildhunt.librarian.ui

import android.media.MediaRecorder
import android.util.Log
import java.io.IOException

class AudioRecorder {

  private var recorder: MediaRecorder? = null

    var isRecording: Boolean = false
        private set(value) {
            field = value
        }
    var recordingFileName: String? = null

    fun startRecording(fileName: String) {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFile(fileName)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
            } catch (e: IOException) {
                Log.e(AudioRecorder::class.java.simpleName, e.toString())
            } catch (e: IllegalStateException) {
                Log.e(AudioRecorder::class.java.simpleName, e.toString())
            }

            isRecording = try {
                start()
                recordingFileName = fileName
                Log.d("123123123", "8")

                true
            } catch (e: IllegalStateException) {
                Log.e(AudioRecorder::class.java.simpleName, e.toString())
                recordingFileName = null
                Log.d("123123123", "9")
                false
            }
        }
    }

    fun stopRecording(): String? {
        val f = recordingFileName
        recorder?.stop()
        recorder?.release()
        isRecording = false
        recorder = null
        recordingFileName = null

        return f
    }
}