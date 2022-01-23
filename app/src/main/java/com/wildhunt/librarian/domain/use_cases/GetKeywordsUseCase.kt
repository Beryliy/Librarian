package com.wildhunt.librarian.domain.use_cases

import com.wildhunt.librarian.domain.models.Message
import com.wildhunt.librarian.domain.repository.WitRepository

interface GetKeywordsUseCase {
    suspend fun get(message: Message): List<String>
}

class GetKeywordsUseCaseImpl(
    private val repo: WitRepository
) : GetKeywordsUseCase {
    override suspend fun get(message: Message): List<String> = when (message) {
        is Message.Text -> repo.getWitKeywords(message.text)
        else -> listOf()
    }
}
