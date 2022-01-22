package com.wildhunt.librarian.chat

data class Message(
  val sender: Persona,
  val message: String,
)


enum class Persona {
  ME,
  AI
}