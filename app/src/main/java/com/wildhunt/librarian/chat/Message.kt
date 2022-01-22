package com.wildhunt.librarian.chat

open class Message(
  val sender: Persona,
  val message: String,
)

class AudioMessage(
  val fileName: String
) : Message(sender = Persona.ME, message = "")


enum class Persona {
  ME,
  AI
}