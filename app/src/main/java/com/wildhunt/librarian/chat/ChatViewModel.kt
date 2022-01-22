package com.wildhunt.librarian.chat

import androidx.lifecycle.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

  private val messages = mutableListOf<Message>()

  private val _messagesFlow = MutableStateFlow<List<Message>>(messages)
  val messagesFlow: Flow<List<Message>> = _messagesFlow

  fun sendUserMessage(messageText: String) {
    messages.add(
      Message(
        Persona.ME,
        messageText
      )
    )
    viewModelScope.launch {
      _messagesFlow.emit(messages)
    }
  }
}