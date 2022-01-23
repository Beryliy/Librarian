package com.wildhunt.librarian.domain.models

import java.util.*

enum class Sender {
    Me, AI
}

open class Message(
    val id: UUID,
    val sender: Sender,
    val date: Date,
)

class TextMessage(
    id: UUID = UUID.randomUUID(),
    sender: Sender,
    date: Date = Date(),
    val text: String,
) : Message(id, sender, date)

class AudioMessage(
    id: UUID = UUID.randomUUID(),
    sender: Sender,
    date: Date = Date(),
    val path: String,
    val length: Long,
) : Message(id, sender, date)

class RecommendationMessage(
    id: UUID = UUID.randomUUID(),
    sender: Sender,
    date: Date = Date(),
    val imageUrl: String? = null,
    val title: String,
    val description: String? = null,
    val authors: List<String> = listOf(),
    val genre: List<String> = listOf(),
): Message(id, sender, date)


