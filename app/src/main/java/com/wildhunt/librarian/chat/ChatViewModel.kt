package com.wildhunt.librarian.chat

import androidx.lifecycle.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel(), LifecycleEventObserver {

  private val _messages = MutableStateFlow(emptyList<Message>())
  val messages: Flow<List<Message>> = _messages

  override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
    viewModelScope.launch {
      when (event) {
        Lifecycle.Event.ON_START -> {
          _messages.emit(
            listOf(
              Message(
                Persona.AI,
                "Hey, wats up?"
              ),
              Message(
                Persona.ME,
                "Not bad"
              ),
              Message(
                Persona.AI,
                "What do you want to read?"
              ),
              Message(
                Persona.ME,
                "Something"
              ),
              Message(
                Persona.AI,
                "Any genre?"
              ),
              Message(
                Persona.ME,
                "Fiction"
              ),

              Message(
                Persona.AI,
                "Here it is"
              ),
            )
          )
        }
      }
    }
  }
}