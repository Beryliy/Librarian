package com.wildhunt.librarian.data

import com.wildhunt.librarian.domain.repository.WitRepository

class WitRepositoryImpl(
    private val witApi: WitApi,
) : WitRepository {
    override suspend fun getWitKeywords(message: String): List<String> {
        val response = witApi.getWitResponse(message)
        return response.toSuccess().entities.flatMap { (_, v) -> v.map { it.value } }
    }
}
