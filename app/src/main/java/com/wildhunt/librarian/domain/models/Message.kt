package com.wildhunt.librarian.domain.models

import java.util.*

enum class Sender {
    Me, AI
}

sealed class Message(
    val id: UUID,
    val sender: Sender,
    val date: Date,
) {
    class Text(
        id: UUID = UUID.randomUUID(),
        sender: Sender,
        date: Date = Date(),
        val text: String,
    ) : Message(id, sender, date)

    class Audio(
        id: UUID = UUID.randomUUID(),
        sender: Sender,
        date: Date = Date(),
        val path: String,
        val length: Long,
    ) : Message(id, sender, date)

    class Recommendation(
        id: UUID = UUID.randomUUID(),
        date: Date = Date(),
        val imageUrl: String? = null,
        val title: String,
        val description: String? = null,
        val authors: List<String> = listOf(),
        val genre: List<String> = listOf(),
    ): Message(id, Sender.AI, date)
}
