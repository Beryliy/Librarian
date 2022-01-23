package com.wildhunt.librarian.ui

import androidx.lifecycle.*
import com.wildhunt.librarian.domain.models.Message
import com.wildhunt.librarian.domain.models.Sender
import com.wildhunt.librarian.domain.models.TextMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

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
//      _messagesFlow.emit(listOf(AudioMessage(fileName)))
    }
  }

  fun startPlaying(fileName: String) {
    audioPlayer.startPlaying(fileName)
  }

  fun newUserMessage(message: Message) {
    viewModelScope.launch {
      val messages = listOf(message) + _messagesFlow.value
      _messagesFlow.emit(messages)

      launch {
        delay(3000)
        val n = listOf(TextMessage(sender = Sender.AI, text = "asdf sadf asd f")) + _messagesFlow.value
        _messagesFlow.emit(n)
      }
    }
  }
}