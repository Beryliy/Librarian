package com.wildhunt.librarian.ui

import android.media.MediaRecorder
import android.util.Log
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path

class AudioRecorder {

    private var recorder: MediaRecorder? = null

    var isRecording: Boolean = false
        private set
    var recordingFileName: String? = null

    fun startRecording(fileName: String) {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(fileName)
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
                true
            } catch (e: IllegalStateException) {
                Log.e(AudioRecorder::class.java.simpleName, e.toString())
                recordingFileName = null
                false
            }
        }
    }

    fun stopRecording(): String? {
        val f = recordingFileName
        isRecording = false
        recordingFileName = null

        return try {
            recorder?.stop()
            recorder?.release()
            recorder = null

            f
        } catch (e: Exception) {
            f?.let { File(f).delete() }
            null
        }
    }
}