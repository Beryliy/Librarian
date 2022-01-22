package com.wildhunt.librarian.chat

import androidx.lifecycle.*
import com.wildhunt.librarian.AudioPlayer
import com.wildhunt.librarian.AudioRecorder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File

class ChatViewModel : ViewModel() {

  private val audioRecorder = AudioRecorder()
  private val audioPlayer = AudioPlayer()

  private val _messagesFlow = MutableStateFlow<List<Message>>(emptyList())
  val messagesFlow: Flow<List<Message>> = _messagesFlow

  fun startAudioRecording(fileName: String): String {
    audioRecorder.startRecording(fileName)
    return fileName
  }

  fun stopAudioRecording(fileName: String) {
    audioRecorder.stopRecording()
    viewModelScope.launch {
      _messagesFlow.emit(listOf(AudioMessage(fileName)))
    }
  }

  fun startPlaying(fileName: String) {
    audioPlayer.startPlaying(fileName)
  }
}