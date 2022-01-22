package com.wildhunt.librarian.domain.use_cases

import com.wildhunt.librarian.domain.repository.WitRepository

interface GetKeywordsUseCase {
    suspend fun get(message: String): List<String>
}

class GetKeywordsUseCaseImpl(
    private val repo: WitRepository
) : GetKeywordsUseCase {
    override suspend fun get(message: String): List<String> = repo.getWitKeywords(message)
}
